package com.retrofit.mobile.fragment;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.retrofit.mobile.R;
import com.retrofit.mobile.adapter.MyOrdersAdapter;
import com.retrofit.mobile.model.DataMyOrder;
import com.retrofit.mobile.model.InfoMyOrder;
import com.retrofit.mobile.response.MyOrderResponse;
import com.retrofit.mobile.rest.ApiClient;
import com.retrofit.mobile.rest.ApiInterface;
import com.retrofit.mobile.utils.dialog.LoadingDialog;
import com.retrofit.mobile.utils.dialog.LoadingView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyOrderFragment extends Fragment implements View.OnClickListener{
    private LinearLayout linearLayout;
    private RecyclerView recyclerView;
    private Button btnAddOrder;
    private LoadingView loadingView;

    public MyOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_order, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView)view.findViewById(R.id.expandable_my_order_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        linearLayout = (LinearLayout)view.findViewById(R.id.no_content_ll);
        btnAddOrder = (Button) view.findViewById(R.id.btn_no_content_make_order);
        btnAddOrder.setOnClickListener(this);
        initMyOrder();
    }

    public void initMyOrder() {
        loadingView = LoadingDialog.view(getActivity().getFragmentManager());
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<MyOrderResponse> call = apiInterface.getMyOrder();
        loadingView.showLoading();
        call.enqueue(new Callback<MyOrderResponse>() {
            @Override
            public void onResponse(Call<MyOrderResponse> call, Response<MyOrderResponse> response) {
                MyOrderResponse myOrderResponse = response.body();
                DataMyOrder dataMyOrder = myOrderResponse.getDataMyOrders().get(0);
                List<InfoMyOrder> infoMyOrders = dataMyOrder.getInfoMyOrders();
                boolean success = dataMyOrder.isSuccess();
                if(success) {
                    if(infoMyOrders.size() == 0) {
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                    recyclerView.setAdapter(new MyOrdersAdapter(getActivity(),infoMyOrders, getFragmentManager()));
                    loadingView.hideLoading();
                }else {
                    Toast.makeText(getActivity(),"error",Toast.LENGTH_LONG).show();
                    loadingView.hideLoading();
                }
            }

            @Override
            public void onFailure(Call<MyOrderResponse> call, Throwable t) {
                Log.i("ssss",t.toString());
                Toast.makeText(getActivity(),t.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_no_content_make_order:
                Fragment fragment = new MakeOrderFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container_frame,fragment);
                ft.addToBackStack(null);
                ft.commit();
                break;
        }
    }
}
