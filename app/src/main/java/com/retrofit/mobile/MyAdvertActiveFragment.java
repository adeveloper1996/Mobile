package com.retrofit.mobile;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
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


public class MyAdvertActiveFragment extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayout linearLayout;
    private LoadingView loadingView;

    public static  MyAdvertActiveFragment getInstance() {
        Bundle args = new Bundle();
        MyAdvertActiveFragment fragment = new MyAdvertActiveFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public MyAdvertActiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_advert_active, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingView = LoadingDialog.view(getActivity().getFragmentManager());
        linearLayout = (LinearLayout) view.findViewById(R.id.no_my_order_ll);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_show_my_advert);

        ApiInterface apiInterface = ApiClient.getApiInterface();
        loadingView.showLoading();
        Call<ShowMyAdvertResponse> call = apiInterface.showMyAdverts();
        call.enqueue(new Callback<ShowMyAdvertResponse>() {
            @Override
            public void onResponse(Call<ShowMyAdvertResponse> call, Response<ShowMyAdvertResponse> response) {
                ShowMyAdvertResponse showMyAdvertResponse = response.body();
                DataShowMyAdvert dataShowMyAdvert = showMyAdvertResponse.getMyAdverts().get(0);
                List<InfoShowMyAdvert> myAdverts = dataShowMyAdvert.getShowMyAdverts();
                boolean succ = dataShowMyAdvert.isSuccess();
                if(succ) {
                    if(myAdverts.size() == 0) {
                        linearLayout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        loadingView.hideLoading();
                    }else {
                        recyclerView.setAdapter(new ShowMyAdvertAdapter(myAdverts, R.layout.layout_my_advert, getContext(),2));
                        loadingView.hideLoading();
                    }
                }
            }

            @Override
            public void onFailure(Call<ShowMyAdvertResponse> call, Throwable t) {
                Toast.makeText(getContext(),t.toString(),Toast.LENGTH_SHORT).show();
                loadingView.hideLoading();
            }
        });
    }
}
