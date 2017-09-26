package com.retrofit.mobile.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.retrofit.mobile.R;
import com.retrofit.mobile.activity.login_registr.LoginSignUp;
import com.retrofit.mobile.model.DataAddAdvertFavorite;
import com.retrofit.mobile.model.DataDeleteAdvertFavorite;
import com.retrofit.mobile.model.DataLoggedIn;
import com.retrofit.mobile.model.DataViewCount;
import com.retrofit.mobile.model.InfoAllAdvert;
import com.retrofit.mobile.model.PhotoAllAdvert;
import com.retrofit.mobile.response.AddAdvFavoriteResponse;
import com.retrofit.mobile.response.DeleteAdvFavoriteResponse;
import com.retrofit.mobile.response.LoggedInResponse;
import com.retrofit.mobile.response.ViewCountResponse;
import com.retrofit.mobile.rest.ApiClient;
import com.retrofit.mobile.rest.ApiInterface;
import com.retrofit.mobile.utils.CircleTransform;
import com.retrofit.mobile.utils.dialog.LoadingDialog;
import com.retrofit.mobile.utils.dialog.LoadingView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import io.realm.Realm;
import io.realm.RealmQuery;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdvertDetailsActivity extends AppCompatActivity implements View.OnClickListener{
    private static ViewPager mPager;
    private List<PhotoAllAdvert> photoAllAdverts = new ArrayList<>();
    private InfoAllAdvert advert;
    private TextView txtCategory;
    private TextView txtModel;
    private TextView txtState;
    private TextView email;
    private TextView address;
    private TextView txtMobPrice;
    private TextView txtUserId;
    private TextView txtUserName;
    private ImageView imgAvatar;
    private TextView txtDescription;
    private TextView txtLastActivity;
    private Button btnPoja;
    private ImageButton btnSendPrice;
    private ImageButton iBtnAddFavorite;
    private ImageButton iBtnShare;
    private ImageView imgAddress;
    private EditText eTxtPrice;
    private String state;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private String infoCity;
    private boolean FAB_Status = false;
    private Animation show_fab_1;
    private Animation hide_fab_1;
    private Animation hide_fab_2;
    private Animation show_fab_2;
    private int sdk = android.os.Build.VERSION.SDK_INT;
    private Realm realm;
    private String idAdvert;
    private LoadingView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("");
        floatingActionButton();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        loadingView = LoadingDialog.view(getFragmentManager());
        idAdvert = (String) getIntent().getExtras().get("advertPhoto");
        realm = Realm.getDefaultInstance();
        RealmQuery<InfoAllAdvert> infoAllAdverts = realm.where(InfoAllAdvert.class).contains("id",idAdvert);
        advert = infoAllAdverts.findFirst();
        photoAllAdverts = infoAllAdverts.findFirst().getPhotos();

        initWidget();
        initViewPager();
        initAdvertDetails();
        initApiCount();

    }

    private void initApiCount() {
        ApiInterface apiInterface = ApiClient.getApiInterface();
        loadingView.showLoading();
        Call<ViewCountResponse> call = apiInterface.getViewCount(advert.getId());
        Log.i("ssss","id "+ advert.getId());
        call.enqueue(new Callback<ViewCountResponse>() {
            @Override
            public void onResponse(Call<ViewCountResponse> call, Response<ViewCountResponse> response) {
                ViewCountResponse viewCountResponse = response.body();
                DataViewCount viewCount = viewCountResponse.getViewCountList().get(0);
                boolean success = viewCount.isSuccess();
                if(success) {
                    Log.i("ssss","true");
                }else {
                    Log.i("ssss", "false");
                }
                loadingView.hideLoading();
            }

            @Override
            public void onFailure(Call<ViewCountResponse> call, Throwable t) {
                Toast.makeText(AdvertDetailsActivity.this,t.toString(), Toast.LENGTH_SHORT).show();
                loadingView.hideLoading();
            }
        });
    }

    private void initAdvertDetails() {
        String coor = advert.getCoords();

        if(!coor.isEmpty()){
          for(int i=0; i<coor.length(); i++) {
              if(coor.charAt(i) == ',') {
                  int j = i;
                  String lt = coor.substring(0, j).trim();
                  String ln = coor.substring(j+1, coor.length()).trim();
                  String url = "http://maps.google.com/maps/api/staticmap?center=" + lt + "," + ln + "&zoom=13&size=600x300&sensor=false";
                  Picasso.with(this).load(url).centerCrop().fit().into(imgAddress);
                  Log.i("ssss", "lt" + lt);
                  Log.i("ssss", "ln" + ln);
              }
          }
        }else {
            imgAddress.setVisibility(View.GONE);
        }

        txtCategory.setText(advert.getMarkname());
        txtModel.setText(advert.getModelname());
        if(advert.getStatus().equals("1")) {
            txtState.setText("Новый");
        }else {
            txtState.setText("Б/У");
        }
        String idcity = advert.getCityId();
//        if(Integer.parseInt(idcity) < 3) {
//            Realm realm1 = Realm.getDefaultInstance();
//            RealmQuery<InfoCity> cities = realm1.where(InfoCity.class).contains("id", idcity);
//            infoCity = cities.findFirst().getName();
//            email.setText(infoCity);
//        }
        address.setText(advert.getAddress());
        txtMobPrice.setText(advert.getPrice() +" тг");
        txtUserId.setText("ID:"+advert.getUserId());
        txtUserName.setText(advert.getFio());
        txtDescription.setText(advert.getDescription());
        txtLastActivity.setText("Последняя активность: " + timestampYMD(Long.parseLong(advert.getLastActivity())));

        if(advert.getFavourite() == 0) {
            iBtnAddFavorite.setBackgroundResource(R.drawable.ic_star_outline);
        }else {
            iBtnAddFavorite.setBackgroundResource(R.drawable.ic_star_outline_full);
        }
        Picasso.with(this).load(advert.getAvatar()).error(R.drawable.img_vopros).transform(new CircleTransform()).into(imgAvatar);

        iBtnAddFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loggedIn();
            }
        });

        iBtnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = "https://play.google.com/store/apps/details?id=marketmobile.project.kz.marketmobile";
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, address);
                startActivity(Intent.createChooser(shareIntent, ""));
            }
        });
    }



    private void floatingActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_advert);
        fab.setOnClickListener(v->{
            if(FAB_Status == false) {
                // Display FAB menu
                expandFab();
                FAB_Status = true;
            }else {
                // Close FAB menu
                hideFAB();
                FAB_Status = false;
            }

        });

        fab1 = (FloatingActionButton) findViewById(R.id.fab_1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab_2);

        if(sdk > Build.VERSION_CODES.KITKAT) {
            show_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_show_21);
            hide_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_hide_21);
            hide_fab_2 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab2_hide_21);
            show_fab_2 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab2_show_21);
        }
        else {
            show_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_show);
            hide_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_hide);
            hide_fab_2 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab2_hide);
            show_fab_2 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab2_show);
        }

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initApiNumberCount();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel","8"+advert.getPhone1(),null));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms","8"+advert.getPhone1(),null));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            }
        });
    }

    private void expandFab() {
        if(sdk > Build.VERSION_CODES.KITKAT) {
            // Floating Action Button 1
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab1.getLayoutParams();
            layoutParams.rightMargin += (int) (fab1.getWidth() * 0.3);
            layoutParams.bottomMargin += (int) (fab1.getHeight() * 1.5);
            fab1.setLayoutParams(layoutParams);
            fab1.startAnimation(show_fab_1);
            fab1.setClickable(true);

            // Floating Action Button 2
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fab2.getLayoutParams();
            layoutParams2.rightMargin += (int) (fab2.getWidth() * 0.3);
            layoutParams2.bottomMargin += (int) (fab2.getHeight() * 3.0);
            fab2.setLayoutParams(layoutParams2);
            fab2.startAnimation(show_fab_2);
            fab2.setClickable(true);
        }
          else {
            //Floating Action Button 1
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab1.getLayoutParams();
            layoutParams.rightMargin += (int) (fab1.getWidth() * 0.1);
            layoutParams.bottomMargin += (int) (fab1.getHeight() * 0.6);
            fab1.setLayoutParams(layoutParams);
            fab1.startAnimation(show_fab_1);
            fab1.setClickable(true);

            //Floating Action Button 2
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fab2.getLayoutParams();
            layoutParams2.rightMargin += (int) (fab2.getWidth() * 0.1);
            layoutParams2.bottomMargin += (int) (fab2.getHeight() * 1.3);
            fab2.setLayoutParams(layoutParams2);
            fab2.startAnimation(show_fab_2);
            fab2.setClickable(true);
        }
    }

    private void hideFAB() {
        if(sdk > Build.VERSION_CODES.KITKAT) {
            //Floating Action Button 1
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab1.getLayoutParams();
            layoutParams.rightMargin -= (int) (fab1.getWidth() * 0.3);
            layoutParams.bottomMargin -= (int) (fab1.getHeight() * 1.5);
            fab1.setLayoutParams(layoutParams);
            fab1.startAnimation(hide_fab_1);
            fab1.setClickable(false);

            //Floating Action Button 2
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fab2.getLayoutParams();
            layoutParams2.rightMargin -= (int) (fab2.getWidth() * 0.3);
            layoutParams2.bottomMargin -= (int) (fab2.getHeight() * 3.0);
            fab2.setLayoutParams(layoutParams2);
            fab2.startAnimation(hide_fab_2);
            fab2.setClickable(false);
        }else {
            //Floating Action Button 1
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab1.getLayoutParams();
            layoutParams.rightMargin -= (int) (fab1.getWidth() * 0.1);
            layoutParams.bottomMargin -= (int) (fab1.getHeight() * 0.6);
            fab1.setLayoutParams(layoutParams);
            fab1.startAnimation(hide_fab_1);
            fab1.setClickable(false);

            //Floating Action Button 2
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fab2.getLayoutParams();
            layoutParams2.rightMargin -= (int) (fab2.getWidth() * 0.1);
            layoutParams2.bottomMargin -= (int) (fab2.getHeight() * 1.3);
            fab2.setLayoutParams(layoutParams2);
            fab2.startAnimation(hide_fab_2);
            fab2.setClickable(false);
        }
    }

    private void initApiNumberCount() {
        ApiInterface apiInterface = ApiClient.getApiInterface();
        loadingView.showLoading();
        Call<ViewCountResponse> call = apiInterface.getViewCountNumber(advert.getId());
        Log.i("ssss","id "+advert.getId());
        call.enqueue(new Callback<ViewCountResponse>() {
            @Override
            public void onResponse(Call<ViewCountResponse> call, Response<ViewCountResponse> response) {
                ViewCountResponse countResponse = response.body();
                DataViewCount viewCount = countResponse.getViewCountList().get(0);
                boolean success = viewCount.isSuccess();
                if(success){
                    Log.i("ssss","true");
                }else {
                    String error = viewCount.getErrors().get(0);
                    Toast.makeText(AdvertDetailsActivity.this,error, Toast.LENGTH_SHORT).show();
                }
                loadingView.hideLoading();
            }

            @Override
            public void onFailure(Call<ViewCountResponse> call, Throwable t) {
                Toast.makeText(AdvertDetailsActivity.this,t.toString(), Toast.LENGTH_SHORT).show();
                loadingView.hideLoading();
            }
        });
    }

    private void initWidget() {
        txtCategory = (TextView)findViewById(R.id.txt_advert_details_category);
        txtModel = (TextView)findViewById(R.id.txt_advert_details_model);
        txtState = (TextView)findViewById(R.id.txt_advert_details_state);
        address = (TextView)findViewById(R.id.txt_advert_details_address);
        email = (TextView)findViewById(R.id.txt_advert_details_email);
        iBtnAddFavorite = (ImageButton)findViewById(R.id.i_btn_add_favorite);
        imgAddress = (ImageView)findViewById(R.id.img_address_map);
        txtMobPrice = (TextView)findViewById(R.id.txt_advert_details_price);
        if(sdk > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 60);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.ALIGN_PARENT_START);
            txtMobPrice.setLayoutParams(layoutParams);
        }
        btnPoja = (Button)findViewById(R.id.btn_ad_det_poja);
        btnSendPrice = (ImageButton)findViewById(R.id.btn_adv_det_send_price);
        txtUserId = (TextView)findViewById(R.id.txt_advert_det_id_person);
        txtUserName = (TextView)findViewById(R.id.txt_advert_det_name_person);
        imgAvatar = (ImageView)findViewById(R.id.img_anketa_seller_person);
        txtDescription = (TextView)findViewById(R.id.txt_desscription_advert);
        txtLastActivity = (TextView)findViewById(R.id.lastActive);
        iBtnShare = (ImageButton)findViewById(R.id.i_btn_share_advert_details);
    }

    private void initViewPager() {
        mPager = (ViewPager) findViewById(R.id.slide_view_pager);
        CircleIndicator circleIndicator = (CircleIndicator)findViewById(R.id.indicatorSlider);
        mPager.setAdapter(new SliderImageAdapter(photoAllAdverts,this));
        mPager.addOnPageChangeListener(viewPagerPageChangeListener);
        circleIndicator.setViewPager(mPager);
    }



    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    @Override
    public void onClick(View v) {

    }

    private void loggedIn() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<LoggedInResponse> call = apiInterface.loggedIn();
        call.enqueue(new Callback<LoggedInResponse>() {
            @Override
            public void onResponse(Call<LoggedInResponse> call, Response<LoggedInResponse> response) {
                LoggedInResponse loggedInResponse = response.body();
                DataLoggedIn dataLoggedIn = loggedInResponse.getDataLoggedIns().get(0);
                boolean success = dataLoggedIn.isSuccess();
                if(success) {
                    if (advert.getFavourite() == 0) {
                        iBtnAddFavorite.setBackgroundResource(R.drawable.ic_star_outline_full);
                        realm.beginTransaction();
                        RealmQuery<InfoAllAdvert> realmQuery = realm.where(InfoAllAdvert.class).contains("id",idAdvert);
                        realmQuery.findFirst().setFavourite(1);
                        realm.commitTransaction();
                        addFavoriteApi(idAdvert);

                    }else {
                        iBtnAddFavorite.setBackgroundResource(R.drawable.ic_star_outline);
                        realm.beginTransaction();
                        RealmQuery<InfoAllAdvert> realmQuery = realm.where(InfoAllAdvert.class).contains("id",idAdvert);
                        realmQuery.findFirst().setFavourite(0);
                        realm.commitTransaction();
                        deleteFavoriteApi(idAdvert);
                    }
                }
                else {
                    Intent intent = new Intent(AdvertDetailsActivity.this, LoginSignUp.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<LoggedInResponse> call, Throwable t) {
                Toast.makeText(AdvertDetailsActivity.this,t.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteFavoriteApi(String idAdvert) {
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<DeleteAdvFavoriteResponse> call = apiInterface.deleteFavoriteAdvert(idAdvert);
        call.enqueue(new Callback<DeleteAdvFavoriteResponse>() {
            @Override
            public void onResponse(Call<DeleteAdvFavoriteResponse> call, Response<DeleteAdvFavoriteResponse> response) {
                DeleteAdvFavoriteResponse favoriteResponse = response.body();
                DataDeleteAdvertFavorite advertFavorite = favoriteResponse.getAdvertFavorites().get(0);
                boolean success = advertFavorite.isSuccess();
            }

            @Override
            public void onFailure(Call<DeleteAdvFavoriteResponse> call, Throwable t) {

            }
        });
    }

    private void addFavoriteApi(String idAdvert) {

        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<AddAdvFavoriteResponse> call = apiInterface.addFavoriteAdvert(idAdvert);
        call.enqueue(new Callback<AddAdvFavoriteResponse>() {
            @Override
            public void onResponse(Call<AddAdvFavoriteResponse> call, Response<AddAdvFavoriteResponse> response) {
                AddAdvFavoriteResponse favoriteResponse = response.body();
                DataAddAdvertFavorite advertFavorite = favoriteResponse.getAdvertFavorites().get(0);
                boolean success = advertFavorite.isSuccess();
            }

            @Override
            public void onFailure(Call<AddAdvFavoriteResponse> call, Throwable t) {

            }
        });
    }

    public String timestampYMD(long time){
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.setTimeInMillis(time * 1000);
        calendar.add(Calendar.MILLISECOND,tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat forma = new SimpleDateFormat("dd.MM.yyyy");
        Date curretntTimeZone = (Date) calendar.getTime();
        String formated = forma.format(curretntTimeZone);
        return formated;
    }
}
