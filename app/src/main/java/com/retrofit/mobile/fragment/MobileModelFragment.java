package com.retrofit.mobile.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.retrofit.mobile.R;
import com.retrofit.mobile.activity.OrderSelllerProductActivity;
import com.retrofit.mobile.activity.OrderTimeActivity;
import com.retrofit.mobile.adapter.MultiChooseOrderModelAdapter;
import com.retrofit.mobile.adapter.OrderModelAdapter;
import com.retrofit.mobile.model.InfoModel;
import com.retrofit.mobile.model.MakeOrder;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MobileModelFragment extends Fragment {
    private RecyclerView recyclerView;
    private Button btnNextModel;

    public MobileModelFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mobile_model, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_order_model);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Realm realm = Realm.getDefaultInstance();
        RealmResults<InfoModel> infoModels = realm.where(InfoModel.class).findAll();
        List<InfoModel> infoModelList = infoModels;
        realm.copyToRealm(infoModelList);
        Log.i("sss", "" + infoModelList.get(0).isSelected());

        Log.i("ssss","multiorsingle" + MakeOrder.getSellerOrBuyer());
        if(MakeOrder.getSellerOrBuyer() == 0) {
            recyclerView.setAdapter(new OrderModelAdapter(infoModelList, R.layout.item_recycler_order_mark, getActivity().getApplicationContext()));
        }else {
            recyclerView.setAdapter(new MultiChooseOrderModelAdapter(infoModelList, R.layout.item_recycler_order_multi_chosoe, getActivity().getApplicationContext()));
        }

        btnNextModel = (Button)view.findViewById(R.id.btn_next_model);
        btnNextModel.setOnClickListener(v -> {
            if(MakeOrder.getSellerOrBuyer() == 0) {
                Intent intent = new Intent(getActivity(), OrderTimeActivity.class);
                startActivity(intent);
            }else{
                startActivity(new Intent(getActivity(),OrderSelllerProductActivity.class));
            }
        });
    }
}

