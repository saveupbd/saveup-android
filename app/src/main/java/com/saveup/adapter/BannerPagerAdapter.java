package com.saveup.adapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.saveup.android.R;
import com.saveup.model.BannerData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import static com.facebook.login.widget.ProfilePictureView.TAG;

/**
 * @author AAPBD on 18,March,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.adapter
 */


public class BannerPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<BannerData> itemData;

    public BannerPagerAdapter(Context context, ArrayList<BannerData> data) {
        itemData = data;
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.banneritem, collection, false);
        ImageView  banner_image = layout.findViewById(R.id.banner_image);
        Log.d(TAG, "onBindViewHolder: " +itemData.get(position).getBanner_image() );
        Glide.with(mContext)
                .load(itemData.get(position).getBanner_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.noimage)
                .into(banner_image);
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return itemData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


}
