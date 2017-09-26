package com.retrofit.mobile.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Nursultan on 06.07.2017.
 */

public class InfoCategory extends RealmObject implements Parcelable {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("parent")
    @Expose
    private String parent;

    public InfoCategory() {

    }

    public InfoCategory(String id, String name, String parent) {
        this.id = id;
        this.name = name;
        this.parent = parent;
    }

    public InfoCategory(Parcel in) {
        id = in.readString();
        name = in.readString();
        parent = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public static final Creator<InfoCategory> CREATOR = new Creator<InfoCategory>() {
        @Override
        public InfoCategory createFromParcel(Parcel in) {
            return new InfoCategory(in);
        }

        @Override
        public InfoCategory[] newArray(int size) {
            return new InfoCategory[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(parent);
    }
}
