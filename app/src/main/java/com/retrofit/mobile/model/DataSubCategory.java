package com.retrofit.mobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nursultan on 10.07.2017.
 */

public class DataSubCategory {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("object")
    @Expose
    private List<InfoSubCategory> infoSubCategories;
    @SerializedName("errors")
    @Expose
    private String errors;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<InfoSubCategory> getInfoSubCategories() {
        return infoSubCategories;
    }

    public void setInfoSubCategories(List<InfoSubCategory> infoSubCategories) {
        this.infoSubCategories = infoSubCategories;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }
}
