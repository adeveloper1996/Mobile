package com.retrofit.mobile.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.retrofit.mobile.model.DataCity;

import java.util.ArrayList;
import java.util.List;


public class CityResponse {
    @SerializedName("data")
    @Expose
    private List<DataCity> dataCities = new ArrayList<DataCity>();

    public List<DataCity> getDataCities() {
        return dataCities;
    }

    public void setDataCities(List<DataCity> dataCities) {
        this.dataCities = dataCities;
    }
}
