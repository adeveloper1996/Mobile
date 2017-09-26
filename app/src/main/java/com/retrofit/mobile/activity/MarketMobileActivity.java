package com.retrofit.mobile.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.retrofit.mobile.R;
import com.retrofit.mobile.activity.main.MainActivity;
import com.retrofit.mobile.fragment.AboutAppFragment;
import com.retrofit.mobile.fragment.AddAdvertFragment;
import com.retrofit.mobile.fragment.ArchiveFragment;
import com.retrofit.mobile.fragment.BuyerRequestFragment;
import com.retrofit.mobile.fragment.MakeOrderFragment;
import com.retrofit.mobile.fragment.MyOrderFragment;
import com.retrofit.mobile.fragment.MyProductFragment;
import com.retrofit.mobile.fragment.PersonalAreaFragment;
import com.retrofit.mobile.fragment.RegestrationProductFragment;
import com.retrofit.mobile.fragment.SettingsFragment;
import com.retrofit.mobile.fragment.SmsFragment;
import com.retrofit.mobile.model.DataLogged;
import com.retrofit.mobile.model.DataRegion;
import com.retrofit.mobile.model.InfoClient;
import com.retrofit.mobile.model.InfoRegion;
import com.retrofit.mobile.model.MyOrderId;
import com.retrofit.mobile.response.LoggedResponse;
import com.retrofit.mobile.response.RegionResponse;
import com.retrofit.mobile.rest.ApiClient;
import com.retrofit.mobile.rest.ApiInterface;
import com.retrofit.mobile.utils.CircleTransform;
import com.retrofit.mobile.utils.RoosterConnectionService;
import com.retrofit.mobile.utils.SessionManager;
import com.retrofit.mobile.utils.dialog.LoadingDialog;
import com.retrofit.mobile.utils.dialog.LoadingView;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarketMobileActivity extends AppCompatActivity implements View.OnClickListener{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private Fragment fragment;
    private SessionManager session;
    private LoadingView loadingView;
    private NavigationView navigationView;
    private ImageView navAvatar;
    private TextView txtAvatarName;
    private TextView txtUserType;
    private TextView txtUserId;
    private ImageButton iBtnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_mobile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.i("sss", "create");
        fragment = new PersonalAreaFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container_frame, fragment);
        ft.addToBackStack(null);
        ft.commit();

        loadingView = LoadingDialog.view(getFragmentManager());
        saveCredentialsAndLogin();
        initDrawerList();
        initRegion();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        intent = getIntent();
        if(getIntent() != null) {
            Log.i("sss", "notnull" + getIntent().getExtras());
        }
        String announ = intent.getStringExtra("openfrag");

        if(announ != null) {
            Log.i("sss", announ);
            if(announ.equals("3")) {
                fragment = new BuyerRequestFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.container_frame,fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        }
    }

    private void initRegion() {
        loadingView.showLoading();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<RegionResponse> call = apiInterface.getRegion();
        call.enqueue(new Callback<RegionResponse>() {
            @Override
            public void onResponse(Call<RegionResponse> call, Response<RegionResponse> response) {
                RegionResponse regionResponse = response.body();
                DataRegion dataRegion = regionResponse.getRegionList().get(0);
                List<InfoRegion> infoRegion = dataRegion.getObject();
                boolean success = dataRegion.isSuccess();

                if(success) {
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(infoRegion);
                    realm.commitTransaction();
                    loadingView.hideLoading();
                }else {
                    String error = dataRegion.getErrors();
                    Toast.makeText(MarketMobileActivity.this, error, Toast.LENGTH_SHORT).show();
                    loadingView.hideLoading();
                }
            }

            @Override
            public void onFailure(Call<RegionResponse> call, Throwable t) {
                Toast.makeText(MarketMobileActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                loadingView.hideLoading();
            }
        });
    }

    private void logout() {
        loadingView.showLoading();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<LoggedResponse> call = apiInterface.logout();
        call.enqueue(new Callback<LoggedResponse>() {
            @Override
            public void onResponse(Call<LoggedResponse> call, Response<LoggedResponse> response) {
                LoggedResponse loggedResponse = response.body();
                DataLogged dataLogged = loggedResponse.getDataLoggeds().get(0);
                boolean success = dataLogged.isSuccess();
                if(success) {
                    finish();
                    startActivity(new Intent(MarketMobileActivity.this, MainActivity.class));
                    loadingView.hideLoading();
                }
            }

            @Override
            public void onFailure(Call<LoggedResponse> call, Throwable t) {
                Toast.makeText(MarketMobileActivity.this,t.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initDrawerList() {
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);
        initNavHeader();
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.menu_navigation_buyer);
        if(MyOrderId.getUserType().equals("0")) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.menu_navigation_buyer);
        }else {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.menu_navigation_seller);
        }
        navigationView.setNavigationItemSelectedListener(new DrawerItemClickListener());
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer,R.string.close_drawer){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                if(MyOrderId.getUserType().equals("0")) {
                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.menu_navigation_buyer);
                    txtUserType.setText("Покупатель");
                } else {
                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.menu_navigation_seller);
                    txtUserType.setText("Продовец");
                }
                initNavHeader();

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void initNavHeader() {
        View navHeder = navigationView.getHeaderView(0);
        navAvatar = (ImageView)navHeder.findViewById(R.id.img_navigation_avatar);
        txtAvatarName = (TextView)navHeder.findViewById(R.id.txt_navigation_view_name);
        txtUserType = (TextView)navHeder.findViewById(R.id.txt_navigation_view_user_type);
        txtUserId = (TextView)navHeder.findViewById(R.id.txt_navigation_view_user_id);
        iBtnEdit = (ImageButton)navHeder.findViewById(R.id.iBtn_navigation_edit);
        navAvatar.setOnClickListener(this);
        iBtnEdit.setOnClickListener(this);
        Realm realm = Realm.getDefaultInstance();
        RealmResults<InfoClient> infoClients = realm.where(InfoClient.class).findAll();
        txtAvatarName.setText(infoClients.get(0).getName());
        txtUserId.setText(infoClients.get(0).getId());
        if(infoClients.get(0).getAvatar().length() == 0) {
            Picasso.with(this).load(R.drawable.ic_perm_identity_white_48dp).transform(new CircleTransform()).into(navAvatar);
        }else {
            Picasso.with(this).load(infoClients.get(0).getAvatar()).transform(new CircleTransform()).into(navAvatar);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_navigation_avatar:
                fragment = new PersonalAreaFragment();
                setFragment(fragment);
                setActionBarTitle("Личный кабинет");
                break;
            case R.id.iBtn_navigation_edit:
                startActivity(new Intent(this,EditPersonalActivity.class));
                break;
        }
    }

    private class DrawerItemClickListener implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            selectItem(item);
            return true;
        }
    }

    private void selectItem(MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.menuDoOrder:
                fragment = new MakeOrderFragment();
                break;
            case R.id.menuMyOrder:
                fragment = new MyOrderFragment();
                break;
            case R.id.menuSms:
                fragment = new SmsFragment();
                break;
            case R.id.menuAdvert:
                fragment = new AddAdvertFragment();
                break;
            case R.id.menuArchive:
                fragment = new ArchiveFragment();
                break;
            case R.id.menuRegistProduct:
                fragment = new RegestrationProductFragment();
                break;
            case R.id.menuMyTovar:
                fragment = new MyProductFragment();
                break;
            case R.id.menuRequestBuyer:
                fragment = new BuyerRequestFragment();
                break;
            case R.id.menuAboutApp:
                fragment = new AboutAppFragment();
                break;
            case R.id.menuSetting:
                fragment = new SettingsFragment();
                break;
            case R.id.menuExit:
                fragment = new PersonalAreaFragment();
                logout();
                break;
            case R.id.menu_settings:
                startActivity(new Intent(MarketMobileActivity.this, MainActivity.class));
                break;
            default:
                break;

        }
        setFragment(fragment);
        setActionBarTitle(item.getTitle().toString());
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_frame, fragment);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
        drawerLayout.closeDrawer(navigationView);
    }

    private void setActionBarTitle(String item) {
        String title = item;
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    private void saveCredentialsAndLogin()
    {
        Log.i("ssss","saveCredentialsAndLogin() called.");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit()
                .putString("xmpp_jid","bagdat@89.219.32.43")
                .putString("xmpp_password","123456789")
                .putBoolean("xmpp_logged_in",true)
                .apply();

       // Start the service
        Intent i1 = new Intent(this,RoosterConnectionService.class);
        startService(i1);

    }
}
