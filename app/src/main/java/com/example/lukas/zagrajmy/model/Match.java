package com.example.lukas.zagrajmy.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Match implements Parcelable {
    private int id;
    private LatLng latLng;
    private String title;
    private String description;
    private int authorId;
    private ArrayList<Integer> participantsIds;
    private Date date;
    private int limit;

    public Match(){

    }

    public Match(int id, LatLng latLng, String title){
        this.id = id;
        this.latLng = latLng;
        this.title = title;
    }

    public Match(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();


        this.authorId = in.readInt();
        this.participantsIds = (ArrayList<Integer>) in.readSerializable();
        this.date = new Date(in.readLong());
        this.limit = in.readInt();
        this.latLng = in.readParcelable(LatLng.class.getClassLoader());
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public void setParticipantsIds(ArrayList<Integer> participantsIds) {
        this.participantsIds = participantsIds;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(title);
        out.writeString(description);
        out.writeInt(authorId);
        out.writeSerializable(participantsIds);
        out.writeLong(date.getTime());
        out.writeInt(limit);
        out.writeParcelable(latLng, flags);
    }
    public static final Parcelable.Creator<Match> CREATOR
            = new Parcelable.Creator<Match>() {

        @Override
        public Match createFromParcel(Parcel in) {
            return new Match(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Match[] newArray(int size) {
            return new Match[size];
        }
    };
}