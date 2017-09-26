package com.retrofit.mobile.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.retrofit.mobile.model.DataRegion;

import java.util.List;

/**
 * Created by Nursultan on 04.07.2017.
 */

public class RegionResponse {

    public List<DataRegion> getRegionList() {
        return regionList;
    }

    public void setRegionList(List<DataRegion> regionList) {
        this.regionList = regionList;
    }

    @SerializedName("data")
    @Expose
    private List<DataRegion> regionList = null;
}
