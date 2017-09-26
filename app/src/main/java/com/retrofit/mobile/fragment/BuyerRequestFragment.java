package com.retrofit.mobile.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.retrofit.mobile.R;
import com.retrofit.mobile.adapter.BuyerRequestsAdapter;
import com.retrofit.mobile.model.DataBuyerRequest;
import com.retrofit.mobile.model.InfoBuyerRequest;
import com.retrofit.mobile.response.RequestBuyerResponse;
import com.retrofit.mobile.rest.ApiClient;
import com.retrofit.mobile.rest.ApiInterface;
import com.retrofit.mobile.utils.dialog.LoadingDialog;
import com.retrofit.mobile.utils.dialog.LoadingView;

import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyerRequestFragment extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayout linearLayout;
    private LoadingView loadingView;
    private Realm realm;

    public BuyerRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buyer_request, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView)view.findViewById(R.id.expandable_buyer_request_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        linearLayout = (LinearLayout)view.findViewById(R.id.no_buyer_request_ll);
        loadingView = LoadingDialog.view(getActivity().getFragmentManager());

        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<RequestBuyerResponse> call = apiInterface.getBuyerRequest();
        call.enqueue(new Callback<RequestBuyerResponse>() {
            @Override
            public void onResponse(Call<RequestBuyerResponse> call, Response<RequestBuyerResponse> response) {
                RequestBuyerResponse requestBuyerResponse = response.body();
                DataBuyerRequest dataBuyerRequest = requestBuyerResponse.getBuyerRequests().get(0);
                List<InfoBuyerRequest> infoBuyerRequests = dataBuyerRequest.getInfoBuyerRequests();
                boolean success = dataBuyerRequest.isSuccess();

                if(success) {
                    if(infoBuyerRequests.size() == 0){
                        Log.i("ssss", ""+infoBuyerRequests.size());
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                    realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(infoBuyerRequests);
                    realm.commitTransaction();
                    recyclerView.setAdapter(new BuyerRequestsAdapter(getActivity(), infoBuyerRequests));
                    loadingView.hideLoading();
                }else {
                    Toast.makeText(getActivity(),"error",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RequestBuyerResponse> call, Throwable t) {
                Toast.makeText(getActivity(),t.toString(),Toast.LENGTH_LONG).show();
                loadingView.hideLoading();
            }
        });
    }

}
