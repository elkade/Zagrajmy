package com.example.lukas.zagrajmy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lukas.zagrajmy.widgets.DatePickerFragment;
import com.example.lukas.zagrajmy.widgets.TimePickerFragment;
import com.example.lukas.zagrajmy.model.Match;
import com.google.android.gms.maps.model.LatLng;

import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddMatchActivity extends AppCompatActivity {
    @BindView(R.id.match_time_picker_button)
    Button pickTimeButton;
    @BindView(R.id.match_date_picker_button)
    Button pickDateButton;
    @BindView(R.id.participants_number_spinner)
    Spinner participantsNumberSpinner;

    Match mMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_match);
        ButterKnife.bind(this);
        createSpinner();
        mMatch = new Match();

        LatLng latLng = getIntent().getParcelableExtra("latLng");
        mMatch.setLatLng(latLng);
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
        mMatch.setDate(new GregorianCalendar(year, month, day).getTime());
        Toast.makeText(this, "Date: "+year+month+day,Toast.LENGTH_LONG).show();
    }

    public void setTime(int hourOfDay, int minute) {
        Toast.makeText(this, "Time: "+hourOfDay+minute,Toast.LENGTH_LONG).show();
    }
    @OnClick(R.id.match_create_button)
    public void onClickCreateButton(){

        Intent resultIntent = new Intent();
        resultIntent.putExtra("match", mMatch);
        setResult(Activity.RESULT_OK, resultIntent);

        finish();
    }
}
