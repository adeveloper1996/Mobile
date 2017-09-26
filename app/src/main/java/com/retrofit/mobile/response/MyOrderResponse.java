package com.retrofit.mobile.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.retrofit.mobile.model.DataMyOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nursultan on 09.09.2017.
 */

public class MyOrderResponse {
    @SerializedName("data")
    @Expose
    List<DataMyOrder> dataMyOrders = new ArrayList<DataMyOrder>();

    public List<DataMyOrder> getDataMyOrders() {
        return dataMyOrders;
    }

    public void setDataMyOrders(List<DataMyOrder> dataMyOrders) {
        this.dataMyOrders = dataMyOrders;
    }
}
