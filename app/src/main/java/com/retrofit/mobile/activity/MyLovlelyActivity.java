package com.retrofit.mobile.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.retrofit.mobile.R;
import com.retrofit.mobile.adapter.AdvertSingleAdapter;
import com.retrofit.mobile.model.DataAllAdvert;
import com.retrofit.mobile.model.InfoAllAdvert;
import com.retrofit.mobile.response.AllAdvertResronse;
import com.retrofit.mobile.rest.ApiClient;
import com.retrofit.mobile.rest.ApiInterface;
import com.retrofit.mobile.utils.OnLoadMoreListener;
import com.retrofit.mobile.utils.dialog.LoadingDialog;
import com.retrofit.mobile.utils.dialog.LoadingView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyLovlelyActivity extends AppCompatActivity {
    private LinearLayout linearLayout;
    private RecyclerView recyclerView;
    private AdvertSingleAdapter adapter;
    private LoadingView loadingView;
    private List<InfoAllAdvert> allAdvert;
    protected android.os.Handler handler;
    private int pageint = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lovlely);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        allAdvert = new ArrayList<>();
        loadingView = LoadingDialog.view(getFragmentManager());
        recyclerView = (RecyclerView) findViewById(R.id.recycler_my_lovely_advert);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AdvertSingleAdapter(allAdvert, this, recyclerView, R.layout.recycler_my_lovely_advert);
        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                allAdvert.add(null);
                adapter.notifyItemInserted(allAdvert.size() -1);

                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                     allAdvert.remove(allAdvert.size() - 1);
                     adapter.notifyItemRemoved(allAdvert.size());
                        int index = pageint = pageint + 1;
                        Log.d("ssss","" + index);
                        loadMore(index);
                    }
                },200);
            }
        });

        loadAdverts(0);
    }

    private void loadAdverts(int page) {
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<AllAdvertResronse> call = apiInterface.myFavoriteAdvert(page);
        loadingView.showLoading();
        call.enqueue(new Callback<AllAdvertResronse>() {
            @Override
            public void onResponse(Call<AllAdvertResronse> call, Response<AllAdvertResronse> response) {
                AllAdvertResronse advertResronse = response.body();
                DataAllAdvert dataAllAdvert = advertResronse.getAllAdvertList().get(0);
                List<InfoAllAdvert> infoAllAdverts = dataAllAdvert.getAllAdverts();
                if (infoAllAdverts.size() == 0){
                    linearLayout = (LinearLayout)findViewById(R.id.no_lovely_advert_ll);
                    linearLayout.setVisibility(View.VISIBLE);
                }
                boolean success = dataAllAdvert.isSuccess();
                if(success){
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(infoAllAdverts);
                    realm.commitTransaction();
                    allAdvert.addAll(infoAllAdverts);
                    recyclerView.setAdapter(adapter);
                    loadingView.hideLoading();
                }
            }

            @Override
            public void onFailure(Call<AllAdvertResronse> call, Throwable t) {
                Toast.makeText(MyLovlelyActivity.this,t.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMore(int index){
        //add loading progress view
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<AllAdvertResronse> call = apiInterface.myFavoriteAdvert(index);
        call.enqueue(new Callback<AllAdvertResronse>() {
            @Override
            public void onResponse(Call<AllAdvertResronse> call, Response<AllAdvertResronse> response) {
                if(response.isSuccessful()){
                    AllAdvertResronse advertResronse = response.body();
                    DataAllAdvert dataAllAdvert = advertResronse.getAllAdvertList().get(0);
                    List<InfoAllAdvert> infoAllAdverts = dataAllAdvert.getAllAdverts();
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(infoAllAdverts);
                    realm.commitTransaction();
                    allAdvert.addAll(infoAllAdverts);
                    adapter.notifyItemInserted(allAdvert.size());
                    adapter.setLoaded();
                }else{
                    Log.e("ssss"," Load More Response Error "+String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<AllAdvertResronse> call, Throwable t) {
                Log.e("ssss"," Load More Response Error "+t.getMessage());
            }
        });
    }
}
