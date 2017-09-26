package com.retrofit.mobile.adapter;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.retrofit.mobile.R;
import com.retrofit.mobile.activity.AnketaSellerActivity;
import com.retrofit.mobile.model.BuyerRequestBids;
import com.retrofit.mobile.model.DataSendPrice;
import com.retrofit.mobile.model.InfoBuyerRequest;
import com.retrofit.mobile.model.InfoClient;
import com.retrofit.mobile.response.SendPriceResponse;
import com.retrofit.mobile.rest.ApiClient;
import com.retrofit.mobile.rest.ApiInterface;
import com.retrofit.mobile.utils.NumberTextWatcher;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nursultan on 13.09.2017.
 */

public class BuyerRequestsAdapter extends ExpandableRecyclerAdapter<BuyerRequestsAdapter.BuyerRequestItem> {
    public static final int TYPE_PERSON = 1001;
    private List<InfoBuyerRequest> myOrders = new ArrayList<>();
    private Context context;
    private Realm realm;

    public BuyerRequestsAdapter(Context context,List<InfoBuyerRequest> myOrders) {
        super(context);
        this.context = context;
        this.myOrders = myOrders;
        setItems(getSampleItems());
    }

    public static class BuyerRequestItem extends ExpandableRecyclerAdapter.ListItem {
        public String id;
        public String markName;
        public String modelName;
        public String data;
        public String endtime;
        public String status;
        public String statusDevice;
        public String sellerId;
        public String myId;
        public List<BuyerRequestBids> infoBidses;

        public BuyerRequestItem(String markName, String data, String statusDevice) {
            super(TYPE_HEADER);
            this.markName = markName;
            this.data = data;
            this.statusDevice = statusDevice;
        }

        public BuyerRequestItem(String id, String modelName, String endtime, List<BuyerRequestBids> infoBidses, String status, String sellerId, String myId) {
            super(TYPE_PERSON);
            this.id = id;
            this.modelName = modelName;
            this.endtime = endtime;
            this.infoBidses = infoBidses;
            this.status = status;
            this.sellerId = sellerId;
            this.myId = myId;
        }
    }

    public class HeaderViewHolder extends ExpandableRecyclerAdapter.HeaderViewHolder {
        TextView markName;
        TextView date;
        TextView statusState;
        String statusDeviceHeader;

        public HeaderViewHolder(View view) {
            super(view, (ImageView) view.findViewById(R.id.item_arrow));

            markName = (TextView) view.findViewById(R.id.txt_my_order_name);
            statusState = (TextView) view.findViewById(R.id.txt_my_products_status);
            date = (TextView) view.findViewById(R.id.txt_my_order_date);
        }

        public void bind(int position) {
            super.bind(position);

            markName.setText(visibleItems.get(position).markName);
            date.setText(visibleItems.get(position).data);
            statusDeviceHeader = visibleItems.get(position).statusDevice;
            if (statusDeviceHeader.equals("1")) {
                statusState.setText("Новый");
            } else {
                statusState.setText("Б/У");
            }
        }
    }

    public class PersonViewHolder extends ExpandableRecyclerAdapter.ViewHolder {
        public TextView modelName;
        public TextView itemTimeAuction;
        public LinearLayout linearLayout;
        public LinearLayout linearLayout1;
        public LinearLayout linearLayout2;
        public ImageButton btnShowSeller;
        public ImageButton btnShowSeller2;
        public ImageButton btnShowSeller3;
        public TextView txtPriceSeller;
        public TextView txtPriceSeller1;
        public TextView txtPriceSeller2;
        public TextView txtIdSeller;
        public TextView txtIdSeller1;
        public TextView txtIdSeller2;
        public TextView txtCreatedPrice;
        public TextView txtCreatedPrice1;
        public TextView txtCreatedPrice2;
        public TextView txtNotSeller;
        public TextView txtSellerId;
        public TextView txtMyId;
        public EditText eTxtPrice;
        public ImageButton btnSendPrice;
        public String id;
        public String statusState;
        public List<BuyerRequestBids> bidses;

        public PersonViewHolder(View view) {
            super(view);
            linearLayout  = (LinearLayout)itemView.findViewById(R.id.seller1);
            linearLayout1  = (LinearLayout)itemView.findViewById(R.id.seller2);
            linearLayout2  = (LinearLayout)itemView.findViewById(R.id.seller3);
            txtPriceSeller = (TextView)itemView.findViewById(R.id.txt_my_order_price_seller1);
            txtPriceSeller1 = (TextView)itemView.findViewById(R.id.txt_my_order_price_seller2);
            txtPriceSeller2 = (TextView)itemView.findViewById(R.id.txt_my_order_price_seller3);
            txtIdSeller = (TextView)itemView.findViewById(R.id.txt_my_order_id_seller);
            txtIdSeller1 = (TextView)itemView.findViewById(R.id.txt_my_order_id_seller2);
            txtIdSeller2 = (TextView)itemView.findViewById(R.id.txt_my_order_id_seller3);
            txtCreatedPrice = (TextView)itemView.findViewById(R.id.txt_my_order_date_seller);
            txtCreatedPrice1 = (TextView)itemView.findViewById(R.id.txt_my_order_date_seller2);
            txtCreatedPrice2 = (TextView)itemView.findViewById(R.id.txt_my_order_date_seller3);
            txtNotSeller = (TextView)itemView.findViewById(R.id.txt_my_order_not_seller);
            txtSellerId = (TextView)itemView.findViewById(R.id.txt_id_seller);
            txtMyId = (TextView)itemView.findViewById(R.id.id_user);
            modelName = (TextView) itemView.findViewById(R.id.txt_my_order_name_device);
            itemTimeAuction = (TextView) itemView.findViewById(R.id.txt_my_order_time_of_auction);
            eTxtPrice = (EditText)itemView.findViewById(R.id.eTxt_buyer_request_price);
            btnSendPrice = (ImageButton)itemView.findViewById(R.id.btn_buyer_request_send_price);
            btnShowSeller = (ImageButton) itemView.findViewById(R.id.btn_my_order_seller_anketa);
            btnShowSeller2 = (ImageButton) itemView.findViewById(R.id.btn_my_order_seller_anketa2);
            btnShowSeller3 = (ImageButton) itemView.findViewById(R.id.btn_my_order_seller_anketa3);

            btnSendPrice.setOnClickListener(v -> {
                ApiInterface apiInterface = ApiClient.getApiInterface();
                Call<SendPriceResponse> call = apiInterface.sendPrice(id,eTxtPrice.getText().toString());
                call.enqueue(new Callback<SendPriceResponse>() {
                    @Override
                    public void onResponse(Call<SendPriceResponse> call, Response<SendPriceResponse> response) {
                        SendPriceResponse priceResponse = response.body();
                        DataSendPrice sendPrice = priceResponse.getDataSendPriceList().get(0);
                        boolean suc = sendPrice.isSuccess();
                        if(suc){
                            if(bidses.size()==0){
                                setAnimate(linearLayout);
                                txtPriceSeller.setText(replace(eTxtPrice.getText().toString()) + "тг");
                                txtIdSeller.setText("ID:" + txtMyId.getText());
                                txtCreatedPrice.setText(timestampYMD(System.currentTimeMillis() / 1000));
                                txtNotSeller.setVisibility(View.GONE);
                            }

                            if(bidses.size()==1){
                                if (txtMyId.getText().toString().equals(bidses.get(0).getUserid())){
//                                    linearLayout.setVisibility(View.VISIBLE);
                                    setAnimate(linearLayout);
                                    txtPriceSeller.setText(eTxtPrice.getText().toString() + "тг");
                                    txtIdSeller.setText("ID:" + txtMyId.getText());
                                    txtCreatedPrice.setText(timestampYMD(System.currentTimeMillis() / 1000));
                                }
                                if(Integer.valueOf(replace(eTxtPrice.getText().toString().trim())) < Integer.valueOf(replace(txtPriceSeller.getText().toString().trim()))
                                        &&!txtMyId.getText().toString().equals(bidses.get(0).getUserid())){
//                                    linearLayout.setVisibility(View.VISIBLE);
                                    setAnimate(linearLayout);
                                    txtPriceSeller.setText(replace(eTxtPrice.getText().toString()) + "тг");
                                    txtIdSeller.setText("ID:" + txtMyId.getText());
                                    txtCreatedPrice.setText(timestampYMD(System.currentTimeMillis() / 1000));

//                                    linearLayout1.setVisibility(View.VISIBLE);
                                    setAnimate2(linearLayout1);
                                    txtPriceSeller1.setText(bidses.get(0).getPrice() + "тг");
                                    txtIdSeller1.setText("ID:" + bidses.get(0).getUserid());
                                    txtCreatedPrice1.setText(timestampYMD(Long.parseLong(bidses.get(0).getCreated())));

                                }
                                if(Integer.valueOf(replace(eTxtPrice.getText().toString().trim())) > Integer.valueOf(replace(txtPriceSeller.getText().toString().trim()))
                                        &&!txtMyId.getText().toString().equals(bidses.get(0).getUserid())){
//                                    linearLayout1.setVisibility(View.VISIBLE);
                                    setAnimate(linearLayout1);
                                    txtPriceSeller1.setText(replace(eTxtPrice.getText().toString()) + "тг");
                                    txtIdSeller1.setText("ID:" + txtMyId.getText());
                                    txtCreatedPrice1.setText(timestampYMD(System.currentTimeMillis() / 1000));
                                }
                            }

                            if(bidses.size()==2){
                                for (int i = 0; i < bidses.size(); i++) {
                                    if (txtMyId.getText().toString().equals(bidses.get(i).getUserid())){
                                        if (i == 0){
//                                            linearLayout.setVisibility(View.VISIBLE);
                                            setAnimate(linearLayout);
                                            txtPriceSeller.setText(eTxtPrice.getText().toString() + "тг");
                                            txtIdSeller.setText("ID:" + txtMyId.getText());
                                            txtCreatedPrice.setText(timestampYMD(System.currentTimeMillis() / 1000));
                                        }else {
//                                            linearLayout1.setVisibility(View.VISIBLE);
                                            setAnimate2(linearLayout1);
                                            txtPriceSeller1.setText(eTxtPrice.getText().toString() + "тг");
                                            txtIdSeller1.setText("ID:" + txtMyId.getText());
                                            txtCreatedPrice1.setText(timestampYMD(System.currentTimeMillis() / 1000));
                                        }
                                    }
                                }
                                if(Integer.valueOf(replace(eTxtPrice.getText().toString().trim())) < Integer.valueOf(replace(txtPriceSeller.getText().toString().trim()))
                                        && Integer.valueOf(replace(eTxtPrice.getText().toString().trim())) < Integer.valueOf(replace(txtPriceSeller1.getText().toString().trim()))
                                        && !txtMyId.getText().toString().equals(bidses.get(0).getUserid())
                                        && !txtMyId.getText().toString().equals(bidses.get(1).getUserid())){
//                                    linearLayout.setVisibility(View.VISIBLE);
                                    setAnimate(linearLayout);
                                    txtPriceSeller.setText(replace(eTxtPrice.getText().toString()) + "тг");
                                    txtIdSeller.setText("ID:" + txtMyId.getText());
                                    txtCreatedPrice.setText(timestampYMD(System.currentTimeMillis() / 1000));

//                                    linearLayout1.setVisibility(View.VISIBLE);
                                    setAnimate2(linearLayout1);
                                    txtPriceSeller1.setText(bidses.get(0).getPrice() + "тг");
                                    txtIdSeller1.setText("ID:" + bidses.get(0).getUserid());
                                    txtCreatedPrice1.setText(timestampYMD(Long.parseLong(bidses.get(0).getCreated())));

//                                    linearLayout2.setVisibility(View.VISIBLE);
                                    setAnimate2(linearLayout2);
                                    txtPriceSeller2.setText(bidses.get(1).getPrice() + "тг");
                                    txtIdSeller2.setText("ID:" + bidses.get(1).getUserid());
                                    txtCreatedPrice2.setText(timestampYMD(Long.parseLong(bidses.get(1).getCreated())));
                                }
                                if(Integer.valueOf(replace(eTxtPrice.getText().toString().trim())) > Integer.valueOf(replace(txtPriceSeller.getText().toString().trim()))
                                        && Integer.valueOf(replace(eTxtPrice.getText().toString().trim())) < Integer.valueOf(replace(txtPriceSeller1.getText().toString().trim()))
                                        && !txtMyId.getText().toString().equals(bidses.get(0).getUserid())
                                        && !txtMyId.getText().toString().equals(bidses.get(1).getUserid())){
//                                    linearLayout1.setVisibility(View.VISIBLE);
                                    setAnimate2(linearLayout1);
                                    txtPriceSeller1.setText(replace(eTxtPrice.getText().toString()) + "тг");
                                    txtIdSeller1.setText("ID:" + txtMyId.getText());
                                    txtCreatedPrice1.setText(timestampYMD(System.currentTimeMillis() / 1000));

//                                    linearLayout2.setVisibility(View.VISIBLE);
                                    setAnimate2(linearLayout2);
                                    txtPriceSeller2.setText(bidses.get(1).getPrice() + "тг");
                                    txtIdSeller2.setText("ID:" + bidses.get(1).getUserid());
                                    txtCreatedPrice2.setText(timestampYMD(Long.parseLong(bidses.get(1).getCreated())));
                                }
                                if (Integer.valueOf(replace(eTxtPrice.getText().toString().trim())) > Integer.valueOf(replace(txtPriceSeller.getText().toString().trim()))
                                        && Integer.valueOf(replace(eTxtPrice.getText().toString().trim())) > Integer.valueOf(replace(txtPriceSeller1.getText().toString().trim()))
                                        && !txtMyId.getText().toString().equals(bidses.get(0).getUserid())
                                        && !txtMyId.getText().toString().equals(bidses.get(1).getUserid())){
//                                    linearLayout2.setVisibility(View.VISIBLE);
                                    setAnimate2(linearLayout2);
                                    txtPriceSeller2.setText(replace(eTxtPrice.getText().toString()) + "тг");
                                    txtIdSeller2.setText("ID:" + txtMyId.getText());
                                    txtCreatedPrice2.setText(timestampYMD(Long.parseLong(bidses.get(0).getCreated())));
                                }
                            }

                            if(bidses.size()==3){
                                for (int i = 0; i < bidses.size(); i++) {
                                    if (txtMyId.getText().toString().equals(bidses.get(i).getUserid())){
                                        if (i == 0){
                                            linearLayout.setVisibility(View.VISIBLE);
                                            txtPriceSeller.setText(eTxtPrice.getText().toString() + "тг");
                                            txtIdSeller.setText("ID:" + txtMyId.getText());
                                            txtCreatedPrice.setText(timestampYMD(System.currentTimeMillis() / 1000));
                                        }
                                        if(i == 1) {
                                            linearLayout1.setVisibility(View.VISIBLE);
                                            txtPriceSeller1.setText(eTxtPrice.getText().toString() + "тг");
                                            txtIdSeller1.setText("ID:" + txtMyId.getText());
                                            txtCreatedPrice1.setText(timestampYMD(System.currentTimeMillis() / 1000));
                                        }
                                        if (i == 2){
                                            linearLayout2.setVisibility(View.VISIBLE);
                                            txtPriceSeller2.setText(eTxtPrice.getText().toString() + "тг");
                                            txtIdSeller2.setText("ID:" + txtMyId.getText());
                                            txtCreatedPrice2.setText(timestampYMD(System.currentTimeMillis() / 1000));
                                        }
                                    }
                                }
                                if(Integer.valueOf(replace(eTxtPrice.getText().toString().trim())) < Integer.valueOf(replace(txtPriceSeller.getText().toString().trim()))
                                        && Integer.valueOf(replace(eTxtPrice.getText().toString().trim())) < Integer.valueOf(replace(txtPriceSeller1.getText().toString().trim()))
                                        && Integer.valueOf(replace(eTxtPrice.getText().toString().trim())) < Integer.valueOf(replace(txtPriceSeller2.getText().toString().trim()))
                                        && !txtMyId.getText().toString().equals(bidses.get(0).getUserid())
                                        && !txtMyId.getText().toString().equals(bidses.get(1).getUserid())
                                        && !txtMyId.getText().toString().equals(bidses.get(2).getUserid())){
                                    linearLayout.setVisibility(View.VISIBLE);
                                    txtPriceSeller.setText(replace(eTxtPrice.getText().toString()) + "тг");
                                    txtIdSeller.setText("ID:" + txtMyId.getText());
                                    txtCreatedPrice.setText(timestampYMD(System.currentTimeMillis() / 1000));

                                    linearLayout1.setVisibility(View.VISIBLE);
                                    txtPriceSeller1.setText(bidses.get(0).getPrice() + "тг");
                                    txtIdSeller1.setText("ID:" + bidses.get(0).getUserid());
                                    txtCreatedPrice1.setText(timestampYMD(Long.parseLong(bidses.get(0).getCreated())));

                                    linearLayout2.setVisibility(View.VISIBLE);
                                    txtPriceSeller2.setText(bidses.get(1).getPrice() + "тг");
                                    txtIdSeller2.setText("ID:" + bidses.get(1).getUserid());
                                    txtCreatedPrice2.setText(timestampYMD(Long.parseLong(bidses.get(1).getCreated())));
                                }
                                if(Integer.valueOf(replace(eTxtPrice.getText().toString().trim())) > Integer.valueOf(replace(txtPriceSeller.getText().toString().trim()))
                                        && Integer.valueOf(replace(eTxtPrice.getText().toString().trim())) < Integer.valueOf(replace(txtPriceSeller1.getText().toString().trim()))
                                        && Integer.valueOf(replace(eTxtPrice.getText().toString().trim())) < Integer.valueOf(replace(txtPriceSeller2.getText().toString().trim()))
                                        && !txtMyId.getText().toString().equals(bidses.get(0).getUserid())
                                        && !txtMyId.getText().toString().equals(bidses.get(1).getUserid())
                                        && !txtMyId.getText().toString().equals(bidses.get(2).getUserid())){
                                    linearLayout1.setVisibility(View.VISIBLE);
                                    txtPriceSeller1.setText(replace(eTxtPrice.getText().toString()) + "тг");
                                    txtIdSeller1.setText("ID:" + txtMyId.getText());
                                    txtCreatedPrice1.setText(timestampYMD(System.currentTimeMillis() / 1000));

                                    linearLayout2.setVisibility(View.VISIBLE);
                                    txtPriceSeller2.setText(bidses.get(1).getPrice() + "тг");
                                    txtIdSeller2.setText("ID:" + bidses.get(1).getUserid());
                                    txtCreatedPrice2.setText(timestampYMD(Long.parseLong(bidses.get(1).getCreated())));
                                }
                                if (Integer.valueOf(replace(eTxtPrice.getText().toString().trim())) > Integer.valueOf(replace(txtPriceSeller.getText().toString().trim()))
                                        && Integer.valueOf(replace(eTxtPrice.getText().toString().trim())) > Integer.valueOf(replace(txtPriceSeller1.getText().toString().trim()))
                                        && Integer.valueOf(replace(eTxtPrice.getText().toString().trim())) > Integer.valueOf(replace(txtPriceSeller2.getText().toString().trim()))
                                        && !txtMyId.getText().toString().equals(bidses.get(0).getUserid())
                                        && !txtMyId.getText().toString().equals(bidses.get(1).getUserid())
                                        && !txtMyId.getText().toString().equals(bidses.get(2).getUserid())){
                                }

                            }
                            if(bidses.size()>3){

                            }
                            Toast.makeText(itemView.getContext(),"Ваша предложения отправлена",Toast.LENGTH_LONG).show();
                            eTxtPrice.getText().clear();
                        }
                    }

                    @Override
                    public void onFailure(Call<SendPriceResponse> call, Throwable t) {
                        Toast.makeText(itemView.getContext(),t.toString(),Toast.LENGTH_LONG).show();
                    }
                });
            });
            eTxtPrice.addTextChangedListener(new NumberTextWatcher(eTxtPrice));
        }

        public void bind(int position) {
            modelName.setText(visibleItems.get(position).modelName);
            itemTimeAuction.setText(visibleItems.get(position).endtime);
            bidses = visibleItems.get(position).infoBidses;
            txtSellerId.setText("Покупатель ID:"+visibleItems.get(position).sellerId);
            txtMyId.setText(visibleItems.get(position).myId);
            id = visibleItems.get(position).id;
            if (bidses.size() == 0) {
                linearLayout.setVisibility(View.GONE);
                linearLayout1.setVisibility(View.GONE);
                linearLayout2.setVisibility(View.GONE);
                txtNotSeller.setVisibility(View.VISIBLE);
            }
            if (bidses.size() == 1) {
                linearLayout.setVisibility(View.VISIBLE);
                linearLayout1.setVisibility(View.GONE);
                linearLayout2.setVisibility(View.GONE);
                txtNotSeller.setVisibility(View.GONE);
                txtPriceSeller.setText(bidses.get(0).getPrice() + "тг");
                txtIdSeller.setText("ID:" + bidses.get(0).getUserid());
                txtCreatedPrice.setText(timestampYMD(Long.parseLong(bidses.get(0).getCreated())));
                btnShowSeller.setOnClickListener(v -> {
                    Intent intent = new Intent(context, AnketaSellerActivity.class);
                    intent.putExtra("idSeller", bidses.get(0).getUserid());
                    context.startActivity(intent);
                });
            }
            if (bidses.size() == 2) {
                linearLayout.setVisibility(View.VISIBLE);
                linearLayout1.setVisibility(View.VISIBLE);
                linearLayout2.setVisibility(View.GONE);
                txtNotSeller.setVisibility(View.GONE);
                txtPriceSeller.setText(bidses.get(0).getPrice() + "тг");
                txtIdSeller.setText("ID:" + bidses.get(0).getUserid());
                txtCreatedPrice.setText(timestampYMD(Long.parseLong(bidses.get(0).getCreated())));
                txtPriceSeller1.setText(bidses.get(1).getPrice() + "тг");
                txtIdSeller1.setText("ID:" + bidses.get(1).getUserid());
                txtCreatedPrice1.setText(timestampYMD(Long.parseLong(bidses.get(1).getCreated())));
                btnShowSeller.setOnClickListener(v -> {
                    Intent intent = new Intent(context, AnketaSellerActivity.class);
                    intent.putExtra("idSeller", bidses.get(0).getUserid());
                    context.startActivity(intent);
                });
                btnShowSeller2.setOnClickListener(v -> {
                    Intent intent = new Intent(context, AnketaSellerActivity.class);
                    intent.putExtra("idSeller", bidses.get(1).getUserid());
                    context.startActivity(intent);
                });
            }
            if (bidses.size() == 3) {
                linearLayout.setVisibility(View.VISIBLE);
                linearLayout1.setVisibility(View.VISIBLE);
                linearLayout2.setVisibility(View.VISIBLE);
                txtNotSeller.setVisibility(View.GONE);
                txtPriceSeller.setText(bidses.get(0).getPrice() + "тг");
                txtIdSeller.setText("ID:" + bidses.get(0).getUserid());
                txtCreatedPrice.setText(timestampYMD(Long.parseLong(bidses.get(0).getCreated())));
                txtPriceSeller1.setText(bidses.get(1).getPrice() + "тг");
                txtIdSeller1.setText("ID:" + bidses.get(1).getUserid());
                txtCreatedPrice1.setText(timestampYMD(Long.parseLong(bidses.get(1).getCreated())));
                txtPriceSeller2.setText(bidses.get(2).getPrice() + "тг");
                txtIdSeller2.setText("ID:" + bidses.get(2).getUserid());
                txtCreatedPrice2.setText(timestampYMD(Long.parseLong(bidses.get(2).getCreated())));
                btnShowSeller.setOnClickListener(v -> {
                    Intent intent = new Intent(context, AnketaSellerActivity.class);
                    intent.putExtra("idSeller", bidses.get(0).getUserid());
                    context.startActivity(intent);
                });
                btnShowSeller2.setOnClickListener(v -> {
                    Intent intent = new Intent(context, AnketaSellerActivity.class);
                    intent.putExtra("idSeller", bidses.get(1).getUserid());
                    context.startActivity(intent);
                });
                btnShowSeller3.setOnClickListener(v -> {
                    Intent intent = new Intent(context, AnketaSellerActivity.class);
                    intent.putExtra("idSeller", bidses.get(2).getUserid());
                    context.startActivity(intent);
                });
            }
            if (bidses.size() > 3) {
                linearLayout.setVisibility(View.VISIBLE);
                linearLayout1.setVisibility(View.VISIBLE);
                linearLayout2.setVisibility(View.VISIBLE);
                txtNotSeller.setVisibility(View.GONE);
                txtPriceSeller.setText(bidses.get(0).getPrice() + "тг");
                txtIdSeller.setText("ID:" + bidses.get(0).getUserid());
                txtCreatedPrice.setText(timestampYMD(Long.parseLong(bidses.get(0).getCreated())));
                txtPriceSeller1.setText(bidses.get(1).getPrice() + "тг");
                txtIdSeller1.setText("ID:" + bidses.get(1).getUserid());
                txtCreatedPrice1.setText(timestampYMD(Long.parseLong(bidses.get(1).getCreated())));
                txtPriceSeller2.setText(bidses.get(2).getPrice() + "тг");
                txtIdSeller2.setText("ID:" + bidses.get(2).getUserid());
                txtCreatedPrice2.setText(timestampYMD(Long.parseLong(bidses.get(2).getCreated())));
                btnShowSeller.setOnClickListener(v -> {
                    Intent intent = new Intent(context, AnketaSellerActivity.class);
                    intent.putExtra("idSeller", bidses.get(0).getUserid());
                    context.startActivity(intent);
                });

            }
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
                view = inflater1.inflate(R.layout.expandable_item_child, parent, false);
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

    public String timestamp(long time){
        int sec = (int) (time - System.currentTimeMillis()/1000);
        int day = (int) TimeUnit.SECONDS.toDays(sec);
        long hours = TimeUnit.SECONDS.toHours(sec) - (day *24);
        long minute = TimeUnit.SECONDS.toMinutes(sec) - (TimeUnit.SECONDS.toHours(sec)* 60);
        String times = day + "д:" + hours % 24 + "ч:" + minute % 60 + "м";
        return times;
    }

    public String replace(String s){
        return s.replace("т","").replace("г","").replace(" ","");
    }

    private List<BuyerRequestItem> getSampleItems() {
        List<BuyerRequestItem> items = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<InfoClient> clients = realm.where(InfoClient.class).findAll();
        String myId = clients.get(0).getId();
        RealmResults<InfoBuyerRequest> buyerRequests = realm.where(InfoBuyerRequest.class).findAll();
//        Log.d("ssss","" + buyerRequests.first().getModelname());

        for (int i = 0; i < myOrders.size(); i++) {
            items.add(new BuyerRequestItem(myOrders.get(i).getMarkname(),timestampYMD(Long.parseLong(myOrders.get(i).getCreated())),myOrders.get(i).getIsNew()));
            items.add(new BuyerRequestItem(myOrders.get(i).getId(),myOrders.get(i).getModelname(),timestamp(Long.parseLong(myOrders.get(i).getEndtime())),myOrders.get(i).getBids(),myOrders.get(i).getIsNew(), myOrders.get(i).getUserId(),myId));
        }

        return items;
    }


    public void setAnimate(LinearLayout linearLayout) {
        linearLayout.animate().alpha(1.2f).setDuration(1500).setListener(
                new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        linearLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        linearLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                }
        );
    }
    public void setAnimate2(LinearLayout linearLayout) {
        linearLayout.animate().alpha(2.0f).setDuration(800).setListener(
                new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        linearLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                }
        );
    }
}

