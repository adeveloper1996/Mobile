package com.retrofit.mobile.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.retrofit.mobile.R;
import com.retrofit.mobile.model.DataSendSms;
import com.retrofit.mobile.response.SendsmsResponse;
import com.retrofit.mobile.rest.ApiClient;
import com.retrofit.mobile.rest.ApiInterface;
import com.retrofit.mobile.utils.Encryption;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

public class SignUpFragment extends Fragment {
     private EditText etxtNumber;
     private Button btnNext;
     private FrameLayout frameLayout;
     private Fragment fragment;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        frameLayout = (FrameLayout) view.findViewById(R.id.content_frame);
        etxtNumber = (EditText) view.findViewById(R.id.edit_sign_up_number);
        decore(etxtNumber);
        btnNext = (Button) view.findViewById(R.id.btn_signup_next);
        Encryption encryption = new Encryption();

        btnNext.setOnClickListener(v -> {
            if(etxtNumber.getText().length() != 0) {
                String phone = etxtNumber.getText().toString();
                String token = encryption.md5Custom("MURAT"+phone+"MURAT");
                Log.i("ssss",token);
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<SendsmsResponse> call = apiInterface.sendsms(phone, token);
                call.enqueue(new Callback<SendsmsResponse>() {
                    @Override
                    public void onResponse(Call<SendsmsResponse> call, Response<SendsmsResponse> response) {
                        SendsmsResponse sendsmsResponse = response.body();
                        DataSendSms dataSendSms = sendsmsResponse.getData().get(0);
                        boolean success = dataSendSms.isSuccess();
                        Log.i("sss", "lll"+ success);
                        if(success) {
                            fragment = new SignUpFragment2();
                            Bundle bundle = new Bundle();
                            bundle.putString("phone", phone);
                            fragment.setArguments(bundle);
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, fragment);
                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            ft.commit();
                        }
                        else {
                            String errors = dataSendSms.getErrors().get(0);
                            Toast.makeText(getActivity(),errors,Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SendsmsResponse> call, Throwable t) {
                        Toast.makeText(getActivity(),t.toString(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }


    private void decore (EditText editText){
        Slot[] slots = new UnderscoreDigitSlotsParser().parseSlots("(___)___-____");
        FormatWatcher formatWatcher = new MaskFormatWatcher(MaskImpl.createTerminated(slots));
        formatWatcher.installOn(editText);
    }
}
