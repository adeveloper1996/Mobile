package com.retrofit.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.retrofit.mobile.R;
import com.retrofit.mobile.model.InfoCity;
import com.retrofit.mobile.model.InfoModel;

import java.util.List;

/**
 * Created by Nursultan on 12.07.2017.
 */

public class SpinnerCityAdapter extends ArrayAdapter{
    private Context context;
    private int textViewResourceId;
    private List<InfoCity> objects;
    private InfoModel firstElement;
    boolean isFirstTime;

    public SpinnerCityAdapter(Context context, int textViewResourceId, List<InfoCity> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.textViewResourceId = textViewResourceId;
        this.objects = objects;
        this.isFirstTime = true;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        notifyDataSetChanged();
        return getCustomView(position, convertView, parent);

    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.spinner_item, parent, false);
        TextView label = (TextView) row.findViewById(R.id.spinner_text);
        label.setText(objects.get(position).getName());
        return row;

    }
}
