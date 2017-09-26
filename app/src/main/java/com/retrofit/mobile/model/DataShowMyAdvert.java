package com.retrofit.mobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nursultan on 08.09.2017.
 */

public class DataShowMyAdvert {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("object")
    @Expose
    private List<InfoShowMyAdvert> showMyAdverts;
    @SerializedName("errors")
    @Expose
    private List<String> errors;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<InfoShowMyAdvert> getShowMyAdverts() {
        return showMyAdverts;
    }

    public void setShowMyAdverts(List<InfoShowMyAdvert> showMyAdverts) {
        this.showMyAdverts = showMyAdverts;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

}
