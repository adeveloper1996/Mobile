package com.retrofit.mobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nursultan on 08.09.2017.
 */

public class ShowMyAdvertPhoto {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("url")
    @Expose
    private String url;

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
}
