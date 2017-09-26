package com.retrofit.mobile.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.retrofit.mobile.fragment.ArchiveMessageFragment;
import com.retrofit.mobile.fragment.MessageFragment;

public class TabsPagerFragmentAdapter extends FragmentPagerAdapter {

    private String[] tabs;
    private Context context;

    public TabsPagerFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        tabs = new String[] {"Сообщение", "Архив"};
    }



    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:

                return MessageFragment.getInstance();
            case 1:
                return ArchiveMessageFragment.getInstance();
        }
        return null;
    }


    @Override
    public int getCount() {
        return tabs.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
}
