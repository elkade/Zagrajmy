package com.example.lukas.zagrajmy.services;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.lukas.zagrajmy.model.Match;
import com.example.lukas.zagrajmy.model.User;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class AppService {

    private static AppService serviceInstance;

    public static final AppService getService(){
        if(serviceInstance == null)
            serviceInstance = new AppService();
        return serviceInstance;
    }

    private AppService(){

    }

    public User getCurrentUser(){
        return new User();
    }

    public User getUser(int id){
        return null;
    }

    public void updateCurrentUser(User user){

    }

    public Match getMatch(int id){
        return null;
    }

    public List<Match> getMatches(/*long latitude, long longitude, long dx, long dy*/){
        Match match1 =  new Match(45, new LatLng(-21.952854, 115.857342), "DAFFFFSDF");
        Match match2 =  new Match(44, new LatLng(-23.87365, 151.20689), "DFDFGER");
        Match match3 =  new Match(145, new LatLng(-21.47093, 153.0235), "@%$UE%RYRE");
        List<Match> l =  new ArrayList<Match>();
        l.add(match1);
        l.add(match2);
        l.add(match3);
        return l;
    }

    public Match createMatch(Match match){
        return null;
    }

    public void joinMatch(int matchId){

    }


    public void setCurrentUserPhoto(Bitmap currentUserPhoto) {
        //this.currentUserPhoto = currentUserPhoto;
    }
}
