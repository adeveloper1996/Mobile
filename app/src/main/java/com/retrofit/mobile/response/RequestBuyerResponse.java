package com.retrofit.mobile.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.retrofit.mobile.model.DataBuyerRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nursultan on 13.09.2017.
 */

public class RequestBuyerResponse {
    @SerializedName("data")
    @Expose
    List<DataBuyerRequest> buyerRequests = new ArrayList<DataBuyerRequest>();

    public List<DataBuyerRequest> getBuyerRequests() {
        return buyerRequests;
    }

    public void setBuyerRequests(List<DataBuyerRequest> buyerRequests) {
        this.buyerRequests = buyerRequests;
    }
}
