package com.retrofit.mobile.response;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.retrofit.mobile.model.DataAuth;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nursultan on 03.07.2017.
 */

public class AuthResponse {
    @SerializedName("data")
    @Expose
    private List<DataAuth> data = new ArrayList<DataAuth>();

    @NonNull
    public List<DataAuth> getData() {
        return data;
    }

    public void setData(@NonNull List<DataAuth> data) {
        this.data = data;
    }
}
