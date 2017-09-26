package com.retrofit.mobile.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.retrofit.mobile.model.DataShowMyAdvert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nursultan on 08.09.2017.
 */

public class ShowMyAdvertResponse {
    @SerializedName("data")
    @Expose
    private List<DataShowMyAdvert> myAdverts = new ArrayList<DataShowMyAdvert>();

    public List<DataShowMyAdvert> getMyAdverts() {
        return myAdverts;
    }

    public void setMyAdverts(List<DataShowMyAdvert> myAdverts) {
        this.myAdverts = myAdverts;
    }
}
