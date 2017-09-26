package com.retrofit.mobile.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by BAGDAT on 10.11.2016.
 */

public class InfoClient extends RealmObject implements Parcelable {

    @PrimaryKey
    @SerializedName("id")
    private String id;
    @SerializedName("phone")
    private String phone;
    @SerializedName("dop_phone")
    private String dopPhone;
    @SerializedName("email")
    private String email;
    @SerializedName("name")
    private String name;
    @SerializedName("city")
    private String city;
    @SerializedName("address")
    private String address;
    @SerializedName("user_type")
    private String userType;
    @SerializedName("avatar")
    private String avatar;

    public InfoClient() {
    }

    public InfoClient(@NonNull String id, @NonNull String phone, @NonNull String dopPhone,
                      @NonNull String email, @NonNull String name, @NonNull String city,
                      @NonNull String address, @NonNull String userType, @NonNull String avatar) {
        this.id = id;
        this.phone = phone;
        this.dopPhone = dopPhone;
        this.email = email;
        this.name = name;
        this.city = city;
        this.address = address;
        this.userType = userType;
        this.avatar = avatar;
    }

    public InfoClient(Parcel in) {
        id = in.readString();
        phone = in.readString();
        dopPhone = in.readString();
        email = in.readString();
        name = in.readString();
        city = in.readString();
        address = in.readString();
        userType = in.readString();
        avatar = in.readString();
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }
    @NonNull
    public String getPhone() {
        return phone;
    }

    public void setPhone(@NonNull String phone) {
        this.phone = phone;
    }

    @NonNull
    public String getDopPhone() {
        return dopPhone;
    }

    public void setDopPhone(@NonNull String dopPhone) {
        this.dopPhone = dopPhone;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getCity() {
        return city;
    }

    public void setCity(@NonNull String city) {
        this.city = city;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    public void setAddress(@NonNull String address) {
        this.address = address;
    }

    @NonNull
    public String getUserType() {
        return userType;
    }

    public void setUserType(@NonNull String userType) {
        this.userType = userType;
    }

    @NonNull
    public String getAvatar() {
        return "http://89.219.32.43/" + avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public static final Creator<InfoClient> CREATOR = new Creator<InfoClient>() {

        @NonNull
        @Override
        public InfoClient createFromParcel(Parcel in) {
            return new InfoClient(in);
        }

        @NonNull
        @Override
        public InfoClient[] newArray(int size) {
            return new InfoClient[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(phone);
        dest.writeString(dopPhone);
        dest.writeString(email);
        dest.writeString(name);
        dest.writeString(city);
        dest.writeString(address);
        dest.writeString(userType);
        dest.writeString(avatar);
    }
}

