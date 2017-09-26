package com.retrofit.mobile.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.retrofit.mobile.model.DataAddSellerProduct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nursultan on 12.09.2017.
 */

public class AddSellerProductResponse {

    @SerializedName("data")
    @Expose
    private List<DataAddSellerProduct> sellerProducts = new ArrayList<DataAddSellerProduct>();

    public List<DataAddSellerProduct> getSellerProducts() {
        return sellerProducts;
    }

    public void setSellerProducts(List<DataAddSellerProduct> sellerProducts) {
        this.sellerProducts = sellerProducts;
    }
}
