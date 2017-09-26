package com.retrofit.mobile.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.retrofit.mobile.model.DataMakeOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nursultan on 12.07.2017.
 */

public class MakeOrderResponse {
    @SerializedName("data")
    @Expose
    private List<DataMakeOrder> dataMakeOrders = new ArrayList<DataMakeOrder>();

    public List<DataMakeOrder> getDataMakeOrders() {
        return dataMakeOrders;
    }

    public void setDataMakeOrders(List<DataMakeOrder> dataMakeOrders) {
        this.dataMakeOrders = dataMakeOrders;
    }
}
