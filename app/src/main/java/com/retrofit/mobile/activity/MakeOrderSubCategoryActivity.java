package com.retrofit.mobile.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.retrofit.mobile.R;
import com.retrofit.mobile.fragment.MobileSubCategoryFragment;
import com.retrofit.mobile.fragment.PhoneSubCategoryFragment;
import com.retrofit.mobile.fragment.TabletSubCategoryFragment;

public class MakeOrderSubCategoryActivity extends AppCompatActivity {

      private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order_sub_categoty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        if(getIntent() != null) {
            Bundle extras = getIntent().getExtras();
            int p = extras.getInt("subcategory");
            Log.i("sss", "" + p);
            if(p == 1) {
                 fragment = new MobileSubCategoryFragment();
                setTitle("Мобильные телефоны");
            }
            if(p == 2) {
                fragment = new TabletSubCategoryFragment();
                setTitle("Планшеты");
            }
            if(p == 3) {
                fragment = new PhoneSubCategoryFragment();
                setTitle("Телефоны");
            }
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame_sub_category,fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }
}
