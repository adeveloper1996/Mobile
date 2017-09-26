package com.retrofit.mobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nursultan on 11.09.2017.
 */

public class DataUserAnketa {
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("object")
    @Expose
    private List<InfoUserAnketa> userAnketaList = null;
    @SerializedName("errors")
    @Expose
    private String errors;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<InfoUserAnketa> getUserAnketaList() {
        return userAnketaList;
    }

    public void setUserAnketaList(List<InfoUserAnketa> userAnketaList) {
        this.userAnketaList = userAnketaList;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }
}
