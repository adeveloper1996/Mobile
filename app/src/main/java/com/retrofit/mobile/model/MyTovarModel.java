package com.retrofit.mobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nursultan on 12.09.2017.
 */

public class MyTovarModel {
    @SerializedName("tvm_id")
    @Expose
    private String tvmId;
    @SerializedName("tovar_id")
    @Expose
    private String tovarId;
    @SerializedName("model_id")
    @Expose
    private String modelId;
    @SerializedName("modelname")
    @Expose
    private String modelname;

    public String getTvmId() {
        return tvmId;
    }

    public void setTvmId(String tvmId) {
        this.tvmId = tvmId;
    }

    public String getTovarId() {
        return tovarId;
    }

    public void setTovarId(String tovarId) {
        this.tovarId = tovarId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelname() {
        return modelname;
    }

    public void setModelname(String modelname) {
        this.modelname = modelname;
    }
}
