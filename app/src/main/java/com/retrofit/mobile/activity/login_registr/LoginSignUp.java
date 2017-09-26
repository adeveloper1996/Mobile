package com.retrofit.mobile.activity.login_registr;

import android.content.Intent;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.retrofit.mobile.R;
import com.retrofit.mobile.fragment.LoginFragment;
import com.retrofit.mobile.fragment.SignUpFragment;

public class LoginSignUp extends AppCompatActivity {

    private View view;
    private Button btnLogin;
    private Button btnSignUp;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);

        Intent intent = getIntent();
        fragment = new LoginFragment();
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnSignUp = (Button) findViewById(R.id.btn_signup);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        btnSignUp.setTextColor(getResources().getColor(R.color.colorBtnNotPress));
        ft.commit();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                fragment = new LoginFragment();
                btnSignUp.setTextColor(getResources().getColor(R.color.colorBtnNotPress));
                btnLogin.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.btn_signup:
                fragment = new SignUpFragment();
                btnLogin.setTextColor(getResources().getColor(R.color.colorBtnNotPress));
                btnSignUp.setTextColor(getResources().getColor(R.color.white));
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }
}
