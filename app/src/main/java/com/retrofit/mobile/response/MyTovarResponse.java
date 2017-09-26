package com.retrofit.mobile.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.retrofit.mobile.model.DataMyTovar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nursultan on 12.09.2017.
 */

public class MyTovarResponse {

    @SerializedName("data")
    @Expose
    List<DataMyTovar> myTovars = new ArrayList<>();

    public List<DataMyTovar> getMyTovars() {
        return myTovars;
    }

    public void setMyTovars(List<DataMyTovar> myTovars) {
        this.myTovars = myTovars;
    }
}
