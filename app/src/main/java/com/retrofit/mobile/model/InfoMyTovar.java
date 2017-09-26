package com.retrofit.mobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nursultan on 12.09.2017.
 */

public class InfoMyTovar {
    @SerializedName("markname")
    @Expose
    private String markname;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("mark_id")
    @Expose
    private String markId;
    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("city_id")
    @Expose
    private String cityId;
    @SerializedName("is_new")
    @Expose
    private String isNew;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("models")
    @Expose
    private List<MyTovarModel> models = null;

    public String getMarkname() {
        return markname;
    }

    public void setMarkname(String markname) {
        this.markname = markname;
    }

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

    public String getIsNew() {
        return isNew;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public List<MyTovarModel> getModels() {
        return models;
    }

    public void setModels(List<MyTovarModel> models) {
        this.models = models;
    }
}
