package com.retrofit.mobile.fragment;


import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.retrofit.mobile.R;
import com.retrofit.mobile.model.DataChangeTime;
import com.retrofit.mobile.model.MyOrderId;
import com.retrofit.mobile.response.ChangeTimeResponse;
import com.retrofit.mobile.rest.ApiClient;
import com.retrofit.mobile.rest.ApiInterface;
import com.retrofit.mobile.utils.TimeDialog;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsAuctionDialogFragnment extends DialogFragment {
    private View form = null;
    private Button btnSave;
    private Button btnCancel;
    private Button btnChange;
    private String idAucionTime;
    private TextView txtHour;
    private TextView txtMin;
    private TextView txtSec;
    private Calendar now;
    private TimeDialog timeDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i("ssss", "" + 123);
        idAucionTime = MyOrderId.getOrderId();
        form = getActivity().getLayoutInflater().inflate(R.layout.fragment_settings_auction_dialog_fragnment, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        btnSave = (Button)form.findViewById(R.id.btn_setting_save_auction_time);
        btnCancel  = (Button)form.findViewById(R.id.btn_setting_cancel_auction_time);
        btnChange = (Button)form.findViewById(R.id.btn_dialog_setting_time_change);
        txtHour = (TextView)form.findViewById(R.id.txtHour1);
        txtMin = (TextView)form.findViewById(R.id.txtMin1);
        txtSec = (TextView)form.findViewById(R.id.txtSec1);

        Context context = getActivity();
        SharedPreferences sharedPreferences = context.getSharedPreferences("timeChange", MODE_PRIVATE);
        String day = sharedPreferences.getString("day","00");
        String hour = sharedPreferences.getString("hour","00");
        String min = sharedPreferences.getString("min","00");

        if(day != null && hour != null && min != null) {
            txtHour.setText(day);
            txtMin.setText(":"+hour);
            txtSec.setText(":"+min);
            sharedPreferences.edit().clear().apply();
        }

        btnChange.setOnClickListener(v -> {
            timeDialog = new TimeDialog();
            Bundle bundle = new Bundle();
            bundle.putInt("timeChange", 1);
            timeDialog.setArguments(bundle);
            timeDialog.show(getActivity().getSupportFragmentManager(), "timeChange");
            dismiss();

//            MyTimePickerDialog pickerDialog = new MyTimePickerDialog(getActivity().getApplication(),new MyTimePickerDialog.OnTimeSetListener() {
//                @Override
//                public void onTimeSet(TimePicker view, int hourOfDay, int minute, int seconds) {
//                    txtHour.setText(String.format("%02d", hourOfDay));
//                    txtMin.setText(":" + String.format("%02d", minute));
//                    txtSec.setText(":" + String.format("%02d", seconds));
//                }
//            }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND), true);
//            pickerDialog.show();
        });


        btnSave.setOnClickListener(v -> {
            String end_time = String.valueOf(timeSecond());
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<ChangeTimeResponse> call = apiInterface.changeTime(idAucionTime,end_time);
            call.enqueue(new Callback<ChangeTimeResponse>() {
                @Override
                public void onResponse(Call<ChangeTimeResponse> call, Response<ChangeTimeResponse> response) {
                    ChangeTimeResponse changeTimeResponse = response.body();
                    DataChangeTime time = changeTimeResponse.getDataChangeTimes().get(0);
                    boolean success = time.isSuccess();
                    if (success){
                        Toast.makeText(getActivity(),"Время изменена",Toast.LENGTH_LONG).show();
                        getDialog().cancel();
                    }
                    else {
                        String error = time.getErrors().get(0);
                        Log.i("ssss",error);
                        Toast.makeText(getActivity(),error,Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ChangeTimeResponse> call, Throwable t) {
                    Log.i("ssss",t.toString());
                    Toast.makeText(getActivity(),t.toString(),Toast.LENGTH_LONG).show();
                }
            });
        });

        btnCancel.setOnClickListener(v -> {
            getDialog().cancel();
        });

        return(builder.setView(form).create());
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        Log.i("ssss",""+1234);
        super.onResume();
    }

    private long timeSecond() {
        String hou = txtHour.getText().toString();
        String str1 = hou.replaceAll(":", "");

        String minn = txtMin.getText().toString();
        String str2 = minn.replaceAll(":", "");

        String secc = txtSec.getText().toString();
        String str3 = secc.replaceAll(":", "");

        int hour = Integer.parseInt(str1);
        int min = Integer.parseInt(str2);
        int sec = Integer.parseInt(str3);

        Log.i("ssss", ""+hour);
        Log.i("ssss", ""+min);
        Log.i("ssss", ""+sec);

        long allsecond = (hour*86400) + (min*3600) + (sec*60);

        return allsecond;
    }
}
