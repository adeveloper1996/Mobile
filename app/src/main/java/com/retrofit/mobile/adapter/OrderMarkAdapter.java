package com.retrofit.mobile.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.retrofit.mobile.R;
import com.retrofit.mobile.model.InfoMark;
import com.retrofit.mobile.model.MakeOrder;

import java.util.List;

public class OrderMarkAdapter extends RecyclerView.Adapter<OrderMarkAdapter.MarkViewHolder>{
    private List<InfoMark> infoMarkList;
    private int rowLayout;
    private Context context;
    private int lastCheckedPosition = -1;
    private MakeOrder makeOrder;

    public OrderMarkAdapter(List<InfoMark> infoMarkList, int rowLayout, Context context) {
        this.infoMarkList = infoMarkList;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    public class MarkViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNameMark;
        private RadioButton radioButton;
        private String markId;

        public MarkViewHolder(View itemView) {
            super(itemView);
            txtNameMark = (TextView)itemView.findViewById(R.id.name_mark);
            radioButton = (RadioButton)itemView.findViewById(R.id.rBtnMark);
            radioButton.setOnClickListener(v -> {
                lastCheckedPosition = getAdapterPosition();
                notifyItemRangeChanged(0, infoMarkList.size());
                makeOrder = new MakeOrder(markId);
            });
        }
    }

    @Override
    public MarkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout,parent,false);
        return new MarkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MarkViewHolder holder, int position) {
        holder.txtNameMark.setText(infoMarkList.get(position).getName());
        holder.radioButton.setChecked(position == lastCheckedPosition);
        holder.markId = infoMarkList.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return infoMarkList.size();
    }

}
