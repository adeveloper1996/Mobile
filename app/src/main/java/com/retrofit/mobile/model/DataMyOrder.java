package com.retrofit.mobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nursultan on 09.09.2017.
 */

public class DataMyOrder {
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("archived_count")
    @Expose
    private int archive_count;
    @SerializedName("object")
    @Expose
    private List<InfoMyOrder> infoMyOrders;
    @SerializedName("errors")
    @Expose
    private String errors;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getArchive_count() {
        return archive_count;
    }

    public void setArchive_count(int archive_count) {
        this.archive_count = archive_count;
    }

    public List<InfoMyOrder> getInfoMyOrders() {
        return infoMyOrders;
    }

    public void setInfoMyOrders(List<InfoMyOrder> infoMyOrders) {
        this.infoMyOrders = infoMyOrders;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }
}
