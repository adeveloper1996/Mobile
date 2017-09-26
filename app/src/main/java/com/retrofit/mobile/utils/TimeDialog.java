package com.retrofit.mobile.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.retrofit.mobile.R;
import com.retrofit.mobile.fragment.SettingsAuctionDialogFragnment;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Nursultan on 11.07.2017.
 */

public class TimeDialog extends DialogFragment implements DialogInterface.OnClickListener,NumberPicker.OnValueChangeListener{
    private View form = null;
    private NumberPicker num1, num2, num3;
    private TextView txtDay, txtHour, txtMinute;
    private int mCurrentHour = 0;
    private int mCurrentMinute = 0;
    private int mCurrentSeconds = 0;
    private int key;

    public TimeDialog() {}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(getArguments() != null) {
           key = (int) getArguments().get("timeChange");
            Log.i("sss","key"+key);
        }

        form = getActivity().getLayoutInflater().inflate(R.layout.time_picker_widget, null);
        AlertDialog.Builder builder = new  AlertDialog.Builder(getActivity());
        builder.setCancelable(false);

        num1 = (NumberPicker) form.findViewById(R.id.hour);
        num2 = (NumberPicker) form.findViewById(R.id.minute);
        num3 = (NumberPicker) form.findViewById(R.id.seconds);

        txtDay = (TextView)getActivity().findViewById(R.id.txtHour);
        txtHour = (TextView)getActivity().findViewById(R.id.txtMin);
        txtMinute = (TextView)getActivity().findViewById(R.id.txtSec);

        num1.setMinValue(0);
        num1.setMaxValue(7);

        num2.setMinValue(0);
        num2.setMaxValue(23);

        num3.setMinValue(0);
        num3.setMaxValue(60);

        num1.setOnValueChangedListener(this);
        num2.setOnValueChangedListener(this);
        num3.setOnValueChangedListener(this);

        num2.setFormatter(TWO_DIGIT_FORMATTER);
        num3.setFormatter(TWO_DIGIT_FORMATTER);

        num1.setWrapSelectorWheel(false);
        num2.setWrapSelectorWheel(false);
        num3.setWrapSelectorWheel(false);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(key == 0) {
                    txtDay.setText(Integer.toString(num1.getValue()));
                    txtHour.setText(":"+ String.format("%02d", num2.getValue()));
                    txtMinute.setText(":"+ String.format("%02d", num3.getValue()));
                }
                if(key == 1) {
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("tineChange", MODE_PRIVATE).edit();
                    editor.putString("day", String.valueOf(num1.getValue()));
                    editor.putString("hour", String.format("%02d", num2.getValue()));
                    editor.putString("min", String.format("%02d", num3.getValue()));
                    editor.apply();
                    new SettingsAuctionDialogFragnment().show(getFragmentManager(), "sett");
                }
                dialog.cancel();
            }
        });

        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setView(form).create();

        final  AlertDialog alert = builder.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        });

        return alert;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        switch (form.getId()){
            case R.id.hour:
                mCurrentHour = newVal;
                Log.d("ssss",""+mCurrentHour);
                break;
            case R.id.minute:
                mCurrentMinute = newVal;
                break;
            case R.id.seconds:
                mCurrentSeconds = newVal;
                break;
        }
    }

    public static final NumberPicker.Formatter TWO_DIGIT_FORMATTER = new NumberPicker.Formatter(){
        @Override
        public String format(int value) {
            return String.format("%02d",value);
        }
    };
}
