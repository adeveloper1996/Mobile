package com.retrofit.mobile.activity;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.retrofit.mobile.R;
import com.retrofit.mobile.model.PhotoAllAdvert;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Nursultan on 04.09.2017.
 */

class SliderImageAdapter extends PagerAdapter {

    private List<PhotoAllAdvert> adverts;
    private LayoutInflater inflater;
    private Context context;

    public SliderImageAdapter(List<PhotoAllAdvert> adverts, Context context) {
        this.adverts = adverts;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slider_image_view, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image_slide);

        Picasso.with(context).load(adverts.get(position).getUrl()).placeholder(R.drawable.loadimg).error(R.drawable.nophoto).fit().centerCrop().into(imageView);

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public int getCount() {
        return adverts.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
