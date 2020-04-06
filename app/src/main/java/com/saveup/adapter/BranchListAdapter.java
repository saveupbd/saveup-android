package com.saveup.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.saveup.android.R;
import com.saveup.model.StoreData;
import com.saveup.utils.ApplyFont;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * @author AAPBD on 3, may,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.adapter
 */


public class BranchListAdapter extends RecyclerView.Adapter<BranchListAdapter.SingleItemRowHolder> {

    private ArrayList<StoreData> itemsList;
    private Context mContext;
    private MyClickListener myClickListener;
    private int count;


    public BranchListAdapter(Context context, ArrayList<StoreData> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;


    }


    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.storeitem, viewGroup, false);
            SingleItemRowHolder mh = new SingleItemRowHolder(v);
            return mh;
    }



    @Override
    public void onBindViewHolder(final SingleItemRowHolder userViewHolder, final int i) {

            userViewHolder.store_name.setText(itemsList.get(i).getStore_name());

            if(itemsList.get(i).getDeal_count().equals("0")) {
                String deals = "<font color=#000000>Deals</font> <font color=#ff2353>" + "  N/A"  + "</font>";
                userViewHolder.store_deal_count.setText(Html.fromHtml(deals));
            }else {
                String deals = "<font color=#000000>Deals</font> <font color=#ff2353>" + " " + itemsList.get(i).getDeal_count() + "</font>";
                userViewHolder.store_deal_count.setText(Html.fromHtml(deals));
            }

            if(itemsList.get(i).getProduct_count().equals("0")) {
                String product = "<font color=#000000>Deals</font> <font color=#ff2353>" + " N/A" + "</font>";
                userViewHolder.store_product_count.setText(Html.fromHtml(product));
            }else {
                String product = "<font color=#000000>Deals</font> <font color=#ff2353>" + " " + itemsList.get(i).getProduct_count() + "</font>";
                userViewHolder.store_product_count.setText(Html.fromHtml(product));
            }

            Glide.with(mContext)
                    .load(itemsList.get(i).getStore_img())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.noimage)
                    .into(userViewHolder.productimage);

    }


    public class SingleItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView store_name, store_deal_count, store_product_count;
        private ImageView productimage;

        public SingleItemRowHolder(View view) {
            super(view);

            this.store_name = view.findViewById(R.id.store_name);
            this.store_deal_count = view.findViewById(R.id.store_deal_count);
            this.store_product_count = view.findViewById(R.id.store_product_count);
            this.productimage = view.findViewById(R.id.productimage);

            ApplyFont.applyBold(mContext, view.findViewById(R.id.store_name));
            ApplyFont.applyLightFont(mContext, view.findViewById(R.id.store_deal_count));
            ApplyFont.applyLightFont(mContext, view.findViewById(R.id.store_product_count));

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

    @Override
    public int getItemCount() {
        return itemsList == null ? 0 : itemsList.size();
    }




}
