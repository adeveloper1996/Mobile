package com.retrofit.mobile.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.retrofit.mobile.R;
import com.retrofit.mobile.activity.MarketMobileActivity;
import com.retrofit.mobile.activity.login_registr.LoginSignUp;
import com.retrofit.mobile.model.Cookie;
import com.retrofit.mobile.model.DataLoggedIn;
import com.retrofit.mobile.response.LoggedInResponse;
import com.retrofit.mobile.rest.ApiClient;
import com.retrofit.mobile.rest.ApiInterface;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_splash);

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Cookie> cookies = realm.where(Cookie.class).findAll();
        if(cookies.size() != 0){
            String cookieString = cookies.get(0).getCookie();
            Log.i("ssss" , "cookieee :" + cookieString);
            ApiClient.setCookiestring(cookieString);
        }

        loggedin();
    }

    private void loggedin() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<LoggedInResponse> call = apiInterface.loggedIn();
        call.enqueue(new Callback<LoggedInResponse>() {
            @Override
            public void onResponse(Call<LoggedInResponse> call, Response<LoggedInResponse> response) {
                LoggedInResponse loggedInResponse = response.body();
                DataLoggedIn dataLoggedIn = loggedInResponse.getDataLoggedIns().get(0);
                boolean success = dataLoggedIn.isSuccess();
                if(success) {
                    Intent intent = new Intent(MainActivity.this, MarketMobileActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(MainActivity.this, LoginSignUp.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
//                    Intent intent = new Intent(MainActivity.this, MarketMobileActivity.class);
//                    startActivity(intent);
//                    finish();
                }
            }

            @Override
            public void onFailure(Call<LoggedInResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.toString(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
