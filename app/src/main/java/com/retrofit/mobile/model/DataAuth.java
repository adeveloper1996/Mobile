package com.retrofit.mobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nursultan on 03.07.2017.
 */

public class DataAuth {
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("object")
    @Expose
    private InfoClient infoClients;
    @SerializedName("errors")
    @Expose
    private List<String> errors;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public InfoClient getInfoClients() {
        return infoClients;
    }

    public void setInfoClients(InfoClient infoClients) {
        this.infoClients = infoClients;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
