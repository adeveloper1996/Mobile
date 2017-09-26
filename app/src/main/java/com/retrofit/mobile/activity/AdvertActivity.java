package com.retrofit.mobile.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.retrofit.mobile.R;
import com.retrofit.mobile.adapter.CustomSpinnerAdapter;
import com.retrofit.mobile.adapter.SpinnerModelAdapter;
import com.retrofit.mobile.fragment.AdvertDialogDateFragment;
import com.retrofit.mobile.fragment.AdvertDialogTovarFragment;
import com.retrofit.mobile.fragment.AdvertGridFragment;
import com.retrofit.mobile.fragment.AdvertSingleFragment;
import com.retrofit.mobile.model.DataCity;
import com.retrofit.mobile.model.DataLoggedIn;
import com.retrofit.mobile.model.DataModel;
import com.retrofit.mobile.model.InfoCity;
import com.retrofit.mobile.model.InfoModel;
import com.retrofit.mobile.response.CityResponse;
import com.retrofit.mobile.response.LoggedInResponse;
import com.retrofit.mobile.response.ModelResponse;
import com.retrofit.mobile.rest.ApiClient;
import com.retrofit.mobile.rest.ApiInterface;
import com.retrofit.mobile.utils.dialog.LoadingDialog;
import com.retrofit.mobile.utils.dialog.LoadingView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdvertActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageButton imgView1;
    private ImageButton imgView2;
    private TextView txtFilterTovar;
    private TextView txtFilterDate;
    private TextView txtFilters;
    private Fragment fragment;
    private LinearLayout llfilter;
    private Button btnShow;
    private Button btnCancel;
    private Spinner spinCat;
    private Spinner spinMark;
    private Spinner spinModel;
    private EditText eTxtPrice;
    private EditText eTxtPrice1;
    private CheckBox rBtnObmen;
    private CheckBox rBtnPhoto;
    private LoadingView loadingView;
    private List<InfoModel> model = new ArrayList<>();
    private HashMap<String,String> fil = new HashMap<>();
    protected String categoryId;
    private String markid;
    protected String modelid;
    protected String obmen;
    protected String dogovor;
    private boolean isSearchOpened = false;
    private EditText edtSeach;
    private int typeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Объявление");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_advert);
        fab.setOnClickListener(v-> {
              loggedin();
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState != null) {
          typeFragment = 0;
            Log.d("sss", "notnull" + savedInstanceState);
            String time = savedInstanceState.getString("time");
            Log.d("sss",time);
            fragment = getFragmentManager().getFragment(savedInstanceState,"fragment");
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.advert_content_frame, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
        } else {
            typeFragment = 1;
            fragment = new AdvertGridFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.advert_content_frame, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
        }

        llfilter = (LinearLayout)findViewById(R.id.filter_ll);
        btnShow = (Button)findViewById(R.id.btn_advert_filter_show);
        btnCancel = (Button)findViewById(R.id.btn_advert_filter_cancel);
        btnShow.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        initimgButton();
        initFilter();
        initFilterWidget();
        spinnerCat();
        spinnerMark();
        spinnerModel();
        initApiCity();
    }

    private void spinnerModel() {
        SpinnerModelAdapter adapter = new SpinnerModelAdapter(this,android.R.layout.simple_spinner_dropdown_item,model);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinModel.setAdapter(adapter);
        spinModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("ssss",model.get(position).getId());
                modelid = model.get(position).getId();
                fil.put("model_id",modelid);
            }
        });
    }

    private void spinnerMark() {
        List<String> mark = new ArrayList<>();
        mark.add("Все");
        mark.add("Sumsung");
        mark.add("Apple");
        mark.add("Sony");
        mark.add("HTC");
        mark.add("Lenova");
        mark.add("Hiaomi");
        mark.add("Meizu");
        mark.add("Nokia");
        String defaultText = "Марка телефона";
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item,mark, defaultText ,2);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinMark.setAdapter(adapter);
        spinMark.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
             //   loadingView = LoadingDialog.view(getFragmentManager());
                int i = 0;
                if(spinMark.getSelectedItem().equals("Все")) {
                    spinModel.setVisibility(View.GONE);
                    fil.remove("mark_id");
                }
                if(spinMark.getSelectedItem().equals("Sumsung")){
                    loadingView.showLoading();
                    spinModel.setVisibility(View.VISIBLE);
                    modelNameApi("1");
                    markid = "1";
                    fil.put("mark_id",markid);
                }
                if(parent.getSelectedItem().equals("Apple")){
                    modelNameApi("2");
                    loadingView.showLoading();
                    spinModel.setVisibility(View.VISIBLE);
                    markid = "2";
                    fil.put("mark_id",markid);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void spinnerCat() {
        List<String> choose = new ArrayList<>();
        choose.add("Все");
        choose.add("Мобильные телефоны");
        choose.add("Планшеты");
        choose.add("Телефоны");
        String defaultText = "Рубрика";
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, choose, defaultText, 1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCat.setAdapter(adapter);
        spinCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinCat.getSelectedItemPosition() == 0){

                }
                if(parent.getSelectedItem().equals("Мобильные телефоны")){
                    categoryId = "1";
                    spinMark.setVisibility(View.VISIBLE);
                    spinnerMark();
                    fil.put("cat_id", categoryId);
                }
                if(parent.getSelectedItem().equals("Планшеты")){
                    categoryId = "2";
                    spinMark.setVisibility(View.VISIBLE);
                    spinnerMark();
                    fil.put("cat_id",categoryId);

                }
                if(parent.getSelectedItem().equals("Телефоны")){
                    categoryId = "3";
                    spinMark.setVisibility(View.VISIBLE);
                    spinnerMark();
                    fil.put("cat_id",categoryId);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initFilterWidget() {
        spinCat = (Spinner)findViewById(R.id.spinner_advert_filter_cat);
        spinMark = (Spinner)findViewById(R.id.spinner_advert_filter_mark);
        spinModel = (Spinner)findViewById(R.id.spinner_advert_filter_model);
        eTxtPrice = (EditText)findViewById(R.id.eTxt_advert_filter_price);
        eTxtPrice1 = (EditText)findViewById(R.id.eTxt_advert_filter_price1);
        rBtnObmen = (CheckBox) findViewById(R.id.rBtn_advert_filter_na_obmen);
        rBtnPhoto = (CheckBox) findViewById(R.id.rBtn_advert_filter_photo);
        rBtnObmen.setOnClickListener(this);
        rBtnPhoto.setOnClickListener(this);
    }

    private  void initFilter() {
        txtFilters = (TextView) findViewById(R.id.txt_filters);
        txtFilters.setOnClickListener(v -> {
            llfilter.setVisibility(View.VISIBLE);
        });

        txtFilterTovar = (TextView) findViewById(R.id.txt_filter_tovar);
        txtFilterTovar.setOnClickListener(v -> {
             new AdvertDialogTovarFragment().show(getFragmentManager(), "filtertovar");
        });
        txtFilterDate = (TextView)findViewById(R.id.txt_filter_date);
        txtFilterDate.setOnClickListener(v -> {
            new AdvertDialogDateFragment().show(getFragmentManager(),"filterdate");
        });

    }
    private void initimgButton() {
        imgView1 = (ImageButton) findViewById(R.id.img_view_1);
        imgView1.setOnClickListener(v -> {
            typeFragment = 0;
            fragment = new AdvertSingleFragment();
            FragmentTransaction ft1 = getFragmentManager().beginTransaction();
            ft1.replace(R.id.advert_content_frame,fragment);
            ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft1.commit();
        });

        imgView2 = (ImageButton) findViewById(R.id.img_view_2);
        imgView2.setOnClickListener(v -> {
            typeFragment = 1;
            fragment = new AdvertGridFragment();
            FragmentTransaction ft2 = getFragmentManager().beginTransaction();
            ft2.replace(R.id.advert_content_frame,fragment);
            ft2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft2.commit();
        });
    }

    @Override
    public void onClick(View v) {
         switch (v.getId()){
             case R.id.btn_advert_filter_show:
                 initFilterApi();
                 break;
             case  R.id.btn_advert_filter_cancel:
                 llfilter.setVisibility(View.GONE);
                 break;
             case R.id.rBtn_advert_filter_na_obmen:
                 if(rBtnObmen.isChecked()){
                     obmen = "1";
                     Log.i("ssss",obmen);
                 }else {
                     obmen = "0";
                     Log.i("ssss", obmen);
                 }
                 break;
             case R.id.rBtn_advert_filter_photo:
                 if(rBtnPhoto.isChecked()) {
                     dogovor = "1";
                     Log.i("ssss",dogovor);
                 }else {
                     dogovor = "0";
                     Log.i("ssss", dogovor);
                 }
                 break;
         }
    }
    private void initFilterApi() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("filter", fil);
        Log.i("ssss", "fil size "+fil.size());
        if(fragment != null) {
            getFragmentManager().beginTransaction().remove(fragment).commit();

            if(typeFragment == 1) {
                fragment = new AdvertGridFragment();
            }else {
                fragment = new AdvertSingleFragment();
            }
            FragmentTransaction ft =getFragmentManager().beginTransaction();
            Log.i("ssss", "frag " + fragment);
            fragment.setArguments(bundle);
            ft.replace(R.id.advert_content_frame, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
        }
        llfilter.setVisibility(View.GONE);
    }

    private void modelNameApi(String modelId) {
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<ModelResponse> call = apiInterface.getModel(modelId);
        call.enqueue(new Callback<ModelResponse>() {
            @Override
            public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                ModelResponse modelResponse = response.body();
                DataModel dataModel = modelResponse.getDataModels().get(0);
                boolean success = dataModel.isSuccess();
                if(success) {
                    model = dataModel.getInfoModels();
                    spinnerModel();
                    loadingView.hideLoading();
                }else {
                    startActivity(new Intent(AdvertActivity.this,MarketMobileActivity.class));
                }
            }

            @Override
            public void onFailure(Call<ModelResponse> call, Throwable t) {
                Toast.makeText(AdvertActivity.this,t.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loggedin() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<LoggedInResponse> call = apiInterface.loggedIn();
        call.enqueue(new Callback<LoggedInResponse>() {
            @Override
            public void onResponse(Call<LoggedInResponse> call, Response<LoggedInResponse> response) {
                LoggedInResponse loggedInResponse = response.body();
                DataLoggedIn dataLoggedIn = loggedInResponse.getDataLoggedIns().get(0);
                boolean succsess = dataLoggedIn.isSuccess();
                if (succsess){
                    Intent intent = new Intent(AdvertActivity.this,AddAdvertActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(AdvertActivity.this,"Войдите в аккаунт" , Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(AdvertActivity.this,LoginSignUpActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    finish();
                }
            }

            @Override
            public void onFailure(Call<LoggedInResponse> call, Throwable t) {
                Toast.makeText(AdvertActivity.this,t.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initApiCity() {
        loadingView = LoadingDialog.view(getFragmentManager());
       // loadingView.showLoading();
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<CityResponse> call = apiInterface.getCity("1");
        call.enqueue(new Callback<CityResponse>() {
            @Override
            public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {
                CityResponse cityResponse = response.body();
                DataCity dataCity = cityResponse.getDataCities().get(0);
                boolean success = dataCity.isSuccess();
                if(success){
                    List<InfoCity> cities = dataCity.getInfoCities();
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(cities);
                    realm.commitTransaction();
                    loadingView.hideLoading();
                }


            }

            @Override
            public void onFailure(Call<CityResponse> call, Throwable t) {
                Toast.makeText(AdvertActivity.this,t.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d("ssss","saveInstanceState");
        Log.d("ssss","saveInstanceState" + fragment);
        getFragmentManager().putFragment(outState,"fragment",fragment);
        outState.putInt("frag",1);
        outState.putString("time","sdsdsd");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("ssss","onPause");
        Bundle outState = new Bundle();
        Log.d("ssss","saveInstanceState");
//        Log.d("ssss","saveInstanceState" + fragment);
//        getSupportFragmentManager().putFragment(outState,"fragment",fragment);
        outState.putInt("frag",1);
        outState.putString("time","sdsdsd");
    }
}


