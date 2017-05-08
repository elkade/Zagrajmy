package com.example.lukas.zagrajmy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lukas.zagrajmy.model.Match;
import com.example.lukas.zagrajmy.widgets.DatePickerFragment;
import com.example.lukas.zagrajmy.widgets.TimePickerFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddMatchActivity extends BaseActivity {
    @BindView(R.id.match_time_picker_button)
    Button pickTimeButton;
    @BindView(R.id.match_date_picker_button)
    Button pickDateButton;
    @BindView(R.id.participants_number_spinner)
    Spinner participantsNumberSpinner;

    Match mMatch;

    Calendar mCal = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_match);
        ButterKnife.bind(this);
        createSpinner();
        mMatch = new Match();

        LatLng latLng = getIntent().getParcelableExtra("latLng");
        mMatch.setLatLng(latLng);
        mCal.setTimeInMillis(0);
    }

    private void createSpinner() {
        int n = 99;
        Integer[] items = new Integer[n];
        for (int i=0;i<n;i++)
            items[i] = i+1;
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        participantsNumberSpinner.setAdapter(adapter);
    }

    @OnClick(R.id.match_time_picker_button)
    public void onClickPickTimeButton(){
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @OnClick(R.id.match_date_picker_button)
    public void onClickPickDateButton(){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void setDate(int year, int month, int day) {
        mCal.set(Calendar.YEAR, year);
        mCal.set(Calendar.MONTH, month);
        mCal.set(Calendar.DAY_OF_MONTH, day);
    }

    public void setTime(int hourOfDay, int minute) {
        mCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mCal.set(Calendar.MINUTE, minute);
    }
    @OnClick(R.id.match_create_button)
    public void onClickCreateButton(){
        mMatch.setDate(mCal.getTime());

        String url = "http://elkade.pythonanywhere.com/matches";
        Gson g = new Gson();
        String jsonString = g.toJson(mMatch);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonString, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String json = response.toString();
                        Gson g = new Gson();
                        mMatch = g.fromJson(json, Match.class);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("", error.toString());
                    }
                });

        volley.getRequestQueue().add(jsObjRequest);

        Intent resultIntent = new Intent();

        resultIntent.putExtra("match", mMatch);
        setResult(Activity.RESULT_OK, resultIntent);

        finish();
    }
}
