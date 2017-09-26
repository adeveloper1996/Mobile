package com.retrofit.mobile.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.retrofit.mobile.R;
import com.retrofit.mobile.adapter.TabsPagerMyAdvertAdapter;

public class MyAdvertActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_advert);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.view_pager_my_advert);
        TabsPagerMyAdvertAdapter adapter = new TabsPagerMyAdvertAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout)findViewById(R.id.tab_layout_my_advert);
        tabLayout.setupWithViewPager(viewPager);
    }
}
