package com.retrofit.mobile.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.retrofit.mobile.R;
import com.retrofit.mobile.adapter.AdvertGridViewAdapter;
import com.retrofit.mobile.model.DataAllAdvert;
import com.retrofit.mobile.model.InfoAllAdvert;
import com.retrofit.mobile.model.PhotoAllAdvert;
import com.retrofit.mobile.response.AllAdvertResronse;
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


public class AdvertGridFragment extends Fragment {
    private List<InfoAllAdvert> infoAllAdverts = new ArrayList<>();
    private List<PhotoAllAdvert> photoAllAdverts = new ArrayList<>();
    private InfoAllAdvert advert = new InfoAllAdvert();
    private HashMap<String, String> filter;
    private LoadingView loadingView;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private LinearLayout linearLayout;
    private AdvertGridViewAdapter adapter;
    protected android.os.Handler handler;
    private int pageint = 0;
    private SearchView searchView;
    private MenuItem item;

    public AdvertGridFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_advert_grid, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_advert);
        linearLayout = (LinearLayout) getActivity().findViewById(R.id.hll);
        infoAllAdverts = new ArrayList<>();
        handler = new android.os.Handler();
        loadingView = LoadingDialog.view(getActivity().getFragmentManager());
        filter = new HashMap<>();
        Bundle bundle = getArguments();
        if(bundle != null) {
            Log.i("ssss", ""+2);
            filter = (HashMap<String, String>) bundle.getSerializable("filter");
            String s = bundle.getString("jjj");
            Log.i("ssss", "str"+s);
            for(int i = 0; i<filter.size(); i++) {
                Log.i("ssss", ""+filter.size());
                Log.i("ssss", "map"+filter.get(i));
            }
        }

        recyclerView = (RecyclerView)getActivity().findViewById(R.id.recycler_grid_view_advert);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setItemViewCacheSize(50);
        recyclerView.setHasFixedSize(true);

        adapter = new AdvertGridViewAdapter(infoAllAdverts, getActivity(), recyclerView);
        adapter.setOnLoadMoreListener(() -> {
            infoAllAdverts.add(null);
            adapter.notifyItemInserted(infoAllAdverts.size() - 1);

        recyclerView.postDelayed(() -> {
            infoAllAdverts.remove(infoAllAdverts.size() - 1);
            adapter.notifyItemRemoved(infoAllAdverts.size());
            int index = pageint = pageint + 1;
            Log.d("ssss", "" + index);
           // loadMore(index);
        },2000);

        });

        loadAdvert(0);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx,int dy){
                super.onScrolled(recyclerView, dx, dy);

                if (dy >0) {
                    // Scroll Down
                    if (fab.isShown()) {
                        fab.hide();
                    }
                }
                else if (dy <0) {
                    // Scroll Up
                    if (!fab.isShown()) {
                        fab.show();
                    }
                }
            }
        });
    }

    private void loadAdvert(int page) {
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<AllAdvertResronse> call = apiInterface.getAllAdvert(filter, page);
        loadingView.showLoading();
        call.enqueue(new Callback<AllAdvertResronse>() {
            @Override
            public void onResponse(Call<AllAdvertResronse> call, Response<AllAdvertResronse> response) {
                AllAdvertResronse advertResronse = response.body();
                DataAllAdvert dataAllAdvert = advertResronse.getAllAdvertList().get(0);
                List<InfoAllAdvert> allAdverts = dataAllAdvert.getAllAdverts();
                boolean success = dataAllAdvert.isSuccess();
                if(success) {
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(allAdverts);
                    realm.commitTransaction();
                    infoAllAdverts.addAll(allAdverts);
                    loadingView.hideLoading();
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<AllAdvertResronse> call, Throwable t) {
                Toast.makeText(getActivity(),t.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
