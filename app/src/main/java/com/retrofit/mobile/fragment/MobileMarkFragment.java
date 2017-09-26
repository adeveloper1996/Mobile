package com.retrofit.mobile.fragment;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;
import com.retrofit.mobile.R;
import com.retrofit.mobile.adapter.OrderMarkAdapter;
import com.retrofit.mobile.model.DataModel;
import com.retrofit.mobile.model.InfoMark;
import com.retrofit.mobile.model.InfoModel;
import com.retrofit.mobile.model.MakeOrder;
import com.retrofit.mobile.response.ModelResponse;
import com.retrofit.mobile.rest.ApiClient;
import com.retrofit.mobile.rest.ApiInterface;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class MobileMarkFragment extends Fragment {
    private RadioButton rBtnMark;
    private RecyclerView recyclerView;
    private Fragment fragment;
    private Button btnNext;
    private String markId;

    public MobileMarkFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mobile_mark, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_order_mark);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rBtnMark = (RadioButton) view.findViewById(R.id.rBtnMark);
        Realm realm = Realm.getDefaultInstance();
        RealmResults<InfoMark> infoMarks = realm.where(InfoMark.class).findAll();
        List<InfoMark> infoMarks1 = infoMarks;
        realm.copyToRealm(infoMarks1);
        Log.i("sss",infoMarks1.get(0).getName());

        recyclerView.setAdapter(new OrderMarkAdapter(infoMarks1,R.layout.item_recycler_order_mark, getActivity().getApplicationContext()));

        setBtnNextClick(view);
    }

    private void setBtnNextClick(View view) {
        btnNext = (Button) view.findViewById(R.id.btn_next_mark);
        btnNext.setOnClickListener(v->{
            fragment = new MobileModelFragment();
            markId = MakeOrder.getMarkId();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<ModelResponse> call = apiInterface.getModel(markId);
            call.enqueue(new Callback<ModelResponse>() {
                @Override
                public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                    ModelResponse modelResponse = response.body();
                    DataModel dataModel = modelResponse.getDataModels().get(0);
                    boolean success = dataModel.isSuccess();
                    List<InfoModel> infoModel = dataModel.getInfoModels();
                    if(success) {
                        if (infoModel != null) {
                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            realm.delete(InfoModel.class);
                            realm.insert(infoModel);
                            realm.commitTransaction();
                            initFragmentTransition(fragment);
                        } else {
                          Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(infoModel);
                            realm.commitTransaction();
                            initFragmentTransition(fragment);
                        }
                    } else {
                        Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ModelResponse> call, Throwable t) {
                    Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_LONG).show();
                }
            });
        });
    }
    private void initFragmentTransition(Fragment fragment) {
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame_order_mark,fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
