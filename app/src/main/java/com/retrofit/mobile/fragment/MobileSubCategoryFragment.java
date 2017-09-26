package com.retrofit.mobile.fragment;


import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.retrofit.mobile.R;
import com.retrofit.mobile.activity.OrderMarkActivity;
import com.retrofit.mobile.model.DataMark;
import com.retrofit.mobile.model.InfoCategory;
import com.retrofit.mobile.model.InfoMark;
import com.retrofit.mobile.model.MakeOrder;
import com.retrofit.mobile.response.MarkResponse;
import com.retrofit.mobile.rest.ApiClient;
import com.retrofit.mobile.rest.ApiInterface;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MobileSubCategoryFragment extends Fragment implements View.OnClickListener{
    private Button btnSmartPhone;
    private Button btnAccessoriesPhone;
    private Button btnSportBraslets;
    private String subcat1;
    private String subcat2;
    private String subcat3;
    private List<InfoCategory> infoCategory;


    public MobileSubCategoryFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mobile_sub_category, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSmartPhone = (Button) view.findViewById(R.id.btn_smart_phone);
        btnAccessoriesPhone = (Button) view.findViewById(R.id.btn_accessories_phone);
        btnSportBraslets  = (Button) view.findViewById(R.id.btn_sport_braslet);
        btnSmartPhone.setOnClickListener(this);
        btnAccessoriesPhone.setOnClickListener(this);
        btnSportBraslets.setOnClickListener(this);

        initButton();
    }

    private void initButton() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<InfoCategory> infoCategories = realm.where(InfoCategory.class).findAll();
        realm.copyToRealm(infoCategories);
        infoCategory = infoCategories;
        btnSmartPhone.setText(infoCategories.get(0).getName());
        subcat1 = infoCategories.get(0).getId();
        btnAccessoriesPhone.setText(infoCategories.get(1).getName());
        subcat2 = infoCategories.get(1).getId();
        btnSportBraslets.setText(infoCategories.get(2).getName());
        subcat3 = infoCategories.get(2).getId();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_smart_phone:
                MakeOrder.setMark(1);
                clickSmartPhone();
                break;
            case R.id.btn_accessories_phone:
                MakeOrder.setMark(2);
                clickSmartPhone();
                break;
            case R.id.btn_sport_braslet:
                MakeOrder.setMark(3);
                clickSmartPhone();
                break;
        }
    }

    private void clickSmartPhone() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<MarkResponse> call = apiInterface.getMark();
        call.enqueue(new Callback<MarkResponse>() {
            @Override
            public void onResponse(Call<MarkResponse> call, Response<MarkResponse> response) {
                MarkResponse markResponse = response.body();
                DataMark dataMark = markResponse.getDataMarks().get(0);
                boolean success = dataMark.isSuccess();
                List<InfoMark> infoMarks = dataMark.getInfoMarks();
                if(success) {
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(infoMarks);
                    realm.commitTransaction();
                    Intent intent = new Intent(getActivity().getApplicationContext(), OrderMarkActivity.class);
                    intent.putExtra("mobile", MakeOrder.getMark());
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<MarkResponse> call, Throwable t) {

            }
        });
    }
}
