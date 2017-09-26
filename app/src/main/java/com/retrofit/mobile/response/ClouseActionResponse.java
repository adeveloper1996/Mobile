package com.retrofit.mobile.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.retrofit.mobile.model.DataCloseAuction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nursultan on 09.09.2017.
 */

public class ClouseActionResponse {
    @SerializedName("data")
    @Expose
    private List<DataCloseAuction> dataCloseAuctions = new ArrayList<DataCloseAuction>();

    public List<DataCloseAuction> getDataCloseAuctions() {
        return dataCloseAuctions;
    }

    public void setDataCloseAuctions(List<DataCloseAuction> dataCloseAuctions) {
        this.dataCloseAuctions = dataCloseAuctions;
    }

}
