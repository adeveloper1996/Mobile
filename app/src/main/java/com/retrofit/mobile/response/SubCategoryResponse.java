package com.retrofit.mobile.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.retrofit.mobile.model.DataSubCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nursultan on 10.07.2017.
 */

public class SubCategoryResponse {

    @SerializedName("data")
    @Expose
    private List<DataSubCategory> subCategories = new ArrayList<DataSubCategory>();

    public List<DataSubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<DataSubCategory> subCategories) {
        this.subCategories = subCategories;
    }
}
