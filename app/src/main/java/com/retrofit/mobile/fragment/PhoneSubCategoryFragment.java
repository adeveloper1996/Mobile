package com.retrofit.mobile.fragment;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.retrofit.mobile.R;
import com.retrofit.mobile.model.InfoSubCategory;

import io.realm.Realm;
import io.realm.RealmResults;

public class PhoneSubCategoryFragment extends Fragment {
    private Button btnRadioPhone;
    private Button btnProvPhone;
    private Button btnIpPhone;
    private String subcat1;
    private String subcat2;
    private String subcat3;

    public PhoneSubCategoryFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_phone_sub_category, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnIpPhone = (Button)view.findViewById(R.id.btn_ip_phone);
        btnRadioPhone = (Button)view.findViewById(R.id.btn_radio_phone);
        btnProvPhone = (Button)view.findViewById(R.id.btn_prov_phone);

        Realm realm = Realm.getDefaultInstance();
        RealmResults<InfoSubCategory> infoSubCategories = realm.where(InfoSubCategory.class).findAll();
        realm.copyToRealm(infoSubCategories);
        if(infoSubCategories.size() == 2) {
            btnIpPhone.setVisibility(View.GONE);
            btnRadioPhone.setText(infoSubCategories.get(0).getName());
            subcat1 = infoSubCategories.get(0).getId();
            btnProvPhone.setText(infoSubCategories.get(1).getName());
            subcat2 = infoSubCategories.get(1).getId();
        }
        else {
            btnRadioPhone.setText(infoSubCategories.get(0).getName());
            subcat1 = infoSubCategories.get(0).getId();
            btnProvPhone.setText(infoSubCategories.get(1).getName());
            subcat2 = infoSubCategories.get(1).getId();
            btnIpPhone.setText(infoSubCategories.get(2).getName());
            subcat3 = infoSubCategories.get(2).getId();
        }
    }
}
