package com.retrofit.mobile.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.retrofit.mobile.model.DataMark;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nursultan on 10.07.2017.
 */

public class MarkResponse {

    @SerializedName("data")
    @Expose
    private List<DataMark> dataMarks = new ArrayList<DataMark>();

    public List<DataMark> getDataMarks() {
        return dataMarks;
    }

    public void setDataMarks(List<DataMark> dataMarks) {
        this.dataMarks = dataMarks;
    }
}
