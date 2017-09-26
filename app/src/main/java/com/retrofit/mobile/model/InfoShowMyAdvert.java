package com.retrofit.mobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nursultan on 08.09.2017.
 */

public class InfoShowMyAdvert {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("is_dogovornaja")
    @Expose
    private String isDogovornaja;
    @SerializedName("is_obmen")
    @Expose
    private String isObmen;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("city_id")
    @Expose
    private String cityId;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("phone1")
    @Expose
    private String phone1;
    @SerializedName("phone2")
    @Expose
    private String phone2;
    @SerializedName("phone3")
    @Expose
    private String phone3;
    @SerializedName("phone4")
    @Expose
    private String phone4;
    @SerializedName("phone5")
    @Expose
    private String phone5;
    @SerializedName("coords")
    @Expose
    private String coords;
    @SerializedName("is_new")
    @Expose
    private String isNew;
    @SerializedName("mark_id")
    @Expose
    private String markId;
    @SerializedName("model_id")
    @Expose
    private String modelId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("modelname")
    @Expose
    private String modelname;
    @SerializedName("markname")
    @Expose
    private String markname;
    @SerializedName("tview_count")
    @Expose
    private String tviewCount;
    @SerializedName("view_count")
    @Expose
    private String viewCount;
    @SerializedName("photos")
    @Expose
    private List<ShowMyAdvertPhoto> photos = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getIsDogovornaja() {
        return isDogovornaja;
    }

    public void setIsDogovornaja(String isDogovornaja) {
        this.isDogovornaja = isDogovornaja;
    }

    public String getIsObmen() {
        return isObmen;
    }

    public void setIsObmen(String isObmen) {
        this.isObmen = isObmen;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getPhone3() {
        return phone3;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3;
    }

    public String getPhone4() {
        return phone4;
    }

    public void setPhone4(String phone4) {
        this.phone4 = phone4;
    }

    public String getPhone5() {
        return phone5;
    }

    public void setPhone5(String phone5) {
        this.phone5 = phone5;
    }

    public String getCoords() {
        return coords;
    }

    public void setCoords(String coords) {
        this.coords = coords;
    }

    public String getIsNew() {
        return isNew;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getModelname() {
        return modelname;
    }

    public void setModelname(String modelname) {
        this.modelname = modelname;
    }

    public String getMarkname() {
        return markname;
    }

    public void setMarkname(String markname) {
        this.markname = markname;
    }

    public String getTviewCount() {
        return tviewCount;
    }

    public void setTviewCount(String tviewCount) {
        this.tviewCount = tviewCount;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public List<ShowMyAdvertPhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<ShowMyAdvertPhoto> photos) {
        this.photos = photos;
    }
}
