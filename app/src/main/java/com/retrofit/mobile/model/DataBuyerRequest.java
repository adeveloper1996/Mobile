package com.retrofit.mobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nursultan on 13.09.2017.
 */

public class DataBuyerRequest {
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("object")
    @Expose
    private List<InfoBuyerRequest> infoBuyerRequests;
    @SerializedName("errors")
    @Expose
    private String errors;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<InfoBuyerRequest> getInfoBuyerRequests() {
        return infoBuyerRequests;
    }

    public void setInfoBuyerRequests(List<InfoBuyerRequest> infoBuyerRequests) {
        this.infoBuyerRequests = infoBuyerRequests;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }
}
