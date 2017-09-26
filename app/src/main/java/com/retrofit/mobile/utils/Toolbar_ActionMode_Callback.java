package com.retrofit.mobile.utils;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.retrofit.mobile.R;
import com.retrofit.mobile.adapter.MyProductsAdapter;
import com.retrofit.mobile.fragment.ArchiveFragment;
import com.retrofit.mobile.fragment.MyProductFragment;
import com.retrofit.mobile.model.InfoArchive;
import com.retrofit.mobile.model.InfoMyTovar;

import java.util.List;

/**
 * Created by Nursultan on 13.09.2017.
 */

public class Toolbar_ActionMode_Callback implements ActionMode.Callback {
    private Context context;
    private ArchivesAdapter recyclerView_adapter;
    private MyProductsAdapter myProductsAdapter;
    private List<InfoArchive> message_models;
    private List<InfoMyTovar> myTovars;
    private int type;

    public Toolbar_ActionMode_Callback(Context context, ArchivesAdapter recyclerView_adapter, List<InfoArchive> message_models, int type) {
        this.context = context;
        this.recyclerView_adapter = recyclerView_adapter;
        this.message_models = message_models;
        this.type = type;
    }

    public Toolbar_ActionMode_Callback(Context context, MyProductsAdapter myProductsAdapter, List<InfoMyTovar> myTovars, int type) {
        this.context = context;
        this.myProductsAdapter = myProductsAdapter;
        this.myTovars = myTovars;
        this.type = type;
    }



    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_delete, menu);//Inflate the menu over action mode
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        if (Build.VERSION.SDK_INT < 11) {
            MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_delete), MenuItemCompat.SHOW_AS_ACTION_NEVER);
        } else {
            menu.findItem(R.id.action_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                //If current fragment is recycler view fragment
                if(type == 1) {
                    ArchiveFragment recyclerFragment = new ArchiveFragment();//Get recycler view fragment
                    if (recyclerFragment != null) {
                        //recyclerFragment.deleteRows(mode);
                    }
                    break;
                }
                if(type == 2){
                    MyProductFragment productFragment = new MyProductFragment();
                    if(productFragment != null) {
                        productFragment.deleteRows(mode);
                    }
                    break;
                }
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
//        if(type == 1) {
//            recyclerView_adapter.removeSelection();  // remove selection
//            ArchiveFragment recyclerFragment = new ArchiveFragment();//Get recycler fragment
//            if (recyclerFragment != null)
//                recyclerFragment.setNullToActionMode();//Set action mode null
//        }
        if(type == 2){
            myProductsAdapter.removeSelection();
            MyProductFragment productFragment = new MyProductFragment();
            if (productFragment != null)
                productFragment.setNullToActionMode();
        }
    }
}
