package com.retrofit.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.retrofit.mobile.R;
import com.retrofit.mobile.adapter.CustomSpinnerAdapter;
import com.retrofit.mobile.adapter.SellerProductListAdapter;
import com.retrofit.mobile.adapter.SpinnerCityAdapter;
import com.retrofit.mobile.model.DataAddSellerProduct;
import com.retrofit.mobile.model.DataCity;
import com.retrofit.mobile.model.InfoCity;
import com.retrofit.mobile.model.InfoModel;
import com.retrofit.mobile.model.InfoRegion;
import com.retrofit.mobile.model.MakeOrder;
import com.retrofit.mobile.response.AddSellerProductResponse;
import com.retrofit.mobile.response.CityResponse;
import com.retrofit.mobile.rest.ApiClient;
import com.retrofit.mobile.rest.ApiInterface;
import com.retrofit.mobile.utils.dialog.LoadingDialog;
import com.retrofit.mobile.utils.dialog.LoadingView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderSelllerProductActivity extends AppCompatActivity {
    private TextView txtProductState;
    private RecyclerView rvProduct;
    private Button btnSend;
    private Spinner spinnerRegion;
    private Spinner spinnerCity;
    private String cityName;
    private List<InfoModel> selectedProducts = new ArrayList<InfoModel>();
    private LoadingView loadingView;
    private List<InfoCity> cities = new ArrayList<>();
    private String idCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_selller_product);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Регистрация товара");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        txtProductState = (TextView)findViewById(R.id.txt_seller_status_state);
        txtProductState.setText(MakeOrder.getNewOld());
        rvProduct = (RecyclerView)findViewById(R.id.recycler_product_list);
        btnSend = (Button)findViewById(R.id.button_send_seller);
        spinnerCity = (Spinner)findViewById(R.id.spinner_city);
        spinnerRegion = (Spinner)findViewById(R.id.spinner_region);

        initProductList();
        spinnerRegion();
        clickButton();

    }

    private void initProductList() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<InfoModel> infoModels = realm.where(InfoModel.class).findAll();
        for(int i = 0; i < infoModels.size(); i++) {
            InfoModel model = infoModels.get(i);
            if(model.isSelected() == true) {
                selectedProducts.add(model);
            }
        }

        rvProduct.setLayoutManager(new LinearLayoutManager(this));
        rvProduct.setAdapter(new SellerProductListAdapter(selectedProducts, R.layout.recycler_seller_product_item,this));
    }

    private void spinnerRegion() {
        List<String> region = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<InfoRegion> regions = realm.where(InfoRegion.class).findAll();
        for (int i = 0; i < regions.size(); i++) {
            region.add(regions.get(i).getName());
        }
        region.add("Регион");
        for(int i = 0; i < region.size(); i++) {
            Log.d("dddd", region.get(i));
        }
        String defaultText = "Регион";
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, region, defaultText,2);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRegion.setAdapter(adapter);
        spinnerRegion.setSelection(adapter.getCount());
        spinnerRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinnerRegion.getSelectedItemPosition() == 0) {
                    Log.d("ssss", "0");
                }

                for(int i = 0; i < regions.size(); i++) {
                    if(spinnerRegion.getSelectedItem().equals(regions.get(i).getName())){
                        initApiCity(regions.get(i).getId());
                        Log.i("ssss", "new " + regions.get(i).getId());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initApiCity(String oblId) {
        loadingView = LoadingDialog.view(getFragmentManager());
        loadingView.showLoading();
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<CityResponse> call = apiInterface.getCity(oblId);
        call.enqueue(new Callback<CityResponse>() {
            @Override
            public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {
                CityResponse cityResponse = response.body();
                DataCity dataCity = cityResponse.getDataCities().get(0);
                boolean success = dataCity.isSuccess();
                if(success){
                    cities = dataCity.getInfoCities();
                    spinnerCity.setVisibility(View.VISIBLE);
                    spinnerCity();
                }
                loadingView.hideLoading();

            }

            @Override
            public void onFailure(Call<CityResponse> call, Throwable t) {
                Toast.makeText(OrderSelllerProductActivity.this,t.toString(),Toast.LENGTH_SHORT).show();
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

    private void clickButton() {
        btnSend.setOnClickListener(v -> {
            Map<String, String> data = new LinkedHashMap<String, String>();
            data.put("catId", String.valueOf(MakeOrder.getSubcategory()));
            Log.i("ssss", ""+selectedProducts.size());
            String pr = "";
            for (int i = 0; i < selectedProducts.size(); i++) {
                data.put("modelId[] " + pr, selectedProducts.get(i).getId());
                pr = pr + "";
                Log.i("modelId[] ", selectedProducts.get(i).getId());
            }
            data.put("markId", MakeOrder.getMarkId());
            data.put("cityId", idCity);
            data.put("isNew",MakeOrder.getStatusStade());
            for (int i = 0; i < data.size(); i++){
                Log.i("ssss", "" + data.get(i));
            }

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<AddSellerProductResponse> call = apiInterface.addProduct(data);
            call.enqueue(new Callback<AddSellerProductResponse>() {
                @Override
                public void onResponse(Call<AddSellerProductResponse> call, Response<AddSellerProductResponse> response) {
                    AddSellerProductResponse addSellerProductResponse = response.body();
                    DataAddSellerProduct dataAddSellerProduct = addSellerProductResponse.getSellerProducts().get(0);
                    boolean success = dataAddSellerProduct.isSuccess();

                    if(success) {
                        Toast.makeText(OrderSelllerProductActivity.this, "Ваш товар успешно добавлен!!!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(OrderSelllerProductActivity.this, MarketMobileActivity.class));
                    }else {
                        Toast.makeText(OrderSelllerProductActivity.this, "Error", Toast.LENGTH_SHORT);
                    }
                }

                @Override
                public void onFailure(Call<AddSellerProductResponse> call, Throwable t) {
                    Toast.makeText(OrderSelllerProductActivity.this, t.toString(), Toast.LENGTH_SHORT);
                }
            });

        });
    }
}
