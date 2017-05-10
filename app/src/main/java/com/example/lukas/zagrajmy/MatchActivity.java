package com.example.lukas.zagrajmy;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lukas.zagrajmy.model.Match;
import com.example.lukas.zagrajmy.model.Participant;
import com.example.lukas.zagrajmy.widgets.DatePickerFragment;
import com.example.lukas.zagrajmy.widgets.ParticipantsViewAdapter;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MatchActivity extends BaseActivity {

    private int matchId;

    private Match mMatch;

    @BindView(R.id.match_title)
    TextView mTitleView;

    @BindView(R.id.match_date)
    TextView mDateView;

    @BindView(R.id.match_join_button)
    Button mJoinButton;

    @BindView(R.id.match_leave_button)
    Button mLeaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        redirectIfUserNotRegistered();
        setContentView(R.layout.activity_match);
        ButterKnife.bind(this);
        matchId = getIntent().getIntExtra("match_id", -1);
        refresh();

        List<Participant> data = fill_with_data();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.participantsListView);
        ParticipantsViewAdapter adapter = new ParticipantsViewAdapter(data, getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public List<Participant> fill_with_data() {

        List<Participant> data = new ArrayList<>();

        data.add(new Participant("Batman vs Superman", "Following the destruction of Metropolis, Batman embarks on a personal vendetta against Superman ", R.drawable.common_google_signin_btn_icon_dark));
        data.add(new Participant("X-Men: Apocalypse", "X-Men: Apocalypse is an upcoming American superhero film based on the X-Men characters that appear in Marvel Comics ", R.drawable.common_google_signin_btn_icon_dark));
        data.add(new Participant("Captain America: Civil War", "A feud between Captain America and Iron Man leaves the Avengers in turmoil.  ", R.drawable.common_google_signin_btn_icon_dark));
        data.add(new Participant("Kung Fu Panda 3", "After reuniting with his long-lost father, Po  must train a village of pandas", R.drawable.common_google_signin_btn_icon_dark));
        data.add(new Participant("Warcraft", "Fleeing their dying home to colonize another, fearsome orc warriors invade the peaceful realm of Azeroth. ", R.drawable.common_google_signin_btn_icon_dark));
        data.add(new Participant("Alice in Wonderland", "Alice in Wonderland: Through the Looking Glass ", R.drawable.common_google_signin_btn_icon_dark));

        return data;
    }

    private void refresh() {
        String url = getServiceUrl() + "/matches/" + matchId;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        handleMatchResponse(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        volley.getRequestQueue().add(jsObjRequest);
    }

    private void handleMatchResponse(JSONObject response) {
        String json = response.toString();
        Gson g = new Gson();
        mMatch = g.fromJson(json, Match.class);
        mTitleView.setText(mMatch.getTitle());
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        Date date = mMatch.getDate();
        String formattedDate = df.format(date);
        mDateView.setText(formattedDate);
        List<Integer> participants = mMatch.getParticipantsIds();
        int userId = getCurrentUserIdFromCache();
        boolean userParticipates = false;
        for(Integer p : participants){
            if(p.equals(userId)) {
                userParticipates = true;
                break;
            }
        }
        if (userParticipates) {
            mLeaveButton.setVisibility(View.VISIBLE);
            mJoinButton.setVisibility(View.GONE);
        } else {
            mLeaveButton.setVisibility(View.GONE);
            mJoinButton.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.match_join_button)
    public void onClickJoinButton() {
        int userId = getCurrentUserIdFromCache();
        String url = getServiceUrl() + "/matches/" + mMatch.getId() + "/participants/" + userId;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.PUT, url, (String) null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        refresh();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("",error.toString());
                    }
                });
        volley.getRequestQueue().add(jsObjRequest);
    }

    @OnClick(R.id.match_leave_button)
    public void onClickLeaveButton() {
        int userId = getCurrentUserIdFromCache();
        String url = getServiceUrl() + "/matches/" + mMatch.getId() + "/participants/" + userId;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.DELETE, url, (String) null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        refresh();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("",error.toString());
                    }
                });
        volley.getRequestQueue().add(jsObjRequest);
    }
}
