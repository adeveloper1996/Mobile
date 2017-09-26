package com.retrofit.mobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.retrofit.mobile.R;
import com.retrofit.mobile.activity.AdvertDetailsActivity;
import com.retrofit.mobile.model.DataLoggedIn;
import com.retrofit.mobile.model.InfoAllAdvert;
import com.retrofit.mobile.response.LoggedInResponse;
import com.retrofit.mobile.rest.ApiClient;
import com.retrofit.mobile.rest.ApiInterface;
import com.retrofit.mobile.utils.OnLoadMoreListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nursultan on 03.09.2017.
 */

public class AdvertGridViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private int[] tmp = null;
    private boolean loading;
    private Realm realm;
    private OnLoadMoreListener onLoadMoreListener;
    private Random mRandom = new Random();

    private Context context;
    private List<InfoAllAdvert> adverts;
    private AddRemoveFavorite addRemoveFavorite;


    public AdvertGridViewAdapter(List<InfoAllAdvert> adverts, Context context, RecyclerView recyclerView) {
        this.context = context;
        this.adverts = adverts;

        if(recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            final StaggeredGridLayoutManager linearLayoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    tmp = linearLayoutManager.findLastVisibleItemPositions(tmp);
                    if(tmp != null && tmp.length>0) {
                        lastVisibleItem = tmp[0];
                    }

                    if(!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if(onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return adverts.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType == VIEW_ITEM){
            return  new AdvertGridViewHolder(inflater.inflate(R.layout.grid_view_advert, parent, false));
        }else {
            return new ProgressGridViewHolder(inflater.inflate(R.layout.progressbar_item,  parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
           if(holder instanceof AdvertGridViewHolder) {
               ((AdvertGridViewHolder)holder).label.setText(adverts.get(position).getModelname());
               ((AdvertGridViewHolder)holder).price.setText(adverts.get(position).getPrice()+"тг");
               ((AdvertGridViewHolder)holder).date.setText(timestampYMD(Long.parseLong(adverts.get(position).getCreated())));
               if(adverts.get(position).getPhotos().size() != 0) {
                   Picasso.with(context).load(adverts.get(position).getPhotos().get(0).getUrl()).error(R.drawable.nophoto).fit().centerCrop().into(((AdvertGridViewHolder)holder).img);
               }else {
                   Picasso.with(context).load(R.drawable.nophoto).into(((AdvertGridViewHolder)holder).img);
               }
               ((AdvertGridViewHolder)holder).idAdvert = adverts.get(position).getId();
               realm = Realm.getDefaultInstance();
               RealmResults<InfoAllAdvert> realmResults = realm.where(InfoAllAdvert.class).findAll();
               if (realmResults.get(position).getFavourite() == 1){
                   ((AdvertGridViewHolder)holder).iBtnAddFavorite.setImageResource(R.drawable.ic_star_outline_full);
               }
               ((AdvertGridViewHolder)holder).iBtnAddFavorite.setOnClickListener(v -> {
                   Log.d("ssss", "onclickFavorite");
                   addRemoveFavorite = new AddRemoveFavorite(holder);
                   addRemoveFavorite.execute();
               });

           } else {
               ((ProgressGridViewHolder)holder).progressBar.setIndeterminate(true);
           }
    }

    public void setLoaded() {loading = false;}

    @Override
    public int getItemCount() {
        return adverts.size();
    }

    public static class AdvertGridViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView label;
        TextView price;
        TextView date;
        ImageView img;
        CardView cardView;
        ImageButton iBtnAddFavorite;
        String idAdvert;


        public AdvertGridViewHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);
            cardView = (CardView) itemView.findViewById(R.id.rv_grid_item);
            iBtnAddFavorite = (ImageButton)itemView.findViewById(R.id.iBtn_add_favorite);
            label = (TextView) itemView.findViewById(R.id.txt_name_grid_view_advert);
            price = (TextView) itemView.findViewById(R.id.txt_price_grid_view_advert);
            date = (TextView) itemView.findViewById(R.id.txt_date_grid_view_advert);
            img = (ImageView)itemView.findViewById(R.id.img_grid_view_advert);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(itemView.getContext(), AdvertDetailsActivity.class);
            intent.putExtra("advertPhoto", idAdvert);
            Toast.makeText(itemView.getContext(),"1",Toast.LENGTH_SHORT).show();
            itemView.getContext().startActivity(intent);
        }
    }

    public static class ProgressGridViewHolder extends RecyclerView.ViewHolder{
        private ProgressBar progressBar;

        public ProgressGridViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar)itemView.findViewById(R.id.progressBar1);
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.onLoadMoreListener = loadMoreListener;
    }

    public String timestampYMD(long time){
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.setTimeInMillis(time * 1000);
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        Date currentTimeZone = (Date) calendar.getTime();
        String formated = format.format(currentTimeZone);
        return formated;
    }

    private class AddRemoveFavorite extends AsyncTask<Void, Void, Void> {
        private RecyclerView.ViewHolder holder;
        AddRemoveFavorite(RecyclerView.ViewHolder holder) {this.holder = holder;}

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            loggedin(holder);
            return null;
        }
    }

    private void loggedin(RecyclerView.ViewHolder holder){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<LoggedInResponse> call = apiInterface.loggedIn();
        call.enqueue(new Callback<LoggedInResponse>() {
            @Override
            public void onResponse(Call<LoggedInResponse> call, Response<LoggedInResponse> response) {
                LoggedInResponse loggedInResponse = response.body();
                DataLoggedIn dataLoggedIn = loggedInResponse.getDataLoggedIns().get(0);
                boolean success = dataLoggedIn.isSuccess();
                if(success) {
                    realm = Realm.getDefaultInstance();
                    RealmQuery<InfoAllAdvert> realmResults = realm.where(InfoAllAdvert.class).contains("id", ((AdvertGridViewHolder)holder).idAdvert);
                    int favorite = realmResults.findFirst().getFavourite();
                    if(favorite == 0) {
                        Log.d("ssss", "favorite = 0");
                        ((AdvertGridViewHolder)holder).iBtnAddFavorite.setBackgroundResource(R.drawable.ic_star_outline_full);
                        realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        RealmQuery<InfoAllAdvert> realmResults1 = realm.where(InfoAllAdvert.class).contains("id", ((AdvertGridViewHolder)holder).idAdvert);
                        realmResults1.findFirst().setFavourite(1);
                        realm.commitTransaction();

                        //addFav
                    }else {
                        Log.d("ssss","favorite = 1");
                        ((AdvertGridViewHolder)holder).iBtnAddFavorite.setImageResource(android.R.color.transparent);
                        ((AdvertGridViewHolder) holder).iBtnAddFavorite.setBackgroundResource(R.drawable.ic_star_outline);
                        realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        RealmQuery<InfoAllAdvert> realmResults1 = realm.where(InfoAllAdvert.class).contains("id",((AdvertGridViewHolder) holder).idAdvert);
                        realmResults1.findFirst().setFavourite(0);
                        realm.commitTransaction();
                       // deleteFavoriteApi(((AdvertGridViewHolder) holder).idAdvert);
                    }
                }else {
                    Toast.makeText(context,"Войдите в аккаунт" , Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(context,LoginSignUpActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<LoggedInResponse> call, Throwable t) {
                Toast.makeText(context,t.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
