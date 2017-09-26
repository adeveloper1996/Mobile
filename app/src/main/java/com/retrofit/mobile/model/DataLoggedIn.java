package com.retrofit.mobile.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataLoggedIn {
    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("object")
    @Expose
    private String object;

    @SerializedName("errors")
    @Expose
    private String errors;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }
}
