package com.retrofit.mobile.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.retrofit.mobile.MyAdvertActiveFragment;
import com.retrofit.mobile.MyAdvertArchiveFragment;

/**
 * Created by Nursultan on 08.09.2017.
 */

public class TabsPagerMyAdvertAdapter extends FragmentPagerAdapter {

    private String[] tabs;
    private Context context;

    public TabsPagerMyAdvertAdapter(Context context,FragmentManager fm) {
        super(fm);
        this.context = context;
        tabs = new String[]{
                "Активный",
                "Архив",
        };
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return MyAdvertActiveFragment.getInstance();
            case 1:
                return MyAdvertArchiveFragment.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabs.length;
    }
}
