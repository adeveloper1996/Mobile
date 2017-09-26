package com.retrofit.mobile.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.retrofit.mobile.model.DataSendPrice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nursultan on 13.09.2017.
 */

public class SendPriceResponse {
    @SerializedName("data")
    @Expose
    List<DataSendPrice> dataSendPriceList = new ArrayList<DataSendPrice>();

    public List<DataSendPrice> getDataSendPriceList() {
        return dataSendPriceList;
    }

    public void setDataSendPriceList(List<DataSendPrice> dataSendPriceList) {
        this.dataSendPriceList = dataSendPriceList;
    }
}
