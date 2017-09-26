package com.retrofit.mobile.activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.retrofit.mobile.R;
import com.retrofit.mobile.adapter.CustomSpinnerAdapter;
import com.retrofit.mobile.adapter.SpinnerCityAdapter;
import com.retrofit.mobile.adapter.SpinnerModelAdapter;
import com.retrofit.mobile.model.DataAddAdvert;
import com.retrofit.mobile.model.DataCity;
import com.retrofit.mobile.model.DataModel;
import com.retrofit.mobile.model.InfoCity;
import com.retrofit.mobile.model.InfoModel;
import com.retrofit.mobile.model.InfoRegion;
import com.retrofit.mobile.response.AddAdvertResponse;
import com.retrofit.mobile.response.CityResponse;
import com.retrofit.mobile.response.ModelResponse;
import com.retrofit.mobile.rest.ApiClient;
import com.retrofit.mobile.rest.ApiInterface;
import com.retrofit.mobile.utils.dialog.LoadingDialog;
import com.retrofit.mobile.utils.dialog.LoadingView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

public class AddAdvertActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnGalerey;
    private Button btnMap;
    private Button btnAddAdvert;
    private Spinner spinnerCategory;
    private Spinner spinnerMark;
    private Spinner spinnerModel;
    private Spinner spinnerStatusState;
    private Spinner spinnerRegion;
    private Spinner spinnerCity;
    private EditText etxtPrice;
    private EditText etxtDescription;
    private EditText etxtEmail;
    private EditText etxtAddress;
    private EditText etxtPhone;
    TextView txtLengthDescription;
    private RadioButton rBtnDog;
    private RadioButton rBtnObmen;
    private RecyclerView recyclerHorizontal;
    private List<Image> images;
    private int numberOfImagesToSelect = 8;
    private LoadingView loadingView;
    private List<InfoModel> model = new ArrayList<>();
    private List<InfoCity> cities = new ArrayList<>();
    protected String categoryId;
    protected String markid;
    protected String modelid;
    private String newOrOld;
    private String dog;
    private String obm;
    protected String idCity;
    private String coordinate = "";
    private static final int REQUST_MAP_CODE = 101;
    private int countSelect = 0;
    private TextWatcher textWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_advert);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        loadingView = LoadingDialog.view(getFragmentManager());

        btnGalerey = (Button) findViewById(R.id.btn_add_advert_galerey);
        btnMap = (Button) findViewById(R.id.btn_add_advert_map);
        btnAddAdvert = (Button) findViewById(R.id.btn_advert_add_advert);

        spinnerCategory = (Spinner) findViewById(R.id.spinner_category);
        spinnerMark = (Spinner) findViewById(R.id.spinner_mark);
        spinnerModel = (Spinner) findViewById(R.id.spinner_model);
        spinnerStatusState = (Spinner) findViewById(R.id.spinner_status_state);
        spinnerRegion = (Spinner) findViewById(R.id.spinner_add_advert_region);
        spinnerCity = (Spinner) findViewById(R.id.spinner_add_advert_city);

        etxtPrice = (EditText) findViewById(R.id.etxt_add_advert_price);
        etxtDescription = (EditText) findViewById(R.id.etxt_description);
        etxtEmail = (EditText) findViewById(R.id.etxt_add_advert_email);
        etxtAddress = (EditText) findViewById(R.id.etxt_add_advert_address);
        etxtPhone = (EditText) findViewById(R.id.etxt_add_advert_phone);

        txtLengthDescription = (TextView) findViewById(R.id.txt_length_description);

        recyclerHorizontal = (RecyclerView) findViewById(R.id.img_recycler_horizontal);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerHorizontal.setLayoutManager(linearLayoutManager);
        decore(etxtPhone);

        rBtnDog = (RadioButton) findViewById(R.id.cbox_dogovornaya);
        rBtnObmen = (RadioButton)findViewById(R.id.cbox_obmen);

        btnGalerey.setOnClickListener(this);
        btnAddAdvert.setOnClickListener(this);
        btnMap.setOnClickListener(this);

        spinnerMark.setVisibility(View.GONE);
        spinnerModel.setVisibility(View.GONE);
        spinnerStatusState.setVisibility(View.GONE);


        spinnerCategory();
        spinnerStatusState();
        spinnerRegion();


    }

    private void spinnerCategory() {
        List<String> choose = new ArrayList<>();
        choose.add("Мобильные телефоны");
        choose.add("Планшеты");
        choose.add("Телефоны");
        choose.add("Рубрика");
        String defaultText = "Рубрика";
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, choose, defaultText, 1);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setSelection(adapter.getCount());
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinnerCategory.getSelectedItemPosition() == 0) {
                    categoryId = "2";
                }
                if(parent.getSelectedItem().equals("Мобильные телефоны")) {
                    categoryId = "1";
                    spinnerMark.setVisibility(View.VISIBLE);
                    spinnerMark();
                }
                if(parent.getSelectedItem().equals("Планшеты")) {
                    categoryId = "2";
                    spinnerMark.setVisibility(View.VISIBLE);
                    spinnerMark();
                }
                if(parent.getSelectedItem().equals("Телефоны")) {
                    categoryId = "3";
                    spinnerMark.setVisibility(View.VISIBLE);
                    spinnerMark();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void spinnerMark() {
        List<String> mark = new ArrayList<>();
        mark.add("Sumsung");
        mark.add("Apple");
        mark.add("Sony");
        mark.add("HTC");
        mark.add("Lenova");
        mark.add("Xiaomi");
        mark.add("Meizu");
        mark.add("Nokia");
        mark.add("Марка телефона");
        String defaultText = "Марка телефона";
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, mark, defaultText, 2);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerMark.setAdapter(adapter);
        spinnerMark.setSelection(adapter.getCount());
        spinnerMark.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadingView = LoadingDialog.view(getFragmentManager());
                if(spinnerMark.getSelectedItemPosition() == 0) {
                    markid = "1";
                    Log.i("ssss", "markId: " + markid);
                    countSelect++;
                    Log.i("ssss", "ipos " + countSelect);
                    if (countSelect == 2) {
                        modelNameApi("1");
                    }
                }

                if (spinnerMark.getSelectedItem().equals("Sumsung")) {
                    Log.i("ssss", "pos " + parent.getSelectedItemPosition());
                    loadingView.showLoading();
                    spinnerModel.setVisibility(View.VISIBLE);
                    spinnerStatusState.setVisibility(View.VISIBLE);
                    modelNameApi("1");
                    markid = "1";
                    Log.i("ssss", markid);
                    countSelect++;
                    Log.i("ssss", "i " + countSelect);
                }
                if (parent.getSelectedItem().equals("Apple")) {
                    Log.i("ssss", "pos " + parent.getSelectedItemPosition());
                    modelNameApi("2");
                    loadingView.showLoading();
                    spinnerModel.setVisibility(View.VISIBLE);
                    spinnerStatusState.setVisibility(View.VISIBLE);
                    markid = "2";
                    Log.i("ssss", markid);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void spinnerModel() {
       // String defaultText = "Модел";
        SpinnerModelAdapter adapter = new SpinnerModelAdapter(this, android.R.layout.simple_spinner_dropdown_item, model);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerModel.setAdapter(adapter);
        spinnerModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("ssss", model.get(position).getId());
                modelid = model.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void modelNameApi(String modelid) {
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<ModelResponse> call = apiInterface.getModel(modelid);
        call.enqueue(new Callback<ModelResponse>() {
            @Override
            public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                ModelResponse modelResponse = response.body();
                DataModel dataModel = modelResponse.getDataModels().get(0);
                boolean succ = dataModel.isSuccess();
                if(succ) {
                    model = dataModel.getInfoModels();
                    spinnerModel();
                    loadingView.hideLoading();
                } else {
                    startActivity(new Intent(AddAdvertActivity.this, MarketMobileActivity.class));
                }
            }

            @Override
            public void onFailure(Call<ModelResponse> call, Throwable t) {
                Toast.makeText(AddAdvertActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void spinnerStatusState() {
        List<String> state = new ArrayList<>();
        state.add("Новый");
        state.add("Б/У");
        state.add("Состояния");
        String defaultText = "Состояния";
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, state, defaultText, 2);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerStatusState.setAdapter(adapter);
        spinnerStatusState.setSelection(adapter.getCount());
        spinnerStatusState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerStatusState.getSelectedItemPosition() == 0) {
                    newOrOld = "1";
                    Log.i("ssss", "new" + newOrOld);
                }
                if (parent.getSelectedItem().equals("Новый")) {
                    newOrOld = "1";
                    Log.i("ssss", newOrOld);
                }
                if (parent.getSelectedItem().equals("Б/У")) {
                    newOrOld = "0";
                    Log.i("ssss", "old" + newOrOld);
                }
            }
        });
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
            Log.d("ssss","region " + region.get(i));
        }
        String defaultText = "Регион";
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, region, defaultText, 2);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerRegion.setAdapter(adapter);
        spinnerRegion.setSelection(adapter.getCount());
        spinnerRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinnerRegion.getSelectedItemPosition() == 0) {
                    Log.i("ssss", "0");
                }
                for (int i = 0; i < regions.size(); i++){
                    if(spinnerRegion.getSelectedItem().equals(regions.get(i).getName())){
                        Log.i("ssss", "position: " + regions.get(i).getId());
                        initApiCity(regions.get(i).getId());
                        loadingView.hideLoading();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initApiCity(String oblId) {
        loadingView.showLoading();
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<CityResponse> call = apiInterface.getCity(oblId);
        call.enqueue(new Callback<CityResponse>() {
            @Override
            public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {
                CityResponse cityResponse = response.body();
                DataCity dataCity = cityResponse.getDataCities().get(0);
                boolean success = dataCity.isSuccess();

                if(success) {
                    cities = dataCity.getInfoCities();
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(cities);
                    realm.commitTransaction();
                    spinnerCity.setVisibility(View.VISIBLE);
                    spinnerCity();
                }
                loadingView.hideLoading();
            }

            @Override
            public void onFailure(Call<CityResponse> call, Throwable t) {
                Toast.makeText(AddAdvertActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void spinnerCity() {
        SpinnerCityAdapter adapter = new SpinnerCityAdapter(this, R.layout.spinner_item, cities);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerCity.setAdapter(adapter);
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idCity = cities.get(position).getId();
                Log.i("ssss", "cityId: " + idCity);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_advert_add_advert:
                if(etxtPhone.getText().length() == 0 || etxtAddress.getText().length() == 0 || etxtDescription.getText().length() == 0 || idCity.length() == 0){
                    Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                }else {
                    initAddAdvert();
                }
                break;
            case R.id.btn_add_advert_galerey:
//                Intent intent = new Intent(this, AlbumSelectActivity.class);
//                //set limit on number of images that can be selected, default is 10
//                intent.putExtra(Constants.INTENT_EXTRA_LIMIT, numberOfImagesToSelect);
//                startActivityForResult(intent, Constants.REQUEST_CODE);
                break;
            case R.id.cbox_dogovornaya:
                rBtnDog.setChecked(true);
                rBtnObmen.setChecked(false);
                break;
            case R.id.cbox_obmen:
                rBtnObmen.setChecked(true);
                rBtnDog.setChecked(false);
                break;
            case R.id.btn_add_advert_map:
                loadingView.showLoading();
//                Intent intent1 = new Intent(this, MapCoordianteActivity.class);
//                startActivityForResult(intent1, REQUST_MAP_CODE);
//                loadingView.hideLoading();
                break;
        }
    }

    private void initAddAdvert() {
        Map<String, RequestBody> params = new HashMap<>();

        initDogObmen();

        RequestBody catId = RequestBody.create(MediaType.parse("text/plain"), categoryId);
        RequestBody dogovor = RequestBody.create(MediaType.parse("text/plain"), dog);
        RequestBody obmen = RequestBody.create(MediaType.parse("text/plain"), obm);
        RequestBody descrip = RequestBody.create(MediaType.parse("text/plain"), etxtDescription.getText().toString());
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), etxtEmail.getText().toString());
        RequestBody city = RequestBody.create(MediaType.parse("text/plain"), idCity);
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), etxtAddress.getText().toString());
        RequestBody isNew = RequestBody.create(MediaType.parse("text/plain"), newOrOld);
        RequestBody coord = RequestBody.create(MediaType.parse("text/plain"), coordinate);
        RequestBody markId = RequestBody.create(MediaType.parse("text/plain"), markid);
        RequestBody modelId = RequestBody.create(MediaType.parse("text/plain"), modelid);
        RequestBody price = RequestBody.create(MediaType.parse("text/plain"), etxtPrice.getText().toString());
        RequestBody phone1 = RequestBody.create(MediaType.parse("text/plain"), etxtPhone.getText().toString());
        RequestBody phone2 = RequestBody.create(MediaType.parse("text/plain"), etxtPhone.getText().toString());

//        for(int i = 0; i < images.size(); i++) {
//            //File file = new File(images.get(i));
//        }

        params.put("cat_id", catId);
        params.put("is_dogovornaja", dogovor);
        params.put("is_obmen", obmen);
        params.put("description", descrip);
        params.put("email", email);
        params.put("city_id", city);
        params.put("address", address);
        params.put("phone1", phone1);
        params.put("phone2", phone2);
        params.put("coords", coord);
        params.put("is_new", isNew);
        params.put("mark_id", markId);
        params.put("model_id", modelId);
        params.put("price", price);

        loadingView = LoadingDialog.view(getFragmentManager());
        loadingView.showLoading();
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<AddAdvertResponse> call = apiInterface.addAdvert(params);
        call.enqueue(new Callback<AddAdvertResponse>() {
            @Override
            public void onResponse(Call<AddAdvertResponse> call, Response<AddAdvertResponse> response) {
                AddAdvertResponse addAdvertResponse = response.body();
                DataAddAdvert dataAddAdvert = addAdvertResponse.getAdverts().get(0);
                boolean success = dataAddAdvert.isSuccess();
                if(success) {
                    startActivity(new Intent(AddAdvertActivity.this, MarketMobileActivity.class));
                    Toast.makeText(AddAdvertActivity.this, "Объявление отрпавлено", Toast.LENGTH_SHORT).show();
                    loadingView.hideLoading();
                } else {
                    loadingView.hideLoading();
                }
            }

            @Override
            public void onFailure(Call<AddAdvertResponse> call, Throwable t) {
                Toast.makeText(AddAdvertActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                loadingView.hideLoading();
            }
        });
    }

    private void initDogObmen() {
        if(rBtnDog.isChecked()) {
            dog = "1";
        }else {
            dog = "0";
        }
        if(rBtnObmen.isChecked()){
            obm = "1";
        }else {
            obm = "0";
        }
    }

    private void decore(EditText editText) {
        Slot[] slots = new UnderscoreDigitSlotsParser().parseSlots("(___)___-___");
        FormatWatcher formatWatcher = new MaskFormatWatcher(MaskImpl.createNonTerminated(slots));
        formatWatcher.installOn(editText);
    }
}
