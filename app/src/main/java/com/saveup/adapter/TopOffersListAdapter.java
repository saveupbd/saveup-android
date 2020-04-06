package com.saveup.adapter;

import android.content.Context;
import android.graphics.Paint;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.saveup.android.R;
import com.saveup.model.TopOffersData;
import com.saveup.utils.ApplyFont;
import com.saveup.utils.SessionSave;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * @author AAPBD on 11,March,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.adapter
 */


public class TopOffersListAdapter extends RecyclerView.Adapter<TopOffersListAdapter.SingleItemRowHolder> {

    private ArrayList<TopOffersData> itemsList;
    private Context mContext;
    private MyClickListener myClickListener;
    private int position;

    public TopOffersListAdapter(Context context, ArrayList<TopOffersData> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.topofferitem, viewGroup, false);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, final int i) {


        holder.top_percentage.setText(itemsList.get(i).getProduct_percentage() + " %OFF");
        holder.top_original_price.setText(itemsList.get(i).getProduct_price());
        holder.top_price.setText("" + SessionSave.getSession("currency_symbol", mContext) + " " + itemsList.get(i).getProduct_discount_price());
        holder.top_product_name.setText(itemsList.get(i).getProduct_title());
        holder.top_merchant_name.setText(itemsList.get(i).getMerchant_name());
        holder.top_original_price.setPaintFlags(holder.top_original_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        Glide.with(mContext)
                .load(itemsList.get(i).getProduct_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.noimage)
                .into(holder.productimage);
       /* if (i == (itemsList.size() - 1)) {
            holder.lineView.setVisibility(View.GONE);
        } else {
            holder.lineView.setVisibility(View.VISIBLE);
        }*/

        if(itemsList.get(i).getProduct_type().equals("all_item")){
            holder.top_price.setVisibility(View.GONE);
            holder.top_original_price.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView top_product_name, top_price, top_original_price, top_percentage, top_merchant_name;
        private ImageView productimage;
        private View lineView;

        public SingleItemRowHolder(View view) {
            super(view);

            this.top_product_name = view.findViewById(R.id.top_product_name);
            this.top_price = view.findViewById(R.id.top_price);
            this.top_original_price = view.findViewById(R.id.top_original_price);
            this.top_percentage = view.findViewById(R.id.top_percentage);
            this.productimage = view.findViewById(R.id.productimage);
            this.lineView = view.findViewById(R.id.lineView);
            this.top_merchant_name = view.findViewById(R.id.top_product_merchant_name);

            ApplyFont.applyFont(mContext, view.findViewById(R.id.top_product_name));
            ApplyFont.applyBold(mContext, view.findViewById(R.id.top_price));
            ApplyFont.applyBold(mContext, view.findViewById(R.id.top_original_price));
            ApplyFont.applyBold(mContext, view.findViewById(R.id.top_percentage));
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
        this.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }

}