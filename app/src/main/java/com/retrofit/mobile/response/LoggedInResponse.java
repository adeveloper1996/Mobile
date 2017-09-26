package com.retrofit.mobile.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.retrofit.mobile.model.DataLoggedIn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nursultan on 03.07.2017.
 */

public class LoggedInResponse {
    @SerializedName("data")
    @Expose
    private List<DataLoggedIn> dataLoggedIns = new ArrayList<DataLoggedIn>();

    public List<DataLoggedIn> getDataLoggedIns() {
        return dataLoggedIns;
    }

    public void setDataLoggedIns(List<DataLoggedIn> dataLoggedIns) {
        this.dataLoggedIns = dataLoggedIns;
    }
}
