package com.example.lukas.zagrajmy;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lukas.zagrajmy.model.Match;
import com.example.lukas.zagrajmy.utils.MySingleton;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MatchActivity extends AppCompatActivity {

    private int matchId;

    private Match mMatch;

    @BindView(R.id.match_description)
    TextView mDescriptionView;

    @BindView(R.id.match_date)
    TextView mDateView;

    ProgressDialog pdLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        ButterKnife.bind(this);
        matchId = getIntent().getIntExtra("match_id", -1);
        //pdLoading  = new ProgressDialog(this);
        //new MatchActivity.AsyncCaller().execute();
        String url = "http://elkade.pythonanywhere.com/matches/" + matchId;
        //pdLoading.setMessage("Loading...");
        //pdLoading.show();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        ObjectMapper objectMapper = new ObjectMapper();
                        String json = response.toString();
                        try {
                            mMatch = objectMapper.readValue(json, Match.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //pdLoading.dismiss();

                        fillView();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //pdLoading.dismiss();

                    }
                });

        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);

    }

    private void fillView() {
        mDescriptionView.setText(mMatch.getTitle());
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        Date date = mMatch.getDate();
        String formattedDate = df.format(date);
        mDateView.setText(formattedDate);
    }
}
