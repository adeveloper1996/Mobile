package com.retrofit.mobile.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.retrofit.mobile.model.DataEdit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nursultan on 06.07.2017.
 */

public class EditResponse {
    @SerializedName("data")
    @Expose
    private List<DataEdit> dataEdits = new ArrayList<DataEdit>();

    public List<DataEdit> getDataEdits() {
        return dataEdits;
    }

    public void setDataEdits(List<DataEdit> dataEdits) {
        this.dataEdits = dataEdits;
    }
}
