package com.retrofit.mobile.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.retrofit.mobile.model.DataCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nursultan on 06.07.2017.
 */

public class CategoryResponse {
    @SerializedName("data")
    @Expose
    private List<DataCategory> dataCategories = new ArrayList<DataCategory>();

    public List<DataCategory> getDataCategories() {
        return dataCategories;
    }

    public void setDataCategories(List<DataCategory> dataCategories) {
        this.dataCategories = dataCategories;
    }
}
