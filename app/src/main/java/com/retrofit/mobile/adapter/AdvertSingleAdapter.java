package com.retrofit.mobile.adapter;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.retrofit.mobile.R;
import com.retrofit.mobile.activity.AdvertDetailsActivity;
import com.retrofit.mobile.model.DataAddAdvertFavorite;
import com.retrofit.mobile.model.DataDeleteAdvertFavorite;
import com.retrofit.mobile.model.DataLoggedIn;
import com.retrofit.mobile.model.InfoAllAdvert;
import com.retrofit.mobile.response.AddAdvFavoriteResponse;
import com.retrofit.mobile.response.DeleteAdvFavoriteResponse;
import com.retrofit.mobile.response.LoggedInResponse;
import com.retrofit.mobile.rest.ApiClient;
import com.retrofit.mobile.rest.ApiInterface;
import com.retrofit.mobile.utils.OnLoadMoreListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdvertSingleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private int row;
    private Realm realm;
    private OnLoadMoreListener onLoadMoreListener;

    private List<InfoAllAdvert> advertLists;
    private Context context;

    public AdvertSingleAdapter(List<InfoAllAdvert> advertLists, Context context,RecyclerView recyclerView,int row) {
        this.advertLists = advertLists;
        this.context = context;
        this.row = row;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView,
                                       int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading&& totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
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
        return advertLists.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType==VIEW_ITEM){
            return new AdvertViewHolder(inflater.inflate(row,parent,false));
        }else{
            return new ProgressViewHolder(inflater.inflate(R.layout.progressbar_item,parent,false));
        }
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof AdvertViewHolder) {
            ((AdvertViewHolder)holder).txtAdvertName.setText(advertLists.get(position).getModelname());
            ((AdvertViewHolder)holder).txtAdvertPrice.setText(advertLists.get(position).getPrice() + " тг");
            ((AdvertViewHolder)holder).txtAdvertData.setText(timestampYMD(advertLists.get(position).getCreated()));
            if(advertLists.get(position).getPhotos().size() != 0) {
                Picasso.with(context).load(advertLists.get(position).getPhotos().get(0).getUrl())
                        .placeholder(R.drawable.loadimg).error(R.drawable.nophoto).fit().centerCrop().into(((AdvertViewHolder) holder).imgAdvert);
            }else {
                Picasso.with(context).load(R.drawable.nophoto).into(((AdvertViewHolder) holder).imgAdvert);
            }
            ((AdvertViewHolder)holder).idAdvert = advertLists.get(position).getId();
            realm = Realm.getDefaultInstance();
            RealmResults<InfoAllAdvert> realmResul = realm.where(InfoAllAdvert.class).findAll();
            if(realmResul.get(position).getFavourite() == 0){
                ((AdvertViewHolder)holder).ibtnFavorite.setBackgroundResource(R.drawable.ic_star_outline);
            }else {
                ((AdvertViewHolder)holder).ibtnFavorite.setBackgroundResource(R.drawable.ic_star_outline_full);
            }

            ((AdvertViewHolder) holder).ibtnFavorite.setOnClickListener(v -> {
                AddRemoveFavorite addRemoveFavorite = new AddRemoveFavorite(holder);
                addRemoveFavorite.execute();
            });

        }else {
            ((ProgressViewHolder)holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return advertLists.size();
    }

    public static class AdvertViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imgAdvert;
        private TextView txtAdvertName;
        private TextView txtAdvertPrice;
        private TextView txtAdvertData;
        private ImageButton ibtnFavorite;
        private LinearLayout llhor;
        private InfoAllAdvert advert;
        private String idAdvert;

        public AdvertViewHolder(View v) {
            super(v);
            itemView.setOnClickListener(this);
            imgAdvert = (ImageView)itemView.findViewById(R.id.img_advert_single);
            txtAdvertName = (TextView)itemView.findViewById(R.id.txt_name_advert_single);
            txtAdvertPrice = (TextView)itemView.findViewById(R.id.txt_price_advert_single);
            txtAdvertData = (TextView)itemView.findViewById(R.id.txt_date_advert_single);
            ibtnFavorite = (ImageButton)itemView.findViewById(R.id.imageButtonFavorite);
            llhor = (LinearLayout)itemView.findViewById(R.id.lladvertsingle);
            llhor.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.lladvertsingle:
                    Intent intent = new Intent(itemView.getContext(), AdvertDetailsActivity.class);
                    intent.putExtra("advertPhoto",idAdvert);
                    Toast.makeText(itemView.getContext(), "1", Toast.LENGTH_SHORT).show();
                    itemView.getContext().startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar)v.findViewById(R.id.progressBar1);
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public String timestampYMD(String timeLong){
        long time = Long.parseLong(timeLong);
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.setTimeInMillis(time * 1000);
        calendar.add(Calendar.MILLISECOND,tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat forma = new SimpleDateFormat("dd.MM.yyyy");
        Date curretntTimeZone = (Date) calendar.getTime();
        String formated = forma.format(curretntTimeZone);
        return formated;
    }

    private class AddRemoveFavorite extends AsyncTask<Void, Void, Void>{
        private RecyclerView.ViewHolder holder;
        AddRemoveFavorite(RecyclerView.ViewHolder holder) {
            this.holder = holder;
        }

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

    private void loggedin(RecyclerView.ViewHolder holder) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<LoggedInResponse> call = apiInterface.loggedIn();
        call.enqueue(new Callback<LoggedInResponse>() {
            @Override
            public void onResponse(Call<LoggedInResponse> call, Response<LoggedInResponse> response) {
                LoggedInResponse loggedInResponse = response.body();
                DataLoggedIn dataLoggedIn = loggedInResponse.getDataLoggedIns().get(0);
                boolean succsess = dataLoggedIn.isSuccess();
                if (succsess){
                    realm = Realm.getDefaultInstance();
                    RealmQuery<InfoAllAdvert> realmResults = realm.where(InfoAllAdvert.class).contains("id",((AdvertViewHolder) holder).idAdvert);
                    int favourite = realmResults.findFirst().getFavourite();
                    if (favourite == 0) {
                        ((AdvertViewHolder) holder).ibtnFavorite.setBackgroundResource(R.drawable.ic_star_outline_full);
                        realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        RealmQuery<InfoAllAdvert> realmResults1 = realm.where(InfoAllAdvert.class).contains("id",((AdvertViewHolder) holder).idAdvert);
                        realmResults1.findFirst().setFavourite(1);
                        realm.commitTransaction();

                        addFavoriteApi(((AdvertViewHolder) holder).idAdvert);

                    }else {
                        ((AdvertViewHolder) holder).ibtnFavorite.setBackgroundResource(R.drawable.ic_star_outline);
                        realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        RealmQuery<InfoAllAdvert> realmResults1 = realm.where(InfoAllAdvert.class).contains("id",((AdvertViewHolder) holder).idAdvert);
                        realmResults1.findFirst().setFavourite(0);
                        realm.commitTransaction();
                        deleteFavoriteApi(((AdvertViewHolder) holder).idAdvert);
                    }
                }
                else {
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

    private void deleteFavoriteApi(String idAdvert) {
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<DeleteAdvFavoriteResponse> call = apiInterface.deleteFavoriteAdvert(idAdvert);
        call.enqueue(new Callback<DeleteAdvFavoriteResponse>() {
            @Override
            public void onResponse(Call<DeleteAdvFavoriteResponse> call, Response<DeleteAdvFavoriteResponse> response) {
                DeleteAdvFavoriteResponse favoriteResponse = response.body();
                DataDeleteAdvertFavorite advertFavorite = favoriteResponse.getAdvertFavorites().get(0);
                boolean success = advertFavorite.isSuccess();
            }

            @Override
            public void onFailure(Call<DeleteAdvFavoriteResponse> call, Throwable t) {

            }
        });
    }

    private void addFavoriteApi(String idAdvert) {

        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<AddAdvFavoriteResponse> call = apiInterface.addFavoriteAdvert(idAdvert);
        call.enqueue(new Callback<AddAdvFavoriteResponse>() {
            @Override
            public void onResponse(Call<AddAdvFavoriteResponse> call, Response<AddAdvFavoriteResponse> response) {
                AddAdvFavoriteResponse favoriteResponse = response.body();
                DataAddAdvertFavorite advertFavorite = favoriteResponse.getAdvertFavorites().get(0);
                boolean success = advertFavorite.isSuccess();
            }

            @Override
            public void onFailure(Call<AddAdvFavoriteResponse> call, Throwable t) {

            }
        });
    }

    public void setFilter(List<InfoAllAdvert> countryModels) {
        advertLists = new ArrayList<>();
        advertLists.addAll(countryModels);
        notifyDataSetChanged();
    }


}
