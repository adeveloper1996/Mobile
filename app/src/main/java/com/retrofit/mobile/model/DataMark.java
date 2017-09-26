package com.retrofit.mobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nursultan on 10.07.2017.
 */

public class DataMark {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("object")
    @Expose
    private List<InfoMark> infoMarks;
    @SerializedName("errors")
    @Expose
    private String errors;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<InfoMark> getInfoMarks() {
        return infoMarks;
    }

    public void setInfoMarks(List<InfoMark> infoMarks) {
        this.infoMarks = infoMarks;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }
}
