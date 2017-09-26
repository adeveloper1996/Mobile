package com.retrofit.mobile.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.retrofit.mobile.R;
import com.retrofit.mobile.adapter.AdvertSingleAdapter;
import com.retrofit.mobile.model.DataAllAdvert;
import com.retrofit.mobile.model.InfoAllAdvert;
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

public class AdvertSingleFragment extends Fragment {
    private RecyclerView recyclerView;
    private HashMap<String,String> filter;
    private LoadingView loadingView;
    private FloatingActionButton fab;
    private LinearLayout linearLayout;
    private AdvertSingleAdapter adapter;
    private List<InfoAllAdvert> allAdverts;
    protected android.os.Handler handler;
    private int pageint = 0;
    private Toolbar toolbar;
    private Animation animMovie,animSlideUp;
    private ProgressBar progressBar;
    private SearchView searchView;
    private MenuItem item;


    public AdvertSingleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_advert_single, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingView = LoadingDialog.view(getActivity().getFragmentManager());
        allAdverts = new ArrayList<>();
        handler = new android.os.Handler();
        linearLayout = (LinearLayout) getActivity().findViewById(R.id.hll);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar1);
        fab = (FloatingActionButton)getActivity().findViewById(R.id.fab_advert);
        toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        initAnim();
        filter = new HashMap<>();
        Bundle bundle = getArguments();
        if(bundle != null){
            Log.i("ssss",""+2);
            filter = (HashMap<String, String>) bundle.getSerializable("filter");
            String s = bundle.getString("jjj");
            Log.i("ssss","str"+s);
            for (int i = 0; i < filter.size(); i++) {
                Log.i("ssss",""+filter.size());
                Log.i("ssss","map"+filter.get(i));
            }
        }
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_advert_single);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));


        adapter = new AdvertSingleAdapter(allAdverts,getActivity(),recyclerView,R.layout.recycler_advert_single);
        adapter.setOnLoadMoreListener(() -> {
            allAdverts.add(null);
            adapter.notifyItemInserted(allAdverts.size() - 1);

            recyclerView.postDelayed(() -> {
                allAdverts.remove(allAdverts.size() - 1);
                adapter.notifyItemRemoved(allAdverts.size());
                int index = pageint = pageint + 1;
                Log.d("ssss",""+index);
                loadMore(index);
            },2000);
        });

        loadAdverts(0);

//        recyclerView.setAdapter(adapter);

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

    private void initAnim() {
        animMovie = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.movie);
        animSlideUp = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.slide_up);
    }

    private void loadAdverts(int page) {
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<AllAdvertResronse> call = apiInterface.getAllAdvert(filter,page);
        loadingView.showLoading();
        call.enqueue(new Callback<AllAdvertResronse>() {
            @Override
            public void onResponse(Call<AllAdvertResronse> call, Response<AllAdvertResronse> response) {
                AllAdvertResronse advertResronse = response.body();
                DataAllAdvert allAdvert = advertResronse.getAllAdvertList().get(0);
                List<InfoAllAdvert> infoAllAdverts = allAdvert.getAllAdverts();
                boolean success = allAdvert.isSuccess();
                if(success){
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(infoAllAdverts);
                    realm.commitTransaction();
                    allAdverts.addAll(infoAllAdverts);
                    recyclerView.setAdapter(adapter);
                    loadingView.hideLoading();
                }
            }

            @Override
            public void onFailure(Call<AllAdvertResronse> call, Throwable t) {
                Toast.makeText(getActivity(),t.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMore(int index){
        //add loading progress view
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<AllAdvertResronse> call = apiInterface.getAllAdvert(filter,index);
        call.enqueue(new Callback<AllAdvertResronse>() {
            @Override
            public void onResponse(Call<AllAdvertResronse> call, Response<AllAdvertResronse> response) {
                if(response.isSuccessful()){
                    AllAdvertResronse advertResronse = response.body();
                    DataAllAdvert allAdvert = advertResronse.getAllAdvertList().get(0);
                    List<InfoAllAdvert> infoAllAdverts = allAdvert.getAllAdverts();
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(infoAllAdverts);
                    realm.commitTransaction();
                    allAdverts.addAll(infoAllAdverts);
                    adapter.notifyItemInserted(allAdverts.size());
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_advert, menu);
        super.onCreateOptionsMenu(menu, inflater);

        item = menu.findItem(R.id.action_search);
        searchView = new SearchView(getActivity());
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.white));
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);
        searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {});

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                final List<InfoAllAdvert> filterModelList = filter(allAdverts, searchQuery);
                adapter.setFilter(filterModelList);
                return true;

            }
        });

        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                adapter.setFilter(allAdverts);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<InfoAllAdvert> filter(List<InfoAllAdvert> models, String query) {
        query = query.toLowerCase();
        final List<InfoAllAdvert> filterModelList = new ArrayList<>();
        for(InfoAllAdvert model : models) {
            final  String text = model.getModelname().toLowerCase();
            if(text.contains(query)) {
                filterModelList.add(model);
            }
        }
        return filterModelList;
    }
}
