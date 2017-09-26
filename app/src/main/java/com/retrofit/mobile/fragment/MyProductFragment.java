package com.retrofit.mobile.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.retrofit.mobile.R;
import com.retrofit.mobile.adapter.MyProductsAdapter;
import com.retrofit.mobile.adapter.RecyclerTouchListener;
import com.retrofit.mobile.model.DataDeleteArchive;
import com.retrofit.mobile.model.DataMyTovar;
import com.retrofit.mobile.model.InfoMyTovar;
import com.retrofit.mobile.response.DeleteArchiveResponse;
import com.retrofit.mobile.response.MyTovarResponse;
import com.retrofit.mobile.rest.ApiClient;
import com.retrofit.mobile.rest.ApiInterface;
import com.retrofit.mobile.utils.RecyclerClick_Listener;
import com.retrofit.mobile.utils.Toolbar_ActionMode_Callback;
import com.retrofit.mobile.utils.dialog.LoadingDialog;
import com.retrofit.mobile.utils.dialog.LoadingView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProductFragment extends Fragment {
    private static RecyclerView recyclerView;
    private LoadingView loadingView;
    private ActionMode mActionMode;
    private static MyProductsAdapter adapter;
    private static List<InfoMyTovar> infoMyTovars;
    private LinearLayout linearLayout;
    private Button btnAddProducts;

    public MyProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_product, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView)view.findViewById(R.id.expandable_seller_my_tovar_recycler);
        linearLayout = (LinearLayout)view.findViewById(R.id.no_products_ll);
        btnAddProducts = (Button) view.findViewById(R.id.btn_no_products);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        initMyProducts();
        implementRecyclerViewClickListeners();
        btnAddProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new RegestrationProductFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container_frame,fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    private void implementRecyclerViewClickListeners() {
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerClick_Listener() {
            @Override
            public void onClick(View view, int position) {
                if(mActionMode != null)
                    onListItemSelect(position);
            }

            @Override
            public void onLongClick(View view, int position) {
                onListItemSelect(position);
            }
        }));
    }

    private void onListItemSelect(int position) {
        adapter.toggleSelection(position);//Toggle the selection

        boolean hasCheckedItems = adapter.getSelectedCount() > 0;//Check if any items are already selected or not


        if (hasCheckedItems && mActionMode == null) {
            // there are some selected items, start the actionMode
            mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new Toolbar_ActionMode_Callback(getActivity(), adapter, infoMyTovars, 2));
        }
        else if (!hasCheckedItems && mActionMode != null) {
            // there no selected items, finish the actionMode
            mActionMode.finish();
            mActionMode = null;
        }

        if (mActionMode != null)
            //set action mode title on item selection
            mActionMode.setTitle(String.valueOf(adapter.getSelectedCount()));
    }

    public void setNullToActionMode() {
        if (mActionMode != null)
            mActionMode = null;
    }

    //Delete selected rows
    public void deleteRows(ActionMode mode) {
        SparseBooleanArray selected = adapter.getSelectedIds();//Get selected ids
        List<String> idArchive = adapter.getIdProducts();
        String ids = "";
        boolean start = false;
        for (int i = 0; i < idArchive.size(); i++) {
            if(i == idArchive.size() - 1){
                ids = ids + idArchive.get(i);
                start = true;
            }else{
                ids = ids + idArchive.get(i) + ",";
            }
        }
        Log.i("ssss","ids"+ids);
        if(start) {
            ApiInterface apiInterface = ApiClient.getApiInterface();
            Call<DeleteArchiveResponse> call = apiInterface.deleteMyTovar(ids);
            call.enqueue(new Callback<DeleteArchiveResponse>() {
                @Override
                public void onResponse(Call<DeleteArchiveResponse> call, Response<DeleteArchiveResponse> response) {
                    DeleteArchiveResponse archiveResponse = response.body();
                    DataDeleteArchive deleteArchive = archiveResponse.getDeleteArchives().get(0);
                    boolean success = deleteArchive.isSuccess();
                    if(success){
                        Log.i("ssss","delete");
                    }else {
                        String error = deleteArchive.getErrors().get(0);
//                        Toast.makeText(getContext(),error,Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<DeleteArchiveResponse> call, Throwable t) {
//                    Toast.makeText(getContext(),t.toString(),Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Log.i("ssss","null");
        }

        for (int i = (selected.size() - 1); i >= 0; i--) {
            if (selected.valueAt(i)) {
                //If current id is selected remove the item via key
                if(infoMyTovars.size() == 1){

                }else {
                    infoMyTovars.remove(selected.keyAt(i));
                    adapter = new MyProductsAdapter(getActivity(),infoMyTovars);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();//notify adapter
                }

            }
        }
        mode.finish();

    }

    private void initMyProducts() {
        loadingView = LoadingDialog.view(getActivity().getFragmentManager());
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<MyTovarResponse> call = apiInterface.getMyTovar();
        loadingView.showLoading();
        call.enqueue(new Callback<MyTovarResponse>() {
            @Override
            public void onResponse(Call<MyTovarResponse> call, Response<MyTovarResponse> response) {
                MyTovarResponse myTovarResponse= response.body();
                List<DataMyTovar> dataMyTovar = myTovarResponse.getMyTovars();
                infoMyTovars = dataMyTovar.get(0).getInfoMyTovars();
                boolean suc = dataMyTovar.get(0).isSuccess();
                if(suc){
                    if(infoMyTovars.size() == 0){
                        recyclerView.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                    adapter = new MyProductsAdapter(getActivity(),infoMyTovars);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    loadingView.hideLoading();
                }

            }

            @Override
            public void onFailure(Call<MyTovarResponse> call, Throwable t) {
                Toast.makeText(getActivity(),t.toString(),Toast.LENGTH_SHORT).show();
                loadingView.hideLoading();
            }
        });
    }

    public String timestampYMD(long time){
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.setTimeInMillis(time * 1000);
        calendar.add(Calendar.MILLISECOND,tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat forma = new SimpleDateFormat("dd.MM.yyyy");
        Date curretntTimeZone = (Date) calendar.getTime();
        String formated = forma.format(curretntTimeZone);
        return formated;
    }
}