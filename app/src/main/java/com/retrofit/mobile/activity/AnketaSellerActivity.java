package com.retrofit.mobile.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.retrofit.mobile.R;
import com.retrofit.mobile.model.DataUserAnketa;
import com.retrofit.mobile.model.InfoUserAnketa;
import com.retrofit.mobile.response.UserAnketaResponse;
import com.retrofit.mobile.rest.ApiClient;
import com.retrofit.mobile.rest.ApiInterface;
import com.retrofit.mobile.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnketaSellerActivity extends AppCompatActivity {
    private TextView phone1;
    private TextView phone2;
    private TextView email;
    private TextView city;
    private TextView address;
    private TextView name;
    private ImageView imgAva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anketa_seller);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        String idUser = getIntent().getStringExtra("idSeller");
        initWidget();

        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<UserAnketaResponse> call = apiInterface.showAnketa(idUser);
        call.enqueue(new Callback<UserAnketaResponse>() {
            @Override
            public void onResponse(Call<UserAnketaResponse> call, Response<UserAnketaResponse> response) {
                UserAnketaResponse userAnketaResponse = response.body();
                DataUserAnketa dataUserAnketa = userAnketaResponse.getDataUserAnketas().get(0);
                InfoUserAnketa infoUserAnketa = dataUserAnketa.getUserAnketaList().get(0);
                boolean success = dataUserAnketa.isSuccess();

                if(success) {
                    phone1.setText(infoUserAnketa.getPhone());
                    phone2.setText(infoUserAnketa.getDopPhone());
                    if(infoUserAnketa.getEmail().length() == 0){
                        email.setText("Не указан");
                    }else {
                        email.setText(infoUserAnketa.getEmail());
                    }
                    if(infoUserAnketa.getCity().length() == 0){
                        city.setText("Не указан");
                    }else  {
                        city.setText(infoUserAnketa.getCity());
                    }
                    if(infoUserAnketa.getAddress().length() == 0) {
                        address.setText("Не указан");
                    }else {
                        address.setText(infoUserAnketa.getAddress());
                    }
                    name.setText(infoUserAnketa.getName());
                    if(infoUserAnketa.getAvatar().length() == 0) {
                        Picasso.with(getApplicationContext()).load(R.drawable.ic_person_white_24dp).transform(new CircleTransform()).into(imgAva);
                    }else {
                        Picasso.with(getApplicationContext()).load(infoUserAnketa.getAvatar()).transform(new CircleTransform()).into(imgAva);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserAnketaResponse> call, Throwable t) {
                Toast.makeText(AnketaSellerActivity.this,t.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initWidget() {
        phone1 = (TextView)findViewById(R.id.txt_anketa_seller_phone1);
        phone2 = (TextView)findViewById(R.id.txt_anketa_seller_phone2);
        email = (TextView)findViewById(R.id.txt_anketa_seller_email);
        city = (TextView)findViewById(R.id.txt_anketa_seller_city);
        address = (TextView)findViewById(R.id.txt_anketa_seller_address);
        name = (TextView)findViewById(R.id.txt_anketa_seller_name);
        imgAva = (ImageView)findViewById(R.id.img_anketa_seller_personna);
    }
}
