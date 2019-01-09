package com.capsulestudio.dailynote.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Juman on 1/20/2018.
 */

public class TextNoteModel implements Parcelable{
    private int id;
    private String title;
    private String details;
    private String date_time;
    private String email;

    public TextNoteModel() {
    }

    public TextNoteModel(String title, String details, String date_time, String email) {
        this.title = title;
        this.details = details;
        this.date_time = date_time;
        this.email = email;
    }

    public TextNoteModel(int id, String title, String details, String date_time, String email) {
        this.id = id;
        this.title = title;
        this.details = details;
        this.date_time = date_time;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(details);
        parcel.writeString(date_time);
        parcel.writeString(email);
    }

    protected TextNoteModel(Parcel in) {
        id = in.readInt();
        title = in.readString();
        details = in.readString();
        date_time = in.readString();
        email = in.readString();
    }

    public static final Creator<TextNoteModel> CREATOR = new Creator<TextNoteModel>() {
        @Override
        public TextNoteModel createFromParcel(Parcel in) {
            return new TextNoteModel(in);
        }

        @Override
        public TextNoteModel[] newArray(int size) {
            return new TextNoteModel[size];
        }
    };


}
