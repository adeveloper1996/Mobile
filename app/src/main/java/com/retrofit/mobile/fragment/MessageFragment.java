package com.retrofit.mobile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.retrofit.mobile.R;
import com.retrofit.mobile.activity.ChatActivity;

public class MessageFragment extends Fragment{
    private RelativeLayout relativeLayout;

    public static MessageFragment getInstance() {
        Bundle args = new Bundle();
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public MessageFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        relativeLayout = (RelativeLayout)view.findViewById(R.id.sms_chat);
        relativeLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.putExtra("EXTRA_CONTACT_JID","nurislam@89.219.32.43");
            startActivity(intent);
        });

    }
}
