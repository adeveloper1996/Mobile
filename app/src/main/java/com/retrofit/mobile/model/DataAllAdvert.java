package com.retrofit.mobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nursultan on 15.07.2017.
 */

public class DataAllAdvert {
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("object")
    @Expose
    private List<InfoAllAdvert> allAdverts;
    @SerializedName("errors")
    @Expose
    private List<String> errors;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<InfoAllAdvert> getAllAdverts() {
        return allAdverts;
    }

    public void setAllAdverts(List<InfoAllAdvert> allAdverts) {
        this.allAdverts = allAdverts;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
