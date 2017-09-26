package com.retrofit.mobile.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.retrofit.mobile.model.DataAddAdvertFavorite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nursultan on 06.09.2017.
 */

public class AddAdvFavoriteResponse {

    @SerializedName("data")
    @Expose
    List<DataAddAdvertFavorite> advertFavorites = new ArrayList<DataAddAdvertFavorite>();

    public List<DataAddAdvertFavorite> getAdvertFavorites() {
        return advertFavorites;
    }

    public void setAdvertFavorites(List<DataAddAdvertFavorite> advertFavorites) {
        this.advertFavorites = advertFavorites;
    }
}
