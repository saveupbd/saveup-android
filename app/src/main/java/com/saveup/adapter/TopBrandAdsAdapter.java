package com.saveup.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.saveup.android.R;
import com.saveup.model.AdsData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import static com.facebook.login.widget.ProfilePictureView.TAG;

/**
 * Created by AAPBD on 12/4/17.
 */

public class TopBrandAdsAdapter extends RecyclerView.Adapter<TopBrandAdsAdapter.SingleItemRowHolder> {

    private ArrayList<AdsData> itemsList;
    private Context mContext;
    private static MyClickListener myClickListener;
    private int position;

    public TopBrandAdsAdapter(Context context, ArrayList<AdsData> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.banneritem, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, final int i) {
        Log.d(TAG, "onBindViewHolder: " +itemsList.get(i).getAds_image() );
        Glide.with(mContext)
                .load(itemsList.get(i).getAds_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.noimage)
                .into(holder.banner_image);

    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected ImageView banner_image;

        public SingleItemRowHolder(View view) {
            super(view);
            this.banner_image = view.findViewById(R.id.banner_image);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (myClickListener != null) {
                myClickListener.onItemClick(getAdapterPosition(), v);
            }
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        TopBrandAdsAdapter.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }

}
