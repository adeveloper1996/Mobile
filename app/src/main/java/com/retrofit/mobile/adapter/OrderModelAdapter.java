package com.retrofit.mobile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.retrofit.mobile.R;
import com.retrofit.mobile.model.InfoModel;
import com.retrofit.mobile.model.MakeOrder;

import java.util.List;

/**
 * Created by Nursultan on 11.07.2017.
 */

public class OrderModelAdapter extends RecyclerView.Adapter<OrderModelAdapter.ModelViewHolder> {
    private List<InfoModel> infoModels;
    private int rowLayout;
    private Context context;
    private int lastCheckedPosition = -1;

    public OrderModelAdapter(List<InfoModel> infoModels, int rowLayout, Context context) {
        this.infoModels = infoModels;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    public class ModelViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNameModel;
        private RadioButton radioButton;
        private String modelId;
        private String name;

        public ModelViewHolder(View itemView) {
            super(itemView);
            txtNameModel = (TextView)itemView.findViewById(R.id.name_mark);
            radioButton = (RadioButton) itemView.findViewById(R.id.rBtnMark);
            radioButton.setOnClickListener(v-> {
                lastCheckedPosition = getAdapterPosition();
                notifyItemRangeChanged(0, infoModels.size());
                MakeOrder.setNameDevice(name);
                MakeOrder.setModelId(modelId);
            });
        }
    }

    @Override
    public ModelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ModelViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ModelViewHolder holder, int position) {
        holder.txtNameModel.setText(infoModels.get(position).getName());
        holder.radioButton.setChecked(position == lastCheckedPosition);
        holder.modelId = infoModels.get(position).getId();
        holder.name = infoModels.get(position).getName();
    }

    @Override
    public int getItemCount() {
        return infoModels.size();
    }
}
