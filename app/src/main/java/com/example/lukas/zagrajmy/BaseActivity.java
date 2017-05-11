package com.example.lukas.zagrajmy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class BaseActivity extends AppCompatActivity {

    protected VolleySingleton volley;

    protected SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getSharedPreferences("APP_STATE", 0);
        volley =  VolleySingleton.getInstance(getApplicationContext());
    }

    protected int getCurrentUserIdFromCache(){
        int userId = settings.getInt("userId", -1);
        return userId;
    }

    protected void redirectIfUserNotRegistered(){
        int userId = getCurrentUserIdFromCache();
        if(userId == -1){
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
        }
    }
    protected String getServiceUrl(){
        return "http://elkade.pythonanywhere.com/";
    }


    protected Gson getGson(){
        return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
    }
}
