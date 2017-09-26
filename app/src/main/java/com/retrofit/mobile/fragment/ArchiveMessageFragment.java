package com.retrofit.mobile.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.retrofit.mobile.R;

/**
 * Created by Nursultan on 11.09.2017.
 */

public class ArchiveMessageFragment extends Fragment{

    public static ArchiveMessageFragment getInstance() {
        Bundle args = new Bundle();
        ArchiveMessageFragment fragment = new ArchiveMessageFragment();
        fragment.setArguments(args);

        return fragment;
    }


    public ArchiveMessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_archive_message, container, false);
    }
}
