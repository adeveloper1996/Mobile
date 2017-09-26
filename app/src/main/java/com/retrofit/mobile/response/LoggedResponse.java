package com.retrofit.mobile.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.retrofit.mobile.model.DataLogged;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nursultan on 04.07.2017.
 */

public class LoggedResponse {

    @SerializedName("data")
    @Expose
    private List<DataLogged> dataLoggeds = new ArrayList<DataLogged>();

    public List<DataLogged> getDataLoggeds() {
        return dataLoggeds;
    }

    public void setDataLoggeds(List<DataLogged> dataLoggeds) {
        this.dataLoggeds = dataLoggeds;
    }
}
