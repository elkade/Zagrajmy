package com.example.lukas.zagrajmy.model;

import android.graphics.Bitmap;

/**
 * Created by Lukas on 2017-03-23.
 */

public class User {
    private int id;
    private String name;
    private int year;
    private Bitmap photo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public String getPhotoUrl() {
        return "http://www.zastavki.com/pictures/originals/2014/Girls_Movie_star_Sasha_Grey__074153_.jpg";
    }
}
