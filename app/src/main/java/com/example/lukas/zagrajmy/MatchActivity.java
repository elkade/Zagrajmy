package com.example.lukas.zagrajmy;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lukas.zagrajmy.model.Match;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MatchActivity extends BaseActivity {

    private int matchId;

    private Match mMatch;

    @BindView(R.id.match_title)
    TextView mTitleView;

    @BindView(R.id.match_date)
    TextView mDateView;

    ProgressDialog pdLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        ButterKnife.bind(this);
        matchId = getIntent().getIntExtra("match_id", -1);
        String url = "http://elkade.pythonanywhere.com/matches/" + matchId;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String json = response.toString();
                        Gson g = new Gson();
                        mMatch = g.fromJson(json, Match.class);
                        fillView();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        volley.getRequestQueue().add(jsObjRequest);

    }

    private void fillView() {
        mTitleView.setText(mMatch.getTitle());
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        Date date = mMatch.getDate();
        String formattedDate = df.format(date);
        mDateView.setText(formattedDate);
    }
}
