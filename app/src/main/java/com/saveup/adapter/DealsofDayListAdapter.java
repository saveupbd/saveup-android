package com.saveup.adapter;

import android.content.Context;
import android.graphics.Paint;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saveup.android.R;
import com.saveup.model.DealsofDayData;
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


public class DealsofDayListAdapter extends RecyclerView.Adapter<DealsofDayListAdapter.SingleItemRowHolder> {

    private ArrayList<DealsofDayData> itemsList;
    private Context mContext;
    private static MyClickListener myClickListener;
    private int position;

    public DealsofDayListAdapter(Context context, ArrayList<DealsofDayData> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public DealsofDayListAdapter.SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dealsofdayitem, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, final int i) {


        holder.deal_percentage.setText(itemsList.get(i).getProduct_percentage() + " % Off");
        holder.deal_original_price.setText(""+ SessionSave.getSession("currency_symbol", mContext)+" "+itemsList.get(i).getProduct_price() );
        holder.deal_price.setText(""+ SessionSave.getSession("currency_symbol", mContext)+" "+itemsList.get(i).getProduct_discount_price() );
        holder.deal_original_price.setPaintFlags(holder.deal_original_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        Glide.with(mContext)
                .load(itemsList.get(i).getProduct_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.noimage)
                .into(holder.productimage);


        if(itemsList.get(i).getProduct_type().equals("all_item")){
            holder.secLay.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected LinearLayout secLay;
        protected TextView deal_percentage, deal_original_price,deal_price;
        protected ImageView favoriteicon, productimage;


        public SingleItemRowHolder(View view) {
            super(view);
            this.secLay = view.findViewById(R.id.secLay);
            this.deal_percentage = view.findViewById(R.id.deal_percentage);
            this.deal_original_price = view.findViewById(R.id.deal_original_price);
            this.deal_price = view.findViewById(R.id.deal_price);
            this.productimage = view.findViewById(R.id.productimage);
            this.favoriteicon = view.findViewById(R.id.favoriteicon);

            ApplyFont.applyBold(mContext,view.findViewById(R.id.deal_percentage));
            ApplyFont.applyBold(mContext,view.findViewById(R.id.deal_price));
            ApplyFont.applyBold(mContext,view.findViewById(R.id.deal_original_price));

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
        DealsofDayListAdapter.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }

}