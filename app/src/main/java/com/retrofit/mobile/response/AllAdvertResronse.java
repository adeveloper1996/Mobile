package com.retrofit.mobile.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.retrofit.mobile.model.DataAllAdvert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nursultan on 15.07.2017.
 */

public class AllAdvertResronse {
    @SerializedName("data")
    @Expose
    List<DataAllAdvert> allAdvertList = new ArrayList<DataAllAdvert>();

    public List<DataAllAdvert> getAllAdvertList() {
        return allAdvertList;
    }

    public void setAllAdvertList(List<DataAllAdvert> allAdvertList) {
        this.allAdvertList = allAdvertList;
    }

}
