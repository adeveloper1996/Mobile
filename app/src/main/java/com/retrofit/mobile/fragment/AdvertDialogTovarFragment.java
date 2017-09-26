package com.retrofit.mobile.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.retrofit.mobile.R;

public class AdvertDialogTovarFragment extends DialogFragment implements DialogInterface.OnClickListener{

    private View form=null;
    private RadioButton rBtnAll;
    private RadioButton rBtnNew;
    private RadioButton rBtnBu;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        form = getActivity().getLayoutInflater().inflate(R.layout.fragment_ad_vert_dialog_tovar, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        rBtnAll = (RadioButton)form.findViewById(R.id.rbtn_dialog_advert_all);
        rBtnNew = (RadioButton)form.findViewById(R.id.rbtn_dialog_advert_new);
        rBtnBu = (RadioButton)form.findViewById(R.id.rbtn_dialog_advert_bu);

        rBtnAll.setOnClickListener(v -> {
            rBtnAll.setChecked(true);
            rBtnNew.setChecked(false);
            rBtnBu.setChecked(false);
        });

        rBtnNew.setOnClickListener(v -> {
            rBtnAll.setChecked(false);
            rBtnNew.setChecked(true);
            rBtnBu.setChecked(false);
        });

        rBtnBu.setOnClickListener(v -> {
            rBtnAll.setChecked(false);
            rBtnNew.setChecked(false);
            rBtnBu.setChecked(true);
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

        if(rBtnAll.isChecked()) {
            TextView txtTovar = (TextView)getActivity().findViewById(R.id.txt_filter_tovar);
            txtTovar.setText("Все");
        }
        if(rBtnNew.isChecked()){
            TextView txtTovar = (TextView)getActivity().findViewById(R.id.txt_filter_tovar);
            txtTovar.setText("Новые");
        }

        if(rBtnBu.isChecked()){
            TextView txtTovar = (TextView)getActivity().findViewById(R.id.txt_filter_tovar);
            txtTovar.setText("Б/У");
        }

    }
}
