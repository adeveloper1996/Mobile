package com.retrofit.mobile.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.retrofit.mobile.R;
import com.retrofit.mobile.model.DataDeleteArchive;
import com.retrofit.mobile.model.InfoMyTovar;
import com.retrofit.mobile.response.DeleteArchiveResponse;
import com.retrofit.mobile.rest.ApiClient;
import com.retrofit.mobile.rest.ApiInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Andrey on 1/31/2017.
 */

public class MyProductsAdapter extends ExpandableRecyclerAdapter<MyProductsAdapter.MyProductItem> {
    public static final int TYPE_PERSON = 1001;
    private List<InfoMyTovar> myTovars = new ArrayList<>();
    private Context context;
    private SparseBooleanArray mSelectedItemsIds;
    private List<String> idProducts;

    public MyProductsAdapter(Context context, List<InfoMyTovar> myTovars) {
        super(context);
        this.myTovars = myTovars;
        this.context = context;
        mSelectedItemsIds = new SparseBooleanArray();
        idProducts = new ArrayList<>();
        setItems(getSampleItems());
    }

    public static class MyProductItem extends ExpandableRecyclerAdapter.ListItem {
        public String id;
        public String markName;
        public String modelName;
        public String data;
        public String status;
        public String tovarId;
        public String tvmId;

        public MyProductItem(String markName,String status,String data) {
            super(TYPE_HEADER);
            this.markName = markName;
            this.status = status;
            this.data = data;
        }

        public MyProductItem(String modelName,String tovarId,String tvmId,String isNew) {
            super(TYPE_PERSON);
            this.modelName = modelName;
            this.tovarId = tovarId;
            this.tvmId = tvmId;
        }
    }

    public class HeaderViewHolder extends ExpandableRecyclerAdapter.HeaderViewHolder {
        TextView markName;
        TextView txtStatusState;
        TextView txtData;
        String statusState;

        public HeaderViewHolder(View view) {
            super(view, (ImageView) view.findViewById(R.id.item_arrow));

            markName = (TextView) view.findViewById(R.id.txt_my_order_name);
            txtStatusState = (TextView) view.findViewById(R.id.txt_my_products_status);
            txtData = (TextView) view.findViewById(R.id.txt_my_order_date);
        }

        public void bind(int position) {
            super.bind(position);

            markName.setText(visibleItems.get(position).markName);
            txtData.setText(visibleItems.get(position).data);
            statusState = visibleItems.get(position).status;
            if (statusState.equals("1")) {
                txtStatusState.setText("Новый");
            } else {
                txtStatusState.setText("Б/У");
            }
            itemView.setBackgroundColor(mSelectedItemsIds.get(position) ? 0x9934B5E4: Color.rgb(33,150,243));
        }
    }

    public class PersonViewHolder extends ExpandableRecyclerAdapter.ViewHolder implements View.OnClickListener,PopupMenu.OnMenuItemClickListener{
        TextView txtModelName;

        public PersonViewHolder(View view) {
            super(view);

            txtModelName = (TextView)view.findViewById(R.id.txt_my_tovar_child_name);
            txtModelName.setOnClickListener(this);
        }

        public void bind(int position) {
            txtModelName.setText(visibleItems.get(position).modelName);

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_delete_product:
                    Log.d("ssss",visibleItems.get(getAdapterPosition()).tvmId);
                    deleteProduct(visibleItems.get(getAdapterPosition()).tvmId);
                    return true;
            }
            return false;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.txt_my_tovar_child_name:
                    Log.d("ssss",visibleItems.get(getAdapterPosition()).modelName + "id" + visibleItems.get(getAdapterPosition()).tvmId + "tid" + visibleItems.get(getAdapterPosition()).tovarId);
                    PopupMenu popupMenu = new PopupMenu(itemView.getContext(),v);
                    popupMenu.setOnMenuItemClickListener(this);
                    popupMenu.inflate(R.menu.menu_delete_product);
                    popupMenu.show();
                    for (int i = 0; i < visibleItems.size(); i++) {
                        Log.d("ssss",""+visibleItems.get(i).ItemType);
                    }
                    Log.d("ssss","pos"+getAdapterPosition());
                    break;
            }
        }

        private void deleteProduct(String tvmId) {
            ApiInterface apiInterface = ApiClient.getApiInterface();
            Call<DeleteArchiveResponse> call = apiInterface.deleteProduct(tvmId);
            call.enqueue(new Callback<DeleteArchiveResponse>() {
                @Override
                public void onResponse(Call<DeleteArchiveResponse> call, Response<DeleteArchiveResponse> response) {
                    DeleteArchiveResponse archiveResponse = response.body();
                    DataDeleteArchive deleteArchive = archiveResponse.getDeleteArchives().get(0);
                    boolean success = deleteArchive.isSuccess();
                    if(success){
                        Log.i("ssss","delete");
                        removeItemAt(getAdapterPosition());
                        notifyDataSetChanged();
                        for (int i = 0; i < visibleItems.size(); i++) {
                            Log.d("ssss","afterdelete"+visibleItems.get(i).ItemType);
                        }
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
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case TYPE_HEADER:
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.expandable_items_my_products, parent, false);
                HeaderViewHolder headerViewHolder = new HeaderViewHolder(view);
                return headerViewHolder;
            case TYPE_PERSON:
            default:
                LayoutInflater inflater1 = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater1.inflate(R.layout.expandable_my_tovar_child, parent, false);
                PersonViewHolder personViewHolder = new PersonViewHolder(view);
                return personViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(ExpandableRecyclerAdapter.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                ((HeaderViewHolder) holder).bind(position);
                break;
            case TYPE_PERSON:
            default:
                ((PersonViewHolder) holder).bind(position);
                break;
        }
    }

    private List<MyProductItem> getSampleItems() {
        List<MyProductItem> items = new ArrayList<>();

        for (int i = 0; i < myTovars.size(); i++) {
            items.add(new MyProductItem(myTovars.get(i).getMarkname(),myTovars.get(i).getIsNew(),timestampYMD(Long.parseLong(myTovars.get(i).getCreated()))));
            for (int j = 0; j < myTovars.get(i).getModels().size(); j++) {
                items.add(new MyProductItem(myTovars.get(i).getModels().get(j).getModelname(),myTovars.get(i).getModels().get(j).getTovarId(),myTovars.get(i).getModels().get(j).getTvmId(),myTovars.get(i).getUserId()));
            }
        }

        return items;
    }


    public void toggleSelection(int position) {
        String id = myTovars.get(position).getId();
        selectView(position, !mSelectedItemsIds.get(position), id);
    }

    //Remove selected selections
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }


    //Put or delete selected position into SparseBooleanArray
    public void selectView(int position, boolean value, String id) {
        if (value) {
            mSelectedItemsIds.put(position, value);
            idProducts.add(id);
        }
        else {
            mSelectedItemsIds.delete(position);
            notifyDataSetChanged();
            idProducts.remove(id);
        }

        notifyDataSetChanged();
    }

    //Get total selected count
    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    //Return all selected ids
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    public List<String> getIdProducts(){
        return idProducts;
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
