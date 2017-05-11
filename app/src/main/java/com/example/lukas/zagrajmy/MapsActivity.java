package com.example.lukas.zagrajmy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.lukas.zagrajmy.model.Match;
import com.example.lukas.zagrajmy.services.NotificationService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.List;

public class MapsActivity extends BaseActivity implements// czy to ma backward compatibility????
        GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;

    private CameraPosition lastCameraPosition;
    ProgressDialog pdLoading;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        lastCameraPosition = getLastCameraPosition();

        Intent serviceIntent = new Intent(this, NotificationService.class);
        startService(serviceIntent);
        int matchId = getIntent().getIntExtra("match_id", -1);
        if(matchId != -1){
            Intent matchIntent = new Intent(this, MatchActivity.class);
            matchIntent.putExtra("match_id", matchId);
            startActivity(matchIntent);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        refresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.go_to_profile) {
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        Integer id = (Integer) marker.getTag();
        Intent intent = new Intent(this, MatchActivity.class);
        intent.putExtra("match_id", id);
        startActivity(intent);
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Intent intent = new Intent(this, AddMatchActivity.class);
        intent.putExtra("latLng", latLng);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            Match match = (Match) data.getParcelableExtra("match");

            Marker mMarker = mMap.addMarker(new MarkerOptions()
                    .position(match.getLatLng())
                    .title(match.getTitle()));
            mMarker.setTag(match.getId());
        }
    }

    @Override
    protected void onDestroy() {
        saveCameraPosition();
        super.onDestroy();
    }

    private void refresh() {
        Log.i("", "refreshing");
        pdLoading = new ProgressDialog(MapsActivity.this);
        pdLoading.setMessage("Loading...");
        pdLoading.show();
        String url = "http://elkade.pythonanywhere.com/matches";
        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, url, (String) null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                handleResonse(response);
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("", error.toString());
                        pdLoading.dismiss();
                    }
                });
        Log.i("", "sending request");
        jsObjRequest.setShouldCache(false);
        volley.getRequestQueue().add(jsObjRequest);
    }

    private void handleResonse(JSONArray response) {
        Log.i("", "on response");
        pdLoading.dismiss();
        String json = response.toString();
        Gson g = getGson();
        Log.i("", json);
        try {
            List<Match> matches = g.fromJson(json, new TypeToken<List<Match>>() {
            }.getType());
            mMap.clear();
            for (Match match : matches) {
                Marker mMarker = mMap.addMarker(new MarkerOptions()
                        .position(match.getLatLng())
                        .title(match.getTitle()));
                mMarker.setTag(match.getId());
            }
            mMap.setOnMarkerClickListener(MapsActivity.this);
            mMap.setOnMapClickListener(MapsActivity.this);
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(lastCameraPosition));
        } catch (Exception ex) {
            Log.e("", ex.toString());
        }
    }

    protected CameraPosition getLastCameraPosition() {
        SharedPreferences settings = getSharedPreferences("APP_STATE", 0);
        double longitude = settings.getFloat("longitude", 0);
        double latitude = settings.getFloat("latitude", 0);
        float zoom = settings.getFloat("zoom", 1);
        LatLng startPosition = new LatLng(latitude, longitude);

        return new CameraPosition.Builder()
                .target(startPosition)
                .zoom(zoom)
                .build();
    }

    protected void saveCameraPosition() {
        CameraPosition mMyCam = mMap.getCameraPosition();
        double longitude = mMyCam.target.longitude;
        double latitude = mMyCam.target.latitude;
        float zoom = mMyCam.zoom;

        SharedPreferences settings = getSharedPreferences("APP_STATE", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat("longitude", (float) longitude);
        editor.putFloat("latitude", (float) latitude);
        editor.putFloat("zoom", zoom);
        editor.apply();
    }
}