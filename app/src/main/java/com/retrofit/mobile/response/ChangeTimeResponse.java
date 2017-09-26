package com.retrofit.mobile.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.retrofit.mobile.model.DataChangeTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nursultan on 10.09.2017.
 */

public class ChangeTimeResponse {
    @SerializedName("data")
    @Expose
    private List<DataChangeTime> dataChangeTimes = new ArrayList<DataChangeTime>();

    public List<DataChangeTime> getDataChangeTimes() {
        return dataChangeTimes;
    }

    public void setDataChangeTimes(List<DataChangeTime> dataChangeTimes) {
        this.dataChangeTimes = dataChangeTimes;
    }
}
