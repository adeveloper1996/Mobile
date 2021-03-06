package com.retrofit.mobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Nursultan on 13.09.2017.
 */

public class InfoBuyerRequest extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("mark_id")
    @Expose
    private String markId;
    @SerializedName("model_id")
    @Expose
    private String modelId;
    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("city_id")
    @Expose
    private String cityId;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("is_new")
    @Expose
    private String isNew;
    @SerializedName("endtime")
    @Expose
    private String endtime;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("tovar_id")
    @Expose
    private String tovarId;
    @SerializedName("markname")
    @Expose
    private String markname;
    @SerializedName("modelname")
    @Expose
    private String modelname;
    @SerializedName("bids")
    @Expose
    private RealmList<BuyerRequestBids> bids = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMarkId() {
        return markId;
    }

    public void setMarkId(String markId) {
        this.markId = markId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIsNew() {
        return isNew;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getTovarId() {
        return tovarId;
    }

    public void setTovarId(String tovarId) {
        this.tovarId = tovarId;
    }

    public String getMarkname() {
        return markname;
    }

    public void setMarkname(String markname) {
        this.markname = markname;
    }

    public String getModelname() {
        return modelname;
    }

    public void setModelname(String modelname) {
        this.modelname = modelname;
    }

    public RealmList<BuyerRequestBids> getBids() {
        return bids;
    }

    public void setBids(RealmList<BuyerRequestBids> bids) {
        this.bids = bids;
    }
}
