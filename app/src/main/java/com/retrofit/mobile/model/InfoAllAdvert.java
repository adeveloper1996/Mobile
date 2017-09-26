package com.retrofit.mobile.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Nursultan on 15.07.2017.
 */

public class InfoAllAdvert extends RealmObject implements Parcelable {
    @PrimaryKey
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
    @SerializedName("fio")
    @Expose
    private String fio;
    @SerializedName("last_activity")
    @Expose
    private String lastActivity;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("is_favourite")
    @Expose
    private int favourite;
    @SerializedName("modelname")
    @Expose
    private String modelname;
    @SerializedName("markname")
    @Expose
    private String markname;
    @SerializedName("photos")
    @Expose
    private RealmList<PhotoAllAdvert> photos = null;

    public InfoAllAdvert() {

    }

    public InfoAllAdvert(Parcel in) {
        id = in.readString();
        catId = in.readString();
        isDogovornaja = in.readString();
        isObmen = in.readString();
        description = in.readString();
        email = in.readString();
        cityId = in.readString();
        address = in.readString();
        phone1 = in.readString();
        phone2 = in.readString();
        phone3 = in.readString();
        phone4 = in.readString();
        phone5 = in.readString();
        coords = in.readString();
        isNew = in.readString();
        markId = in.readString();
        modelId = in.readString();
        userId = in.readString();
        price = in.readString();
        created = in.readString();
        status = in.readString();
        favourite = in.readInt();
        modelname = in.readString();
        markname = in.readString();
        fio = in.readString();
        avatar = in.readString();
    }

    public static final Creator<InfoAllAdvert> CREATOR = new Creator<InfoAllAdvert>() {
        @Override
        public InfoAllAdvert createFromParcel(Parcel in) {
            return new InfoAllAdvert(in);
        }

        @Override
        public InfoAllAdvert[] newArray(int size) {
            return new InfoAllAdvert[size];
        }
    };

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

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
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

    public RealmList<PhotoAllAdvert> getPhotos() {
        return photos;
    }

    public void setPhotos(RealmList<PhotoAllAdvert> photos) {
        this.photos = photos;
    }

    public String getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(String lastActivity) {
        this.lastActivity = lastActivity;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getAvatar() {
        return "http://89.219.32.43/" + avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(catId);
        dest.writeString(isDogovornaja);
        dest.writeString(isObmen);
        dest.writeString(description);
        dest.writeString(email);
        dest.writeString(cityId);
        dest.writeString(address);
        dest.writeString(phone1);
        dest.writeString(phone2);
        dest.writeString(phone3);
        dest.writeString(phone4);
        dest.writeString(phone5);
        dest.writeString(coords);
        dest.writeString(isNew);
        dest.writeString(markId);
        dest.writeString(modelId);
        dest.writeString(userId);
        dest.writeString(price);
        dest.writeString(created);
        dest.writeString(status);
        dest.writeInt(favourite);
        dest.writeString(modelname);
        dest.writeString(markname);
        dest.writeString(fio);
        dest.writeString(avatar);
    }
}