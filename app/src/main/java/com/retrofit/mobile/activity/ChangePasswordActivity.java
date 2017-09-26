package com.retrofit.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.retrofit.mobile.R;
import com.retrofit.mobile.activity.main.MainActivity;
import com.retrofit.mobile.model.DataEdit;
import com.retrofit.mobile.model.DataLogged;
import com.retrofit.mobile.model.InfoClient;
import com.retrofit.mobile.response.EditResponse;
import com.retrofit.mobile.response.LoggedResponse;
import com.retrofit.mobile.rest.ApiClient;
import com.retrofit.mobile.rest.ApiInterface;
import com.retrofit.mobile.utils.dialog.LoadingDialog;
import com.retrofit.mobile.utils.dialog.LoadingView;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nursultan on 12.09.2017.
 */

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText eTxtNewPassword;
    private EditText eTxtNewPassword2;
    private Button createNewPassword;
    private LoadingView loadingView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_change_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Realm realm = Realm.getDefaultInstance();
        RealmResults<InfoClient> clients = realm.where(InfoClient.class).findAll();

        eTxtNewPassword = (EditText)findViewById(R.id.etxt_new_password);
        eTxtNewPassword2 = (EditText)findViewById(R.id.etxt_new_password_2);
        createNewPassword = (Button) findViewById(R.id.btn_create_new_password);

        createNewPassword.setOnClickListener(v -> {
            if(eTxtNewPassword.getText().toString().equals(eTxtNewPassword2.getText().toString()) && !eTxtNewPassword.getText().toString().isEmpty()){
                ApiInterface apiInterface = ApiClient.getApiInterface();
                Call<EditResponse> call = apiInterface.edit(clients.get(0).getDopPhone(),clients.get(0).getEmail(),clients.get(0).getName(),clients.get(0).getCity(),clients.get(0).getAddress(),eTxtNewPassword.toString());
                call.enqueue(new Callback<EditResponse>() {
                    @Override
                    public void onResponse(Call<EditResponse> call, Response<EditResponse> response) {
                        EditResponse editResponse = response.body();
                        DataEdit dataEdit = editResponse.getDataEdits().get(0);
                        boolean success = dataEdit.isSuccess();
                        if (success){
                            Log.i("ssss",eTxtNewPassword.getText().toString());
                            logout();
                        }
                    }

                    @Override
                    public void onFailure(Call<EditResponse> call, Throwable t) {
                        Toast.makeText(ChangePasswordActivity.this,t.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                Toast.makeText(this,"Пароли не совподают",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void logout() {
        loadingView = LoadingDialog.view(getFragmentManager());
        loadingView.showLoading();
        ApiInterface apiInterface1 = ApiClient.getClient().create(ApiInterface.class);
        Call<LoggedResponse> call1 = apiInterface1.logout();
        call1.enqueue(new Callback<LoggedResponse>() {
            @Override
            public void onResponse(Call<LoggedResponse> call, Response<LoggedResponse> response) {
                LoggedResponse loggedResponse = response.body();
                DataLogged dataLogged = loggedResponse.getDataLoggeds().get(0);
                boolean success = dataLogged.isSuccess();
                if(success){
                    finish();
                    startActivity(new Intent(ChangePasswordActivity.this,MainActivity.class));
                    loadingView.hideLoading();
                }
            }

            @Override
            public void onFailure(Call<LoggedResponse> call, Throwable t) {

            }
        });
    }
}
