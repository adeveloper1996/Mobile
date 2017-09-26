package com.retrofit.mobile.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class PhotoAllAdvert extends RealmObject implements Parcelable {
    @PrimaryKey
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("url")
    @Expose
    private String url;

    public PhotoAllAdvert() {
    }

    protected PhotoAllAdvert(Parcel in) {
        id = in.readString();
        url = in.readString();
    }

    public static final Creator<PhotoAllAdvert> CREATOR = new Creator<PhotoAllAdvert>() {
        @Override
        public PhotoAllAdvert createFromParcel(Parcel in) {
            return new PhotoAllAdvert(in);
        }

        @Override
        public PhotoAllAdvert[] newArray(int size) {
            return new PhotoAllAdvert[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return "http://89.219.32.43/" + url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(url);
    }
}
