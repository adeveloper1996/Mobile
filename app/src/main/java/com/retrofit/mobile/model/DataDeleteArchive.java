package com.retrofit.mobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nursultan on 13.09.2017.
 */

public class DataDeleteArchive {
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("object")
    @Expose
    private List<String> strings;
    @SerializedName("errors")
    @Expose
    private List<String> errors;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<String> getStrings() {
        return strings;
    }

    public void setStrings(List<String> strings) {
        this.strings = strings;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
