package com.retrofit.mobile.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.retrofit.mobile.R;
import com.retrofit.mobile.model.DataRegistration;
import com.retrofit.mobile.response.RegResponse;
import com.retrofit.mobile.rest.ApiClient;
import com.retrofit.mobile.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpFragment2 extends Fragment {
      private EditText etxtName;
      private EditText etxtPassword;
      private EditText etxtCode;
      private Button btnSignUp;
      private String phone;
      private Fragment fragment;


    public SignUpFragment2() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_fragment2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null) {
            phone = bundle.getString("phone");
        }

        etxtName = (EditText) view.findViewById(R.id.edit_sign_up_name);
        etxtPassword = (EditText) view.findViewById(R.id.edit_sign_up_password);
        etxtCode = (EditText) view.findViewById(R.id.edit_sign_up_code);
        etxtCode.setText("7878");

        btnSignUp = (Button) view.findViewById(R.id.btn_signup_signup);
        btnSignUp.setOnClickListener(v ->{

            String name = etxtName.getText().toString();
            String password = etxtPassword.getText().toString();
            String code = etxtCode.getText().toString();

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<RegResponse> call = apiInterface.register(phone, password, name, code);
            call.enqueue(new Callback<RegResponse>() {
                @Override
                public void onResponse(Call<RegResponse> call, Response<RegResponse> response) {
                    RegResponse regResponse = response.body();
                    DataRegistration dataRegistration = regResponse.getData().get(0);
                    boolean success = dataRegistration.isSuccess();
                    if(success) {
                        Toast.makeText(getActivity(),"Вы зарегистировались", Toast.LENGTH_LONG).show();
                        fragment = new LoginFragment();
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, fragment);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.commit();
                    } else {
                        String error = dataRegistration.getErrors().get(0);
                        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<RegResponse> call, Throwable t) {
                        Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
                }
            });
        });

    }
}
