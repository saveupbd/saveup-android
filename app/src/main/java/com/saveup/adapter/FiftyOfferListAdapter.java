package com.saveup.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.saveup.android.R;
import com.saveup.model.FiftyOfferData;
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


public class FiftyOfferListAdapter extends RecyclerView.Adapter<FiftyOfferListAdapter.SingleItemRowHolder> {

    private ArrayList<FiftyOfferData> itemsList;
    private Context mContext;
    private static MyClickListener myClickListener;
    private int position;

    public FiftyOfferListAdapter(Context context, ArrayList<FiftyOfferData> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fiftyofferdata, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, final int i) {


        holder.fifty_product_name.setText(itemsList.get(i).getProduct_title());
        holder.fifty_product_merchant_name.setText(itemsList.get(i).getMerchant_name());
        holder.fifty_product_price.setText("" + SessionSave.getSession("currency_symbol", mContext) + " " + itemsList.get(i).getProduct_discount_price());
        holder.fifyt_product_percentage.setText(itemsList.get(i).getProduct_percentage() + " %OFF");
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
        holder.lineView.setVisibility(View.GONE);

        if(itemsList.get(i).getProduct_type().equals("all_item")){
            holder.fifty_product_price.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView fifty_product_name, fifty_product_price, fifyt_product_percentage, fifty_product_merchant_name;
        private ImageView productimage;
        private View lineView;


        public SingleItemRowHolder(View view) {
            super(view);

            this.fifty_product_name = view.findViewById(R.id.fifty_product_name);
            this.fifty_product_price = view.findViewById(R.id.fifty_product_price);
            this.fifyt_product_percentage = view.findViewById(R.id.fifyt_product_percentage);
            this.productimage = view.findViewById(R.id.productimage);
            this.lineView = view.findViewById(R.id.lineView);
            this.fifty_product_merchant_name = view.findViewById(R.id.fifty_product_merchant_name);

            ApplyFont.applyFont(mContext, view.findViewById(R.id.fifty_product_name));
            ApplyFont.applyBold(mContext, view.findViewById(R.id.fifty_product_price));
            ApplyFont.applyBold(mContext, view.findViewById(R.id.fifyt_product_percentage));
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
        FiftyOfferListAdapter.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }

}
