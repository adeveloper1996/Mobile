package com.retrofit.mobile.fragment;


import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.retrofit.mobile.R;
import com.retrofit.mobile.activity.MakeOrderSubCategoryActivity;
import com.retrofit.mobile.model.DataSubCategory;
import com.retrofit.mobile.model.InfoCategory;
import com.retrofit.mobile.model.InfoSubCategory;
import com.retrofit.mobile.model.MakeOrder;
import com.retrofit.mobile.response.SubCategoryResponse;
import com.retrofit.mobile.rest.ApiClient;
import com.retrofit.mobile.rest.ApiInterface;
import com.retrofit.mobile.utils.dialog.LoadingDialog;
import com.retrofit.mobile.utils.dialog.LoadingView;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MakeOrderFragment extends Fragment implements View.OnClickListener{
      private RadioGroup radioGroup;
      private RadioButton rBtnNewPhone;
      private RadioButton rBtnOldPhone;
      private Button btnMobilePhone;
      private Button btnTablet;
      private Button btnPhone;
      private LoadingView loadingView;
      private List<InfoCategory> category;

    public MakeOrderFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_make_order, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        radioGroup =(RadioGroup) view.findViewById(R.id.rbtn_group_make_order);
        rBtnNewPhone = (RadioButton) view.findViewById(R.id.rBtn_make_order_new);
        rBtnOldPhone = (RadioButton) view.findViewById(R.id.rBtn_make_order_old);
        btnMobilePhone = (Button) view.findViewById(R.id.btn_mobile_phone);
        btnTablet = (Button) view.findViewById(R.id.btn_tablet);
        btnPhone = (Button) view.findViewById(R.id.btn_phone);
        MakeOrder.setSellerOrBuyer(0);

        Realm realm = Realm.getDefaultInstance();
        RealmResults<InfoCategory> infoCategories = realm.where(InfoCategory.class).findAll();
        category = infoCategories;
        realm.copyToRealm(infoCategories);
        btnMobilePhone.setText(infoCategories.get(0).getName());
        btnTablet.setText(infoCategories.get(1).getName());
        btnPhone.setText(infoCategories.get(2).getName());
        btnMobilePhone.setOnClickListener(this);
        btnTablet.setOnClickListener(this) ;
        btnPhone.setOnClickListener(this);
        rBtnNewPhone.setOnClickListener(this);
        rBtnOldPhone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_mobile_phone:
                if(rBtnNewPhone.isChecked() == true || rBtnOldPhone.isChecked() == true) {
                    loadingView = LoadingDialog.view(getActivity().getFragmentManager());
                    loadingView.showLoading();
                    MakeOrder.setSubcategory(1);
                    subMobileCategory();

                }else {
                    Toast.makeText(getActivity(), "Выберите состояния девайса", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_tablet:
                if(rBtnNewPhone.isChecked() == true || rBtnOldPhone.isChecked() == true) {
                    loadingView = LoadingDialog.view(getActivity().getFragmentManager());
                    loadingView.showLoading();
                    MakeOrder.setSubcategory(2);
                    subTabletCategory();

                }else {
                    Toast.makeText(getActivity(), "Выберите состояния девайса", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_phone:
                if(rBtnNewPhone.isChecked() == true || rBtnOldPhone.isChecked() == true) {
                    loadingView = LoadingDialog.view(getActivity().getFragmentManager());
                    loadingView.showLoading();
                    MakeOrder.setSubcategory(3);
                    subPhoneCategory();

                }else {
                    Toast.makeText(getActivity(), "Выберите состояния девайса", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.rBtn_make_order_new:
                rBtnNewPhone.setChecked(true);
                rBtnOldPhone.setChecked(false);
                MakeOrder.setNewOld("Новый");
                MakeOrder.setStatusStade("1");
                break;
            case  R.id.rBtn_make_order_old:
                rBtnNewPhone.setChecked(false);
                rBtnOldPhone.setChecked(true);
                MakeOrder.setNewOld("Б/У");
                MakeOrder.setStatusStade("0");
                break;
        }
    }

    private  void subMobileCategory() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SubCategoryResponse> call = apiInterface.getSubCategory("1");
        call.enqueue(new Callback<SubCategoryResponse>() {
            @Override
            public void onResponse(Call<SubCategoryResponse> call, Response<SubCategoryResponse> response) {
                SubCategoryResponse subCategoryResponse = response.body();
                DataSubCategory dataSubCategory = subCategoryResponse.getSubCategories().get(0);
                boolean success = dataSubCategory.isSuccess();
                List<InfoSubCategory> infoSubCategories = dataSubCategory.getInfoSubCategories();
                if(success) {
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.delete(InfoSubCategory.class);
                    realm.insert( infoSubCategories);
                    realm.commitTransaction();
                    Intent intent = new Intent(getActivity().getApplicationContext(), MakeOrderSubCategoryActivity.class);
                    intent.putExtra("subcategory", MakeOrder.getSubcategory());
                    Log.i("sss", "catId" + MakeOrder.getSubcategory());
                    startActivity(intent);
                    loadingView.hideLoading();
                }else {
                    Log.i("sss", "error");
                }
            }

            @Override
            public void onFailure(Call<SubCategoryResponse> call, Throwable t) {

            }
        });
    }

    private void subTabletCategory() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SubCategoryResponse> call = apiInterface.getSubCategory("2");
        call.enqueue(new Callback<SubCategoryResponse>() {
            @Override
            public void onResponse(Call<SubCategoryResponse> call, Response<SubCategoryResponse> response) {
                SubCategoryResponse subCategoryResponse = response.body();
                DataSubCategory dataSubCategory = subCategoryResponse.getSubCategories().get(0);
                boolean success = dataSubCategory.isSuccess();
                List<InfoSubCategory> infoSubCategories = dataSubCategory.getInfoSubCategories();
                if(success) {
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.delete(InfoSubCategory.class);
                    realm.insert( infoSubCategories);
                    realm.commitTransaction();
                    Intent intent = new Intent(getActivity().getApplicationContext(), MakeOrderSubCategoryActivity.class);
                    intent.putExtra("subcategoty", MakeOrder.getSubcategory());
                    Log.i("sss", "catId" + MakeOrder.getSubcategory());
                    startActivity(intent);
                    loadingView.hideLoading();
                }else {
                    Log.i("sss", "error");
                }
            }

            @Override
            public void onFailure(Call<SubCategoryResponse> call, Throwable t) {

            }
        });
    }

    private void subPhoneCategory() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SubCategoryResponse> call = apiInterface.getSubCategory("3");
        call.enqueue(new Callback<SubCategoryResponse>() {
            @Override
            public void onResponse(Call<SubCategoryResponse> call, Response<SubCategoryResponse> response) {
                SubCategoryResponse subCategoryResponse = response.body();
                DataSubCategory dataSubCategory = subCategoryResponse.getSubCategories().get(0);
                boolean success = dataSubCategory.isSuccess();
                List<InfoSubCategory> infoSubCategories = dataSubCategory.getInfoSubCategories();
                if(success) {
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.delete(InfoSubCategory.class);
                    realm.insert( infoSubCategories);
                    realm.commitTransaction();
                    Intent intent = new Intent(getActivity().getApplicationContext(), MakeOrderSubCategoryActivity.class);
                    intent.putExtra("subcategoty", MakeOrder.getSubcategory());
                    Log.i("sss", "catId" + MakeOrder.getSubcategory());
                    startActivity(intent);
                    loadingView.hideLoading();
                }else {
                    Log.i("sss", "error");
                }
            }

            @Override
            public void onFailure(Call<SubCategoryResponse> call, Throwable t) {

            }
        });
    }

}
