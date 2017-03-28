package com.example.lukas.zagrajmy.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;

public class Match {
    private int id;
    private LatLng latLng;
    private String name;
    private String description;
    private int authorId;
    private List<Integer> participantsIds;
    private Date date;
    private int age;
    private int limit;


    public Match(int id, LatLng latLng, String name){
        this.id = id;
        this.latLng = latLng;
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public List<Integer> getParticipantsIds() {
        return participantsIds;
    }

    public void setParticipantsIds(List<Integer> participantsIds) {
        this.participantsIds = participantsIds;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}