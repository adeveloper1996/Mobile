package com.retrofit.mobile.adapter;

import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.retrofit.mobile.R;
import com.retrofit.mobile.activity.AnketaSellerActivity;
import com.retrofit.mobile.fragment.CloseAuctionDialogFragment;
import com.retrofit.mobile.fragment.SettingsAuctionDialogFragnment;
import com.retrofit.mobile.model.InfoBids;
import com.retrofit.mobile.model.InfoMyOrder;
import com.retrofit.mobile.model.MyOrderId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class MyOrdersAdapter extends ExpandableRecyclerAdapter<MyOrdersAdapter.MyOrderItem> {
    public static final int TYPE_PERSON = 1001;
    private List<InfoMyOrder> myOrders = new ArrayList<>();
    private Context context;
    private static FragmentManager fragmentManager;

    public MyOrdersAdapter(Context context, List<InfoMyOrder> myOrders, FragmentManager fragmentManager) {
        super(context);
        this.myOrders = myOrders;
        this.context = context;
        MyOrdersAdapter.fragmentManager = fragmentManager;
        setItems(getSampleItems());
    }

    public static class MyOrderItem extends ExpandableRecyclerAdapter.ListItem {
        public String id;
        public String markName;
        public String modelName;
        public String data;
        public String endtime;
        public String status;
        public String statusDevice;
        public List<InfoBids> infoBidses;

        public MyOrderItem(String markName, String data, String statusDevice) {
            super(TYPE_HEADER);
            this.markName = markName;
            this.data = data;
            this.statusDevice = statusDevice;
        }

        public MyOrderItem(String id, String modelName, String endtime,List<InfoBids> infoBidses,String status) {
            super(TYPE_PERSON);
            this.id = id;
            this.modelName = modelName;
            this.endtime = endtime;
            this.infoBidses = infoBidses;
            this.status = status;
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
        LinearLayout llchild;
        ImageView btn_expand_toggle;
        TextView modelName;
        TextView itemTimeAuction;
        TextView itemStatusState;
        Button btnCloseAuction;
        Button btnSetting;
        Button btnShowAll;
        ImageButton btnShowSeller;
        ImageButton btnShowSeller2;
        ImageButton btnShowSeller3;
        LinearLayout linearLayout;
        LinearLayout linearLayout1;
        LinearLayout linearLayout2;
        TextView txtPriceSeller;
        TextView txtPriceSeller1;
        TextView txtPriceSeller2;
        TextView txtIdSeller;
        TextView txtIdSeller1;
        TextView txtIdSeller2;
        TextView txtCreatedPrice;
        TextView txtCreatedPrice1;
        TextView txtCreatedPrice2;
        TextView txtNotSeller;
        String id;
        List<InfoBids> bidses;
        String statusState;
        public int pos;


        public PersonViewHolder(View view) {
            super(view);
            llchild = (LinearLayout) itemView.findViewById(R.id.ll_child_my_order);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.seller1);
            linearLayout1 = (LinearLayout) itemView.findViewById(R.id.seller2);
            linearLayout2 = (LinearLayout) itemView.findViewById(R.id.seller3);
            txtPriceSeller = (TextView) itemView.findViewById(R.id.txt_my_order_price_seller1);
            txtPriceSeller1 = (TextView) itemView.findViewById(R.id.txt_my_order_price_seller2);
            txtPriceSeller2 = (TextView) itemView.findViewById(R.id.txt_my_order_price_seller3);
            txtIdSeller = (TextView) itemView.findViewById(R.id.txt_my_order_id_seller);
            txtIdSeller1 = (TextView) itemView.findViewById(R.id.txt_my_order_id_seller2);
            txtIdSeller2 = (TextView) itemView.findViewById(R.id.txt_my_order_id_seller3);
            txtCreatedPrice = (TextView) itemView.findViewById(R.id.txt_my_order_date_seller);
            txtCreatedPrice1 = (TextView) itemView.findViewById(R.id.txt_my_order_date_seller2);
            txtCreatedPrice2 = (TextView) itemView.findViewById(R.id.txt_my_order_date_seller3);
            txtNotSeller = (TextView) itemView.findViewById(R.id.txt_my_order_not_seller);
            btn_expand_toggle = (ImageView) itemView.findViewById(R.id.item_arrow);
            btnShowAll = (Button) itemView.findViewById(R.id.btn_my_order_show_all);
            modelName = (TextView) itemView.findViewById(R.id.txt_my_order_name_device);
            itemTimeAuction = (TextView) itemView.findViewById(R.id.txt_my_order_time_of_auction);
            btnShowSeller = (ImageButton) itemView.findViewById(R.id.btn_my_order_seller_anketa);
            btnShowSeller2 = (ImageButton) itemView.findViewById(R.id.btn_my_order_seller_anketa2);
            btnShowSeller3 = (ImageButton) itemView.findViewById(R.id.btn_my_order_seller_anketa3);
            btnCloseAuction = (Button) itemView.findViewById(R.id.btn_close_auction_my_order);
            btnSetting = (Button) itemView.findViewById(R.id.btn_setting_my_order);
        }

        public void bind(int position) {
            modelName.setText(visibleItems.get(position).modelName);
            itemTimeAuction.setText(visibleItems.get(position).endtime);
            bidses = visibleItems.get(position).infoBidses;
            id = visibleItems.get(position).id;
            statusState = visibleItems.get(position).status;
            pos = position;
            if (bidses.size() == 0) {
                linearLayout.setVisibility(View.VISIBLE);
                linearLayout1.setVisibility(View.VISIBLE);
                linearLayout2.setVisibility(View.VISIBLE);
                btnShowAll.setVisibility(View.GONE);
                txtNotSeller.setVisibility(View.VISIBLE);
                btnShowSeller.setOnClickListener(v -> {
                    Intent intent = new Intent(context, AnketaSellerActivity.class);
                  //  intent.putExtra("idSeller", bidses.get(0).getUserid());
                    context.startActivity(intent);
                });
            }
            if (bidses.size() == 1) {
                linearLayout.setVisibility(View.VISIBLE);
                linearLayout1.setVisibility(View.GONE);
                linearLayout2.setVisibility(View.GONE);
                txtNotSeller.setVisibility(View.GONE);
                btnShowAll.setVisibility(View.GONE);
                txtPriceSeller.setText(bidses.get(0).getPrice() + "тг");
                txtIdSeller.setText("ID:" + bidses.get(0).getUserid());
                txtCreatedPrice.setText(timestampYMD(Long.parseLong(bidses.get(0).getCreated())));
                btnShowSeller.setOnClickListener(v -> {
                    Intent intent = new Intent(context, AnketaSellerActivity.class);
                    //intent.putExtra("idSeller", bidses.get(0).getUserid());
                    context.startActivity(intent);
                });
            }
            if (bidses.size() == 2) {
                linearLayout.setVisibility(View.VISIBLE);
                linearLayout1.setVisibility(View.VISIBLE);
                linearLayout2.setVisibility(View.GONE);
                txtNotSeller.setVisibility(View.GONE);
                btnShowAll.setVisibility(View.GONE);
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
                  //  intent.putExtra("idSeller", bidses.get(1).getUserid());
                    context.startActivity(intent);
                });
            }
            if (bidses.size() == 3) {
                linearLayout.setVisibility(View.VISIBLE);
                linearLayout1.setVisibility(View.VISIBLE);
                linearLayout2.setVisibility(View.VISIBLE);
                txtNotSeller.setVisibility(View.GONE);
                btnShowAll.setVisibility(View.GONE);
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
                    //intent.putExtra("idSeller", bidses.get(0).getUserid());
                    context.startActivity(intent);
                });
                btnShowSeller2.setOnClickListener(v -> {
                    Intent intent = new Intent(context, AnketaSellerActivity.class);
                    //intent.putExtra("idSeller", bidses.get(1).getUserid());
                    context.startActivity(intent);
                });
                btnShowSeller3.setOnClickListener(v -> {
                    Intent intent = new Intent(context, AnketaSellerActivity.class);
                   // intent.putExtra("idSeller", bidses.get(2).getUserid());
                    context.startActivity(intent);
                });
            }
            if (bidses.size() > 3) {
                linearLayout.setVisibility(View.VISIBLE);
                linearLayout1.setVisibility(View.VISIBLE);
                linearLayout2.setVisibility(View.VISIBLE);
                txtNotSeller.setVisibility(View.GONE);
                btnShowAll.setVisibility(View.VISIBLE);
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
            btnCloseAuction.setOnClickListener(v -> {
                MyOrderId.setOrderId(id);
                MyOrderId.setPosOrder(position);
                new CloseAuctionDialogFragment().show(fragmentManager,"yesorno");
            });

            btnSetting.setOnClickListener(v ->{
                MyOrderId.setOrderId(id);
                new SettingsAuctionDialogFragnment().show(fragmentManager,"timechange");
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
                view = inflater1.inflate(R.layout.expandable_item_person, parent, false);
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
        Log.i("ssss","curentsec"+sec);
        int day = (int) TimeUnit.SECONDS.toDays(sec);
        long hours = TimeUnit.SECONDS.toHours(sec) - (day *24);
        long minute = TimeUnit.SECONDS.toMinutes(sec) - (TimeUnit.SECONDS.toHours(sec)* 60);
        String times = day + "д:" + hours % 24 + "ч:" + minute % 60 + "м";
        Log.d("ssss",times);
        return times;
    }

    private List<MyOrderItem> getSampleItems() {
        List<MyOrderItem> items = new ArrayList<>();

        for (int i = 0; i < myOrders.size(); i++) {
            items.add(new MyOrderItem(myOrders.get(i).getMarkname(),timestampYMD(Long.parseLong(myOrders.get(i).getCreated())),myOrders.get(i).getIsNew()));
            items.add(new MyOrderItem(myOrders.get(i).getId(),myOrders.get(i).getModelname(),timestamp(Long.parseLong(myOrders.get(i).getEndtime())),myOrders.get(i).getBidsListInfo(),myOrders.get(i).getIsNew()));
        }

        return items;
    }

}


