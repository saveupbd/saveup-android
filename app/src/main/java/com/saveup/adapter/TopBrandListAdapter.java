package com.saveup.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.saveup.android.R;
import com.saveup.model.TopBrandData;
import com.saveup.utils.ApplyFont;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * Created by AAPBD on 12/4/17.
 */

public class TopBrandListAdapter extends RecyclerView.Adapter<TopBrandListAdapter.SingleItemRowHolder> {

    private ArrayList<TopBrandData> itemsList;
    private Context mContext;
    private static MyClickListener myClickListener;
    private int position;

    public TopBrandListAdapter(Context context, ArrayList<TopBrandData> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.branditem, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, final int i) {


        holder.brand_name.setText(itemsList.get(i).getSub_category_name());
        Glide.with(mContext)
                .load(itemsList.get(i).getSub_category_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.noimage)
                .into(holder.productimage);



    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView  brand_name;
        protected ImageView  productimage;


        public SingleItemRowHolder(View view) {
            super(view);

            this.brand_name = view.findViewById(R.id.brand_name);
            this.productimage = view.findViewById(R.id.productimage);

            ApplyFont.applyBold(mContext,view.findViewById(R.id.brand_name));
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
        TopBrandListAdapter.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }

}