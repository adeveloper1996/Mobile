package com.retrofit.mobile.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.retrofit.mobile.R;
import com.retrofit.mobile.utils.RoosterConnection;
import com.retrofit.mobile.utils.RoosterConnectionService;

import co.devcenter.androiduilibrary.ChatView;
import co.devcenter.androiduilibrary.ChatViewEventListener;
import co.devcenter.androiduilibrary.SendButton;

public class ChatActivity extends AppCompatActivity {
   private static final String TAG = "ChatActivity";

    private String contactJid;
    private ChatView mChatView;
    private SendButton mSendButton;
    private BroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mChatView = (ChatView) findViewById(R.id.rooster_chat_view);
        mChatView.setEventListener(new ChatViewEventListener() {
            @Override
            public void userIsTyping() {

            }

            @Override
            public void userHasStoppedTyping() {

            }
        });

        mSendButton = mChatView.getSendButton();
        mSendButton.setOnClickListener(v -> {

            if (RoosterConnectionService.getState().equals(RoosterConnection.ConnectionState.CONNECTED)) {
                Log.d(TAG, "The client is connected to the server,Sendint Message");
                //Send the message to the server

                Intent intent = new Intent(RoosterConnectionService.SEND_MESSAGE);
                intent.putExtra(RoosterConnectionService.BUNDLE_MESSAGE_BODY, mChatView.getTypedString());
                intent.putExtra(RoosterConnectionService.BUNDLE_TO, contactJid);

                sendBroadcast(intent);

                //Update the chat view.
                mChatView.sendMessage();
            } else {
                Toast.makeText(getApplicationContext(), "Client not connected to server ,Message not sent!", Toast.LENGTH_LONG).show();
            }
        });

        Intent intent = getIntent();
        contactJid = intent.getStringExtra("EXTRA_CONTACT_JID");
        setTitle(contactJid);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                switch (action)
                {
                    case RoosterConnectionService.NEW_MESSAGE:
                        String from = intent.getStringExtra(RoosterConnectionService.BUNDLE_FROM_JID);
                        String body = intent.getStringExtra(RoosterConnectionService.BUNDLE_MESSAGE_BODY);

                        if ( from.equals(contactJid))
                        {
                            mChatView.receiveMessage(body);

                        }else
                        {
                            Log.d(TAG,"Got a message from jid :"+from);
                        }

                        return;
                }

            }
        };

        IntentFilter filter = new IntentFilter(RoosterConnectionService.NEW_MESSAGE);
        registerReceiver(mBroadcastReceiver,filter);


    }

}
