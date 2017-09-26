package com.retrofit.mobile.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.retrofit.mobile.R;
import com.retrofit.mobile.adapter.CustomSpinnerAdapter;
import com.retrofit.mobile.adapter.SpinnerCityAdapter;
import com.retrofit.mobile.model.DataCity;
import com.retrofit.mobile.model.DataMakeOrder;
import com.retrofit.mobile.model.InfoCity;
import com.retrofit.mobile.model.InfoRegion;
import com.retrofit.mobile.model.MakeOrder;
import com.retrofit.mobile.response.CityResponse;
import com.retrofit.mobile.response.MakeOrderResponse;
import com.retrofit.mobile.rest.ApiClient;
import com.retrofit.mobile.rest.ApiInterface;
import com.retrofit.mobile.utils.TimeDialog;
import com.retrofit.mobile.utils.dialog.LoadingDialog;
import com.retrofit.mobile.utils.dialog.LoadingView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderTimeActivity extends AppCompatActivity {
    private Button btnSetTimeAuction;
    private Button btnSend;
    private TextView txtHour;
    private TextView txtMin;
    private TextView txtSec;
    private TextView txtNameDevice;
    private TextView txtStatusState;
    private Spinner spinnerRegion;
    private Spinner spinnerCity;
    private Calendar now;
    private LoadingView loadingView;
    private List<InfoCity> cities = new ArrayList<>();
    private String idCity;
    private long seconds = 0;
    private TimeDialog timeDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_time);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Заявка");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        btnSetTimeAuction = (Button) findViewById(R.id.btnSetTime);
        btnSend = (Button) findViewById(R.id.button_send);
        spinnerCity = (Spinner) findViewById(R.id.spinner);
        spinnerRegion = (Spinner) findViewById(R.id.spinner_reg);
        txtHour = (TextView) findViewById(R.id.txtHour);
        txtMin = (TextView) findViewById(R.id.txtMin);
        txtSec = (TextView) findViewById(R.id.txtSec);
        txtNameDevice = (TextView) findViewById(R.id.txtDiviceName);
        txtNameDevice.setText(MakeOrder.getNameDevice());
        txtStatusState = (TextView) findViewById(R.id.txtStatusState);
        txtStatusState.setText(MakeOrder.getNewOld());
        now = Calendar.getInstance();

        spinnerRegion();
        btnClickSetTime();
        btnSendOrder();
    }

    private void spinnerRegion() {
        List<String> region = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<InfoRegion> regions = realm.where(InfoRegion.class).findAll();
        for (int i = 0; i < regions.size(); i++) {
            region.add(regions.get(i).getName());
        }
        region.add("Регион");
        for (int i = 0; i < region.size(); i++) {
            Log.d("dddd",region.get(i));
        }
        String defaultText = "Регион";
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this,android.R.layout.simple_spinner_dropdown_item,region,defaultText,2);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRegion.setAdapter(adapter);
        spinnerRegion.setSelection(adapter.getCount());
        spinnerRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinnerRegion.getSelectedItemPosition() == 0){
                    Log.i("ssss","0");
                }

                for (int i = 0; i < regions.size(); i++) {
                    if(spinnerRegion.getSelectedItem().equals(regions.get(i).getName())) {
                        Log.i("ssss", "new" + regions.get(i).getId());
                        initApiCity(regions.get(i).getId());
                    }
                }
            }
        });
    }

    private void btnSendOrder() {
        btnSend.setOnClickListener(v -> {
//            timeSecond();
            if (timeSecond() == 0){
                Toast.makeText(OrderTimeActivity.this,"Установите время",Toast.LENGTH_SHORT).show();
            }else {
                String catId = String.valueOf(MakeOrder.getSubcategory());
                Log.i("ssss", "catId" + MakeOrder.getCatId());
                Log.i("ssss", "catId" + MakeOrder.getSubcategory());
                String markId = MakeOrder.getMarkId();
                String modelId = MakeOrder.getModelId();
                Log.i("ssss", "status" + MakeOrder.getStatusStade());
                String isNew = MakeOrder.getStatusStade();
                String cityId = idCity;
                String endTime = String.valueOf(timeSecond());
                Log.i("ssss", "endTime" + endTime);
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<MakeOrderResponse> call = apiInterface.makeOrder(catId, modelId, markId, cityId, isNew, endTime);
                call.enqueue(new Callback<MakeOrderResponse>() {
                    @Override
                    public void onResponse(Call<MakeOrderResponse> call, Response<MakeOrderResponse> response) {
                        MakeOrderResponse makeOrderResponse = response.body();
                        DataMakeOrder dataMakeOrder = makeOrderResponse.getDataMakeOrders().get(0);
                        boolean succes = dataMakeOrder.isSuccess();
                        String error = dataMakeOrder.getErrors();
                        Log.i("ssss", "" + succes);
                        if (succes) {
                            Toast.makeText(OrderTimeActivity.this, "Ваш заказ успешно добавлен", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(OrderTimeActivity.this, MarketMobileActivity.class));
                        } else {
                            Toast.makeText(OrderTimeActivity.this, "error" + error, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MakeOrderResponse> call, Throwable t) {
                        Toast.makeText(OrderTimeActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void btnClickSetTime() {

        btnSetTimeAuction.setOnClickListener(v -> {
            timeDialog = new TimeDialog();
            Bundle bundle = new Bundle();
            bundle.putInt("timeChange",0);
            timeDialog.setArguments(bundle);
            timeDialog.show(getSupportFragmentManager(),"timeSet");
        });
    }

    private long timeSecond(){
        String hou = txtHour.getText().toString();
        String str1 = hou.replaceAll( ":", "" );

        String minn = txtMin.getText().toString();
        String str2 = minn.replaceAll( ":", "" );

        String secc = txtSec.getText().toString();
        String str3 = secc.replaceAll( ":", "" );

        int hour = Integer.parseInt(str1);
        int min = Integer.parseInt(str2);
        int sec = Integer.parseInt(str3);

        Log.i("ssss",""+hour);
        Log.i("ssss",""+min);
        Log.i("ssss",""+sec);

        long allsecond = (hour*86400) + (min*3600) + (sec*60);
        return allsecond;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                Intent intent = new Intent(this,OrderMarkActivity.class);
                intent.putExtra("mobile", MakeOrder.getMark());
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initApiCity(String oblid) {
        loadingView = LoadingDialog.view(getFragmentManager());
        loadingView.showLoading();
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<CityResponse> call = apiInterface.getCity(oblid);
        call.enqueue(new Callback<CityResponse>() {
            @Override
            public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {
                CityResponse cityResponse = response.body();
                DataCity dataCity = cityResponse.getDataCities().get(0);
                boolean success = dataCity.isSuccess();
                if(success){
                    cities = dataCity.getInfoCities();
                    spinnerCity();
                    spinnerCity.setVisibility(View.VISIBLE);
                }
                loadingView.hideLoading();

            }

            @Override
            public void onFailure(Call<CityResponse> call, Throwable t) {
                Toast.makeText(OrderTimeActivity.this,t.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void spinnerCity() {
        SpinnerCityAdapter adapter = new SpinnerCityAdapter(this,android.R.layout.simple_spinner_dropdown_item,cities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(adapter);
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idCity = cities.get(position).getId();
                Log.i("ssss","cityId"+idCity);
            }
        });
    }

}
