package com.retrofit.mobile.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.retrofit.mobile.R;
import com.retrofit.mobile.model.DataViewCount;
import com.retrofit.mobile.model.InfoShowMyAdvert;
import com.retrofit.mobile.response.ViewCountResponse;
import com.retrofit.mobile.rest.ApiClient;
import com.retrofit.mobile.rest.ApiInterface;
import com.retrofit.mobile.utils.dialog.LoadingDialog;
import com.retrofit.mobile.utils.dialog.LoadingView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nursultan on 08.09.2017.
 */

public class ShowMyAdvertAdapter extends RecyclerView.Adapter<ShowMyAdvertAdapter.MyAdvertViewHolder> {
    private List<InfoShowMyAdvert> myAdverts;
    private int row;
    private Context context;
    private int type;
    private LoadingView loadingView;


    public ShowMyAdvertAdapter(List<InfoShowMyAdvert> myAdverts, int row, Context context, int type) {
        this.myAdverts = myAdverts;
        this.row = row;
        this.context = context;
        this.type = type;
    }

    public class MyAdvertViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,PopupMenu.OnMenuItemClickListener {
        private CardView cardView;
        private ImageView imgAdvert;
        private TextView txtAdvertName;
        private TextView txtAdvertPrice;
        private TextView txtAdvertData;
        private TextView txtCountShow;
        private TextView txtCountSms;
        private TextView txtCountNumber;
        private ImageButton iBtnSettings;

        public MyAdvertViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            FragmentManager manager = ((Activity) context).getFragmentManager();
            loadingView = LoadingDialog.view(manager);
            cardView = (CardView) itemView.findViewById(R.id.card_view_my_advert);
            imgAdvert = (ImageView) itemView.findViewById(R.id.img_my_advert);
            txtAdvertName = (TextView) itemView.findViewById(R.id.txt_my_advert_name);
            txtAdvertPrice = (TextView) itemView.findViewById(R.id.txt_price_advert_single);
            txtAdvertData = (TextView) itemView.findViewById(R.id.txt_my_advert_data);
            txtCountShow = (TextView) itemView.findViewById(R.id.txt_my_advert_view_count);
            txtCountNumber = (TextView) itemView.findViewById(R.id.txt_my_advert_view_number);
            txtCountSms = (TextView) itemView.findViewById(R.id.txt_my_advert_view_count);
            iBtnSettings = (ImageButton) itemView.findViewById(R.id.ibtn_my_advbert_settings);
            iBtnSettings.setOnClickListener(this);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ibtn_my_advbert_settings:
                    PopupMenu popupMenu = new PopupMenu(itemView.getContext(), v);
                    popupMenu.setOnMenuItemClickListener(this);
                    if (type == 1) {
                        popupMenu.inflate(R.menu.menu_my_advert_settings);
                    } else {
                        popupMenu.inflate(R.menu.menu_my_advert_archive_settings);
                    }
                    popupMenu.show();
                    break;
                case R.id.card_view_my_advert:
//                    InfoAllAdvert advert = (InfoAllAdvert) v.getTag();
//                    Intent intent = new Intent(itemView.getContext(), AdvertDetailsActivity.class);
//                    intent.putExtra("advertDet",advert);
//                    Toast.makeText(itemView.getContext(),"1",Toast.LENGTH_SHORT).show();
//                    Log.i("ssss",""+1);
//                    itemView.getContext().startActivity(intent);
                    break;
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_add_archive_my_advert:
                    sendArchive();
                    return true;
                case R.id.menu_do_active_my_advert:
                     doActive();
                    return true;
                case R.id.menu_delete_my_advert:
                     deleteAdvert();
                    return true;
            }
            return false;
        }

        private void doActive() {
            Log.i("ssss",myAdverts.get(getAdapterPosition()).getId());
            ApiInterface apiInterface = ApiClient.getApiInterface();
            loadingView.showLoading();
            Call<ViewCountResponse> call = apiInterface.doActiveMyAdvert(myAdverts.get(getAdapterPosition()).getId());
            call.enqueue(new Callback<ViewCountResponse>() {
                @Override
                public void onResponse(Call<ViewCountResponse> call, Response<ViewCountResponse> response) {
                    ViewCountResponse countResponse = response.body();
                    DataViewCount viewCount = countResponse.getViewCountList().get(0);
                    boolean succes = viewCount.isSuccess();
                    if(succes){
                        Log.i("ssss",""+ succes);
                        myAdverts.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(),myAdverts.size());
                        loadingView.hideLoading();
                    }else {
                        String error = viewCount.getErrors().get(0);
                        Toast.makeText(itemView.getContext(),error,Toast.LENGTH_SHORT).show();
                        loadingView.hideLoading();
                    }
                }

                @Override
                public void onFailure(Call<ViewCountResponse> call, Throwable t) {
                    Toast.makeText(itemView.getContext(),t.toString(),Toast.LENGTH_SHORT).show();
                    loadingView.hideLoading();
                }
            });
        }

        private void sendArchive() {
            Log.i("ssss",myAdverts.get(getAdapterPosition()).getId());
            ApiInterface apiInterface = ApiClient.getApiInterface();
            loadingView.showLoading();
            Call<ViewCountResponse> call = apiInterface.sendArchiveMyAdvert(myAdverts.get(getAdapterPosition()).getId());
            call.enqueue(new Callback<ViewCountResponse>() {
                @Override
                public void onResponse(Call<ViewCountResponse> call, Response<ViewCountResponse> response) {
                    ViewCountResponse countResponse = response.body();
                    DataViewCount viewCount = countResponse.getViewCountList().get(0);
                    boolean succes = viewCount.isSuccess();
                    if(succes){
                        Log.i("ssss",""+succes);
                        myAdverts.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(),myAdverts.size());
                    }else {
                        String error = viewCount.getErrors().get(0);
                        Toast.makeText(itemView.getContext(),error,Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ViewCountResponse> call, Throwable t) {
                    Toast.makeText(itemView.getContext(),t.toString(),Toast.LENGTH_SHORT).show();
                }
            });
            loadingView.hideLoading();
        }

        private void deleteAdvert() {
            Log.i("ssss",myAdverts.get(getAdapterPosition()).getId());
            ApiInterface apiInterface = ApiClient.getApiInterface();
            loadingView.showLoading();
            Call<ViewCountResponse> call = apiInterface.deleteMyAdvert(myAdverts.get(getAdapterPosition()).getId());
            call.enqueue(new Callback<ViewCountResponse>() {
                @Override
                public void onResponse(Call<ViewCountResponse> call, Response<ViewCountResponse> response) {
                    ViewCountResponse countResponse = response.body();
                    DataViewCount viewCount = countResponse.getViewCountList().get(0);
                    boolean succes = viewCount.isSuccess();
                    if(succes){
                        Log.i("ssss",""+succes);
                        myAdverts.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(),myAdverts.size());
                    }else {
                        String error = viewCount.getErrors().get(0);
                        Toast.makeText(itemView.getContext(),error,Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ViewCountResponse> call, Throwable t) {
                    Toast.makeText(itemView.getContext(),t.toString(),Toast.LENGTH_SHORT).show();
                }
            });
            loadingView.hideLoading();
        }
    }

        @Override
        public MyAdvertViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(row, parent, false);
            return new MyAdvertViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyAdvertViewHolder holder, int position) {
            holder.txtAdvertName.setText(myAdverts.get(position).getModelname());
            holder.txtAdvertPrice.setText(myAdverts.get(position).getPrice() + " тг");
            holder.txtAdvertData.setText(timestampYMD(Long.parseLong(myAdverts.get(position).getCreated())));
            holder.txtCountShow.setText(myAdverts.get(position).getViewCount());
            holder.txtCountNumber.setText(myAdverts.get(position).getTviewCount());
            holder.txtCountSms.setText("0");
            Picasso.with(context).load(myAdverts.get(position).getPhotos().get(0).getUrl()).placeholder(R.drawable.loadimg).error(R.drawable.nophoto).fit().centerCrop().into(holder.imgAdvert);
        }

        @Override
        public int getItemCount() {
            return myAdverts.size();
        }

        public String timestampYMD(long time) {
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(time * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat forma = new SimpleDateFormat("dd.MM.yyyy");
            Date curretntTimeZone = (Date) calendar.getTime();
            String formated = forma.format(curretntTimeZone);
            return formated;
        }
    }


