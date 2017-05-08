package com.example.lukas.zagrajmy.model;

import android.graphics.Bitmap;

public class User {

    public User(){
        id = -1;
    }

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

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

}
