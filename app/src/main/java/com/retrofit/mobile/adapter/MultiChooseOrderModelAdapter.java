package com.retrofit.mobile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.retrofit.mobile.R;
import com.retrofit.mobile.model.InfoModel;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Nursultan on 11.07.2017.
 */

public class MultiChooseOrderModelAdapter extends RecyclerView.Adapter<MultiChooseOrderModelAdapter.MultiModelViewHolder> {

    private List<InfoModel> infoModels;
    private int rowLayout;
    private Context context;
    private int lastCheckedPosition = -1;
    private Realm realm;

    public MultiChooseOrderModelAdapter(List<InfoModel> infoModels, int rowLayout, Context context) {
        this.infoModels = infoModels;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    public class MultiModelViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNameModel;
        private CheckBox checkBox;
        private String modelid;
        private String name;

        public MultiModelViewHolder(View itemView) {
            super(itemView);
            txtNameModel = (TextView)itemView.findViewById(R.id.name_mark);
            checkBox = (CheckBox) itemView.findViewById(R.id.cBoxModel);
        }
    }

    @Override
    public MultiModelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout,parent,false);
        return new MultiModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MultiModelViewHolder holder, int position) {
        int pos = position;
        holder.txtNameModel.setText(infoModels.get(position).getName());
        holder.modelid = infoModels.get(position).getId();
        holder.name = infoModels.get(position).getName();
        holder.checkBox.setChecked(infoModels.get(position).isSelected());
        holder.checkBox.setTag(infoModels.get(position));

        holder.checkBox.setOnClickListener(v ->{
            CheckBox cb = (CheckBox) v;
            Realm realm = Realm.getDefaultInstance();
            RealmResults<InfoModel> infoModels = realm.where(InfoModel.class).findAll();
            realm.beginTransaction();
            InfoModel model = infoModels.get(0);
            model = (InfoModel) cb.getTag();
            model.setSelected(cb.isChecked());
            infoModels.get(pos).setSelected(cb.isChecked());
            realm.commitTransaction();

            Toast.makeText(v.getContext(),"Clicked on Checkbox: " + cb.getText() + " is "+ cb.isChecked(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return infoModels.size();
    }
}
