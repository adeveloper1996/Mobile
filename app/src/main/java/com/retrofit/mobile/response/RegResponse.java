package com.retrofit.mobile.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.retrofit.mobile.model.DataRegistration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nursultan on 04.07.2017.
 */

public class RegResponse {

    @SerializedName("data")
    @Expose
    private List<DataRegistration> data = new ArrayList<DataRegistration>();

    public List<DataRegistration> getData() {
        return data;
    }

    public void setData(List<DataRegistration> data) {
        this.data = data;
    }
}
