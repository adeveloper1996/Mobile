package com.retrofit.mobile.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.retrofit.mobile.model.DataViewCount;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nursultan on 06.09.2017.
 */

public class ViewCountResponse {

    @SerializedName("data")
    @Expose
    private List<DataViewCount> viewCountList = new ArrayList<DataViewCount>();

    public List<DataViewCount> getViewCountList() {
        return viewCountList;
    }

    public void setViewCountList(List<DataViewCount> viewCountList) {
        this.viewCountList = viewCountList;
    }
}
