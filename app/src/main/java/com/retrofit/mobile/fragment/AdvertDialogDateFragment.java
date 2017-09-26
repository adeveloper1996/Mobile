package com.retrofit.mobile.fragment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;
import android.app.AlertDialog;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.retrofit.mobile.R;

public class AdvertDialogDateFragment extends DialogFragment implements DialogInterface.OnClickListener {
    private View form=null;
    private RadioButton rBtnDateNew;
    private RadioButton rBtnDateOld;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        form = getActivity().getLayoutInflater().inflate(R.layout.fragment_advert_dialog_date, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        rBtnDateNew = (RadioButton)form.findViewById(R.id.rbtn_dialog_advert_date_new);
        rBtnDateOld = (RadioButton)form.findViewById(R.id.rbtn_dialog_advert_date_old);

        rBtnDateNew.setOnClickListener(v -> {
            rBtnDateNew.setChecked(true);
            rBtnDateOld.setChecked(false);
        });

        rBtnDateOld.setOnClickListener(v -> {
            rBtnDateOld.setChecked(true);
            rBtnDateNew.setChecked(false);
        });

        builder.setPositiveButton("OK", this);
        builder.setNegativeButton("Отмена", null);

        builder.setView(form).create();

        final AlertDialog alert = builder.create();
        alert.setOnShowListener(dialog -> {
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        });

        return alert;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        if(rBtnDateNew.isChecked()){
            TextView txtDate = (TextView)getActivity().findViewById(R.id.txt_filter_date);
            txtDate.setText("Свежие");
        }

        if(rBtnDateOld.isChecked()){
            TextView txtDate = (TextView)getActivity().findViewById(R.id.txt_filter_date);
            txtDate.setText("Старые");
        }
    }
}