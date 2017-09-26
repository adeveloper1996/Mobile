package com.retrofit.mobile.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.retrofit.mobile.model.DataSendSms;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nursultan on 04.07.2017.
 */

public class SendsmsResponse {

    @SerializedName("data")
    @Expose
    private List<DataSendSms> data = new ArrayList<>();

    public List<DataSendSms> getData() {
        return data;
    }
    public void setData(List<DataSendSms> data) {
        this.data = data;
    }
}
