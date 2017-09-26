package com.retrofit.mobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nursultan on 12.09.2017.
 */

public class DataMyTovar {
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("object")
    @Expose
    private List<InfoMyTovar> infoMyTovars = new ArrayList<>();
    @SerializedName("errors")
    @Expose
    private String errors;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<InfoMyTovar> getInfoMyTovars() {
        return infoMyTovars;
    }

    public void setInfoMyTovars(List<InfoMyTovar> infoMyTovars) {
        this.infoMyTovars = infoMyTovars;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }
}
