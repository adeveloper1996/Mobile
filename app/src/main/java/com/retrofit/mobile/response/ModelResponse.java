package com.retrofit.mobile.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.retrofit.mobile.model.DataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nursultan on 10.07.2017.
 */

public class ModelResponse {
    @SerializedName("data")
    @Expose
    private List<DataModel> dataModels = new ArrayList<DataModel>();

    public List<DataModel> getDataModels() {
        return dataModels;
    }

    public void setDataModels(List<DataModel> dataModels) {
        this.dataModels = dataModels;
    }
}
