package com.retrofit.mobile.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.retrofit.mobile.model.DataDeleteArchive;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nursultan on 13.09.2017.
 */

public class DeleteArchiveResponse {
    @SerializedName("data")
    @Expose
    List<DataDeleteArchive> deleteArchives = new ArrayList<DataDeleteArchive>();

    public List<DataDeleteArchive> getDeleteArchives() {
        return deleteArchives;
    }

    public void setDeleteArchives(List<DataDeleteArchive> deleteArchives) {
        this.deleteArchives = deleteArchives;
    }
}
