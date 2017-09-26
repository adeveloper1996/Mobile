package com.retrofit.mobile.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.retrofit.mobile.model.DataDeleteAdvertFavorite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nursultan on 06.09.2017.
 */

public class DeleteAdvFavoriteResponse {
    @SerializedName("data")
    @Expose
    List<DataDeleteAdvertFavorite> advertFavorites = new ArrayList<DataDeleteAdvertFavorite>();

    public List<DataDeleteAdvertFavorite> getAdvertFavorites() {
        return advertFavorites;
    }

    public void setAdvertFavorites(List<DataDeleteAdvertFavorite> advertFavorites) {
        this.advertFavorites = advertFavorites;
    }
}
