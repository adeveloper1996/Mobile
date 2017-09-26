package com.retrofit.mobile.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.retrofit.mobile.model.DataAddAdvert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nursultan on 07.09.2017.
 */

public class AddAdvertResponse {

    @SerializedName("data")
    @Expose
    List<DataAddAdvert> adverts = new ArrayList<DataAddAdvert>();

    public List<DataAddAdvert> getAdverts() {
        return adverts;
    }

    public void setAdverts(List<DataAddAdvert> adverts) {
        this.adverts = adverts;
    }
}
