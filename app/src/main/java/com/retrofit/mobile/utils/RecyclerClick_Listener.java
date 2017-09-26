package com.retrofit.mobile.utils;

import android.view.View;

/**
 * Created by Nursultan on 12.09.2017.
 */

public interface RecyclerClick_Listener {

    /**
     * Interface for Recycler View Click listener
     **/

    void onClick(View view, int position);

    void onLongClick(View view, int position);
}