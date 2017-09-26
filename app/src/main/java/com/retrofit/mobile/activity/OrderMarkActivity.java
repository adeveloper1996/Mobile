package com.retrofit.mobile.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;

import com.retrofit.mobile.R;
import com.retrofit.mobile.fragment.MobileMarkFragment;
import com.retrofit.mobile.model.MakeOrder;

public class OrderMarkActivity extends AppCompatActivity {

    private Fragment fragment;
    private Button btnNext;
    private String markIdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_mark);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initFragment() {
        if(getIntent() != null) {
            Bundle extras = getIntent().getExtras();
            int mobilesubcat = extras.getInt("mobile");

            if(mobilesubcat == 1) {
                fragment = new MobileMarkFragment();
                setTitle("Смартфоны");
            }
            if(mobilesubcat == 2) {
                fragment = new MobileMarkFragment();
            }
            if(mobilesubcat == 3) {
                fragment = new MobileMarkFragment();
            }
            initFragmentTransition(fragment);
        }
    }

    private void initFragmentTransition(Fragment fragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame_order_mark,fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initFragment();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                Intent intent = new Intent(this,MakeOrderSubCategoryActivity.class);
                intent.putExtra("subcategory", MakeOrder.getSubcategory());
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
