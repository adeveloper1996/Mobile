package com.retrofit.mobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Nursultan on 13.09.2017.
 */

public class BuyerRequestBids extends RealmObject {

    @SerializedName("price")
    @Expose
    private String price;
    @PrimaryKey
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("created")
    @Expose
    private String created;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}