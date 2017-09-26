package com.retrofit.mobile;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.retrofit.mobile.adapter.ShowMyAdvertAdapter;
import com.retrofit.mobile.model.DataShowMyAdvert;
import com.retrofit.mobile.model.InfoShowMyAdvert;
import com.retrofit.mobile.response.ShowMyAdvertResponse;
import com.retrofit.mobile.rest.ApiClient;
import com.retrofit.mobile.rest.ApiInterface;
import com.retrofit.mobile.utils.dialog.LoadingDialog;
import com.retrofit.mobile.utils.dialog.LoadingView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyAdvertArchiveFragment extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayout linearLayout;
    private LoadingView loadingView;

    public static MyAdvertArchiveFragment getInstance() {
        Bundle args = new Bundle();
        MyAdvertArchiveFragment fragment = new MyAdvertArchiveFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public MyAdvertArchiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_advert_archive, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingView = LoadingDialog.view(getActivity().getFragmentManager());
        linearLayout = (LinearLayout)view.findViewById(R.id.no_my_advert_archive);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_show_my_advert_archive);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<ShowMyAdvertResponse> call = apiInterface.showMyAdvertsArchive();
        call.enqueue(new Callback<ShowMyAdvertResponse>() {
            @Override
            public void onResponse(Call<ShowMyAdvertResponse> call, Response<ShowMyAdvertResponse> response) {
                ShowMyAdvertResponse myAdvertResponse = response.body();
                DataShowMyAdvert showMyAdvert = myAdvertResponse.getMyAdverts().get(0);
                List<InfoShowMyAdvert> myAdvert = showMyAdvert.getShowMyAdverts();
                Log.i("ssss","size " + myAdvert.size());
                boolean succes = showMyAdvert.isSuccess();
                if(succes){
                    if(myAdvert.size() == 0){
                        linearLayout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }else {
                        recyclerView.setAdapter(new ShowMyAdvertAdapter(myAdvert, R.layout.layout_my_advert, getContext(),2));
                    }
                }else {
                    String error = showMyAdvert.getErrors().get(0);
                    Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ShowMyAdvertResponse> call, Throwable t) {
                Toast.makeText(getContext(),t.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    }
