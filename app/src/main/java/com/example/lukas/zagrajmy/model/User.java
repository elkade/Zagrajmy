package com.example.lukas.zagrajmy.model;

import android.graphics.Bitmap;

/**
 * Created by Lukas on 2017-03-23.
 */

public class User {
    private int id;
    private String name;
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

    public Bitmap getPhoto() {
        return photo;
    }

    public String getPhotoUrl() {
        return "http://elkade.pythonanywhere.com/images/1";
    }
}
