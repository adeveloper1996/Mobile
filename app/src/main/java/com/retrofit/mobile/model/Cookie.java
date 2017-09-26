package com.retrofit.mobile.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Cookie extends RealmObject{

    @PrimaryKey
    private String cookie;

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}
