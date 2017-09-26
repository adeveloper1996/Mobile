package com.retrofit.mobile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.retrofit.mobile.R;
import com.retrofit.mobile.model.InfoModel;

import java.util.List;

public class SellerProductListAdapter extends RecyclerView.Adapter<SellerProductListAdapter.ProductViewHolder>{
    private List<InfoModel> modelList;
    private int row;
    private Context context;

    public SellerProductListAdapter(List<InfoModel> modelList, int row, Context context) {
        this.modelList = modelList;
        this.row = row;
        this.context = context;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{
        private TextView txtModelName;

        public ProductViewHolder(View itemView){
          super(itemView);
            txtModelName = (TextView)itemView.findViewById(R.id.name_seller_product_model);
        }
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(row, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        holder.txtModelName.setText(modelList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
