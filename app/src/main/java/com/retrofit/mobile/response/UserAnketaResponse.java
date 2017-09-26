package com.retrofit.mobile.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.retrofit.mobile.model.DataUserAnketa;

import java.util.List;

/**
 * Created by Nursultan on 11.09.2017.
 */

public class UserAnketaResponse {
    @SerializedName("data")
    @Expose
    List<DataUserAnketa> dataUserAnketas;

    public List<DataUserAnketa> getDataUserAnketas() {
        return dataUserAnketas;
    }

    public void setDataUserAnketas(List<DataUserAnketa> dataUserAnketas) {
        this.dataUserAnketas = dataUserAnketas;
    }
}
