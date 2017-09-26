package com.retrofit.mobile.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.retrofit.mobile.R;
import com.retrofit.mobile.model.DataCloseAuction;
import com.retrofit.mobile.model.MyOrderId;
import com.retrofit.mobile.response.ClouseActionResponse;
import com.retrofit.mobile.rest.ApiClient;
import com.retrofit.mobile.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CloseAuctionDialogFragment extends DialogFragment {
    private View form=null;
    private Button btnYes;
    private Button btnNo;
    private Button btnClose;
    private Button btnSettings;
    private String idAucion;
    private int position;
    private RecyclerView recyclerView;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        idAucion = MyOrderId.getOrderId();
        position = MyOrderId.getPosOrder();
        form = getActivity().getLayoutInflater().inflate(R.layout.fragment_close_auction_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        recyclerView = (RecyclerView)form.findViewById(R.id.expandable_my_order_recycler);
        btnYes = (Button)form.findViewById(R.id.btn_dialog_close_auction_yes);
        btnNo  = (Button)form.findViewById(R.id.btn_dialog_close_auction_no);
        btnClose = (Button)getActivity().findViewById(R.id.btn_close_auction_my_order);
        btnSettings = (Button)getActivity().findViewById(R.id.btn_setting_my_order);

        btnYes.setOnClickListener(v -> {
            String status = "0";
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<ClouseActionResponse> call = apiInterface.closeAuction(idAucion, status);
            call.enqueue(new Callback<ClouseActionResponse>() {
                @Override
                public void onResponse(Call<ClouseActionResponse> call, Response<ClouseActionResponse> response) {
                    ClouseActionResponse clouseActionResponse = response.body();
                    DataCloseAuction dataCloseAuction = clouseActionResponse.getDataCloseAuctions().get(0);
                    boolean success = dataCloseAuction.isSuccess();

                    if(success) {
                        Log.i("ssss","" + success);
                        btnClose.setTextColor(getResources().getColor(R.color.red));
                        btnClose.setText("Торг закрыть");
                        btnClose.setClickable(false);
                        btnSettings.setClickable(false);
                        getDialog().cancel();
                    }else {
                        String error = dataCloseAuction.getErrors().get(0);
                        Log.i("ssss",error);
                    }
                }

                @Override
                public void onFailure(Call<ClouseActionResponse> call, Throwable t) {
                    Log.i("ssss",t.toString());
                }
            });
        });

        btnNo.setOnClickListener(v -> {
            getDialog().cancel();
        });

        return(builder.setView(form).create());

    }
}
