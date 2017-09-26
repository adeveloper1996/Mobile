package com.retrofit.mobile.rest;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.retrofit.mobile.model.Cookie;

import java.io.IOException;
import java.util.HashSet;

import io.realm.Realm;
import io.realm.RealmObject;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    public static final String BASE_URL = "http://89.219.32.43/api.php/";
    private static Retrofit retrofit = null;
    private static volatile ApiInterface apiInterface;
    private static HashSet<String> cookies = new HashSet<>();
    private static String cookiestring = "";

    public static String getCookiestring() {
        return cookiestring;
    }

    public static void setCookiestring(String cookiestring) {
        ApiClient.cookiestring = cookiestring;
    }

    public ApiClient() {
    }


    @NonNull
    public static ApiInterface getApiInterface() {
        ApiInterface service = apiInterface;
        if (service == null) {
            synchronized (ApiClient.class) {
                service = apiInterface;
                if (service == null) {
                    service = apiInterface = getClient().create(ApiInterface.class);
                }
            }
        }
        return service;
    }

    public static Retrofit getClient() {

        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .setLenient()
                .create();

        OkHttpClient client = new OkHttpClient();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if(cookiestring != null){
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response response = chain.proceed(chain.request());

                    if (!response.headers("Set-Cookie").isEmpty()) {
                        for (String header : response.headers("Set-Cookie")) {
                            cookies.add(header);
                        }
                    }

                    return response;
                }
            });
        }

        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();


                for (String cookie : cookies) {
                    String[] parser = cookie.split(";");
                    cookiestring = "" + parser[0] + "; ";
                    Log.i("ssss","cookieLL :" + cookiestring);
                    Cookie cookie1 = new Cookie();
                    cookie1.setCookie(cookiestring);
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.delete(Cookie.class);
                    realm.insert(cookie1);
                    realm.commitTransaction();
                }
                builder.addHeader("Cookie", cookiestring);
                return chain.proceed(builder.build());
            }
        });
        client = builder.build();

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit;

    }
}
