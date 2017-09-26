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
import com.retrofit.mobile.activity.AddAdvertActivity;
import com.retrofit.mobile.activity.AdvertActivity;
import com.retrofit.mobile.activity.MessageActivity;
import com.retrofit.mobile.activity.MyAdvertActivity;
import com.retrofit.mobile.activity.MyLovlelyActivity;


public class AddAdvertFragment extends Fragment implements  View.OnClickListener{
    private Button btnAddadvert;
    private Button btnMyAdverts;
    private Button btnShowAdvert;
    private Button btnLovely;

    public AddAdvertFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_advert, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnAddadvert = (Button)view.findViewById(R.id.btn_add_advert);
        btnShowAdvert = (Button)view.findViewById(R.id.btn_show_advert);
        btnMyAdverts = (Button)view.findViewById(R.id.btn_show_my_advert);
        btnLovely = (Button)view.findViewById(R.id.btn_my_lovely_advert);
        btnAddadvert.setOnClickListener(this);
        btnShowAdvert.setOnClickListener(this);
        btnMyAdverts.setOnClickListener(this);
        btnLovely.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_advert:
                startActivity(new Intent(getActivity(), AddAdvertActivity.class));
                break;
            case R.id.btn_show_my_advert:
                startActivity(new Intent(getActivity(), MyAdvertActivity.class));
                break;
            case R.id.btn_show_advert:
                startActivity(new Intent(getActivity(), AdvertActivity.class));
                break;
            case R.id.btn_message_advert:
                startActivity(new Intent(getActivity(), MessageActivity.class));
                break;
            case R.id.btn_my_lovely_advert:
                startActivity(new Intent(getActivity(), MyLovlelyActivity.class));
                break;
        }
    }
}

