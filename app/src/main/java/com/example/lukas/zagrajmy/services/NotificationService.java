package com.example.lukas.zagrajmy.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lukas.zagrajmy.AccountActivity;
import com.example.lukas.zagrajmy.MapsActivity;
import com.example.lukas.zagrajmy.MatchActivity;
import com.example.lukas.zagrajmy.R;
import com.example.lukas.zagrajmy.VolleySingleton;
import com.example.lukas.zagrajmy.model.Match;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {
    int mStartMode;       // indicates how to behave if the service is killed
    Timer mTimer;
    MyTimerTask mTimerTask;
    int mUserId;
    boolean isScheduled = false;
    final int latency = 3600;
    final int interval = 10;

    @Override
    public void onCreate() {
        mTimer = new Timer();
        mTimerTask = new MyTimerTask();

        settings = getSharedPreferences("APP_STATE", 0);
        volley = VolleySingleton.getInstance(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!isScheduled) {
            mTimer.schedule(mTimerTask, 0, interval * 1000);
            isScheduled = true;
        }
        return mStartMode;
    }

    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            mUserId = getCurrentUserIdFromCache();
            if (mUserId == -1)
                return;
            String matches_url = getServiceUrl() + "/matches";
            JsonArrayRequest jsObjRequest = new JsonArrayRequest
                    (Request.Method.GET, matches_url, (String) null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            handleResponse(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("", error.toString());
                        }
                    });
            jsObjRequest.setShouldCache(false);
            volley.getRequestQueue().add(jsObjRequest);
        }
    }

    private void handleResponse(JSONArray response) {
        String json = response.toString();
        Gson g = getGson();
        Log.i("", json);
        try {
            List<Match> matches = g.fromJson(json, new TypeToken<List<Match>>() {
            }.getType());
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.SECOND, latency);
            Date now = cal.getTime();
            cal.add(Calendar.MILLISECOND, interval * 1000 + 1);
            Date future = cal.getTime();
            for (Match match : matches) {
                if (match.getParticipantsIds().contains(mUserId)) {
                    Date matchDate = match.getDate();
                    if (now.before(matchDate) && future.after(matchDate))
                        if(match.getParticipantsIds().size()>=match.getLimit())
                            sendNotification(match.getId());
                }
            }
        } catch (Exception ex) {
            Log.e("", ex.toString());
        }
    }

    private void sendNotification(int matchId) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(NotificationService.this)
                        .setSmallIcon(R.drawable.googleg_standard_color_18)
                        .setContentTitle("FULL TEAM!!!")
                        .setContentText("The match is starting in 1 hour!");
        Intent resultIntent = new Intent(NotificationService.this, MapsActivity.class);
        resultIntent.putExtra("match_id", matchId);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(NotificationService.this);
        stackBuilder.addParentStack(MapsActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(matchId, mBuilder.build());
    }

    @Override
    public void onDestroy() {
        isScheduled = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    protected VolleySingleton volley;

    protected SharedPreferences settings;

    protected int getCurrentUserIdFromCache() {
        int userId = settings.getInt("userId", -1);
        return userId;
    }

    protected String getServiceUrl() {
        return "http://elkade.pythonanywhere.com/";
    }


    protected Gson getGson() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
    }
}