package com.example.lukas.zagrajmy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.lukas.zagrajmy.model.Match;
import com.example.lukas.zagrajmy.services.AppService;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity  extends FragmentActivity implements// czy to ma backward compatibility????
        GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private static final LatLng PERTH = new LatLng(-31.952854, 115.857342);
    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
    private static final LatLng BRISBANE = new LatLng(-27.47093, 153.0235);

    private Marker mPerth;
    private Marker mSydney;
    private Marker mBrisbane;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        new AsyncCaller().execute();
    }

    /** Called when the map is ready. */
    @Override
    public void onMapReady(GoogleMap map) {

        AppService service = AppService.getService();

        mMap = map;

        // Add some markers to the map, and add a data object to each marker.
        mPerth = mMap.addMarker(new MarkerOptions()
                .position(PERTH)
                .title("Perth"));
        mPerth.setTag(0);

        mSydney = mMap.addMarker(new MarkerOptions()
                .position(SYDNEY)
                .title("Sydney"));
        mSydney.setTag(1);

        mBrisbane = mMap.addMarker(new MarkerOptions()
                .position(BRISBANE)
                .title("Brisbane"));
        mBrisbane.setTag(2);

        List<Match> matches = service.getMatches();

        for (Match match: matches) {
            Marker mMarker = mMap.addMarker(new MarkerOptions()
                    .position(match.getLatLng())
                    .title(match.getTitle()));
            mMarker.setTag(match.getId());
        }

        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        Integer id = 1;//(Integer) marker.getTag();
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
        if (resultCode == RESULT_OK) {
            Match match = (Match)data.getParcelableExtra("match");
            Marker mMarker = mMap.addMarker(new MarkerOptions()
                    .position(match.getLatLng())
                    .title(match.getTitle()));
            mMarker.setTag(0);
        }
    }


    private class AsyncCaller extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog pdLoading = new ProgressDialog(MapsActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("Loading...");
            pdLoading.show();
        }
        @Override
        protected Void doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            //this method will be running on UI thread

            pdLoading.dismiss();
        }

    }
}