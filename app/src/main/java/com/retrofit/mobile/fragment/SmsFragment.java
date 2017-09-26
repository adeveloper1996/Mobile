package com.retrofit.mobile.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.retrofit.mobile.R;
import com.retrofit.mobile.adapter.TabsPagerFragmentAdapter;

public class SmsFragment extends Fragment {
    private ViewPager viewPager;
    private TabLayout tabLayout;


    public SmsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sms, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTabs(view);
    }

    private void initTabs(View view) {
        viewPager = (ViewPager)view.findViewById(R.id.view_pager_sms);
        TabsPagerFragmentAdapter adapter = new TabsPagerFragmentAdapter(getActivity(),getActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout)view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

    }
}
