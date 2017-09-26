package com.retrofit.mobile.fragment;


import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.retrofit.mobile.R;
import com.retrofit.mobile.activity.ForgotPasswordActivity;
import com.retrofit.mobile.activity.GuestActivity;
import com.retrofit.mobile.activity.MarketMobileActivity;
import com.retrofit.mobile.app.Config;
import com.retrofit.mobile.model.DataAuth;
import com.retrofit.mobile.model.InfoClient;
import com.retrofit.mobile.response.AuthResponse;
import com.retrofit.mobile.rest.ApiClient;
import com.retrofit.mobile.rest.ApiInterface;
import com.retrofit.mobile.utils.SessionManager;
import com.retrofit.mobile.utils.dialog.LoadingDialog;
import com.retrofit.mobile.utils.dialog.LoadingView;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

public class LoginFragment extends Fragment {

    private Button btnLogin;
    private Button btnGuest;
    private EditText eTxtUsername;
    private EditText eTxtPassword;
    private ImageButton imgPassword;
    private SessionManager session;
    private static final String TAG = "MainActivity";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String regId;
    private FormatWatcher formatWatcher;
    private FormatWatcher formatWatcher1;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    String message = intent.getStringExtra("message");
//                    Toast.makeText(getActivity().getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                }
            }
        };
        displayFirebaseRegId();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        session = new SessionManager(getActivity().getApplicationContext());
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgPassword = (ImageButton) view.findViewById(R.id.imgBtn_pass_forgot);
        eTxtUsername = (EditText) view.findViewById(R.id.edit_login_name);
        eTxtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                if (s.length() == 0){
//                    decoree(eTxtUsername);
//                }
            }
        });
        decore(eTxtUsername);
        eTxtPassword = (EditText) view.findViewById(R.id.edit_login_password);
        btnGuest = (Button) view.findViewById(R.id.btn_login_guest);
        imgPassword.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ForgotPasswordActivity.class));
        });
        btnGuest.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), GuestActivity.class);
            startActivity(intent);
        });
        btnLogin = (Button) view.findViewById(R.id.btn_login_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LoadingView loadingView = LoadingDialog.view(getActivity().getFragmentManager());
                loadingView.showLoading();

                String number = eTxtUsername.getText().toString();
                String password = eTxtPassword.getText().toString();
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<AuthResponse> call = apiInterface.auth(number, password, regId);
                call.enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        AuthResponse authorization = response.body();
                        Log.i("ssss", "head" + response.headers());
                        DataAuth dataAuth = authorization.getData().get(0);
                        boolean success = dataAuth.isSuccess();
                        InfoClient infoClient = dataAuth.getInfoClients();
                        if (infoClient != null) {
                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            realm.delete(InfoClient.class);
                            realm.insert(infoClient);
                            realm.commitTransaction();
                        } else {
                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(infoClient);
                            realm.commitTransaction();
                        }
                        if (success) {
                            loadingView.hideLoading();
                            session.setLoggedin(true);
//                            saveCredentialsAndLogin();
                            Intent intent = new Intent(getActivity(), MarketMobileActivity.class);
                            intent.putExtra("infoClient", infoClient);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            getActivity().finish();
                        } else {
                            Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
                            eTxtUsername.setText("");
                            eTxtPassword.setText("");
                        }

                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        loadingView.hideLoading();
                        Log.i("ssss", "" + t);
                        Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
                        eTxtUsername.setText("");
                        eTxtPassword.setText("");
                    }
                });
            }
        });
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        regId = pref.getString("regId", null);
        Log.i("baga", "Firebase reg id: " + regId);
//        if (!TextUtils.isEmpty(regId))
//            Toast.makeText(getActivity(),"Firebase Reg Id: " + regId, Toast.LENGTH_LONG).show();
//        else
//            Toast.makeText(getActivity(),"Firebase Reg Id is not received yet!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
       // NotificationUtils.clearNotifications(getActivity().getApplicationContext());
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private void decore(EditText editText) {
        Slot[] slots = new UnderscoreDigitSlotsParser().parseSlots("(___)___-____");
        formatWatcher = new MaskFormatWatcher(MaskImpl.createTerminated(slots));
        formatWatcher.installOn(editText);
    }

    private void decoree(EditText editText) {
        Slot[] slots = new UnderscoreDigitSlotsParser().parseSlots(" ");
        formatWatcher1 = new MaskFormatWatcher(MaskImpl.createTerminated(slots));
        formatWatcher1.installOn(editText);
    }
}
