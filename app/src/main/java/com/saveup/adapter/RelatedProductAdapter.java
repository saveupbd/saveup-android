package com.saveup.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.saveup.android.R;
import com.saveup.model.MostPopularData;
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

public class RelatedProductAdapter extends RecyclerView.Adapter<RelatedProductAdapter.SingleItemRowHolder> {

    private ArrayList<MostPopularData> itemsList;
    private Context mContext;
    private static MyClickListener myClickListener;
    private int position;

    public RelatedProductAdapter(Context context, ArrayList<MostPopularData> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.relatedpopularitem, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, final int i) {


        holder.product_price.setText(""+ SessionSave.getSession("currency_symbol", mContext)+" "+itemsList.get(i).getProduct_discount_price() );
        holder.product_name.setText(itemsList.get(i).getProduct_title() );
        holder.product_merchant_name.setText(itemsList.get(i).getMerchant_name() );
        holder.product_percentage.setText(itemsList.get(i).getProduct_percentage() + " %OFF");
        holder.product_discount_price.setText(""+ SessionSave.getSession("currency_symbol", mContext)+" "+itemsList.get(i).getProduct_discount_price() );


        System.out.println("check_data>>>marcent_name>>>>"+itemsList.get(i).getMerchant_name()+"product_name>>>>"+itemsList.get(i).getProduct_title());

        Glide.with(mContext)
                .load(itemsList.get(i).getProduct_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.noimage)
                .into(holder.productimage);


        /*if((i%2)==0){
            holder.popularLine1.setVisibility(View.VISIBLE);
        }
        else{
            holder.popularLine1.setVisibility(View.GONE);
        }*/
        holder.popularLine1.setVisibility(View.GONE);
        holder.popularLine.setVisibility(View.GONE);

        if(itemsList.get(i).getProduct_type().equals("all_item")){
            holder.product_price.setVisibility(View.GONE);
            holder.product_discount_price.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView product_name, product_price, product_percentage,product_discount_price, product_merchant_name;
        private ImageView productimage;
        private View popularLine,popularLine1;


        public SingleItemRowHolder(View view) {
            super(view);

            this.product_name = view.findViewById(R.id.product_name);
            this.product_price = view.findViewById(R.id.product_price);
            this.productimage = view.findViewById(R.id.productimage);
            this.popularLine = view.findViewById(R.id.popularLine);
            this.popularLine1 = view.findViewById(R.id.popularLine1);
            this.product_percentage = view.findViewById(R.id.popular_product_percentage);
            this.product_discount_price = view.findViewById(R.id.popular_product_discount_price);
            this.product_merchant_name = view.findViewById(R.id.product_merchant_name);

            ApplyFont.applyFont(mContext,view.findViewById(R.id.product_name));
            ApplyFont.applyFont(mContext,view.findViewById(R.id.product_price));
            ApplyFont.applyFont(mContext, product_percentage);
            ApplyFont.applyFont(mContext, product_discount_price);
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
        RelatedProductAdapter.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }

}
