package com.saveup.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
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
 * @author AAPBD on 10,May,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.adapter
 */


public class MostSubCategroyAdapter extends RecyclerView.Adapter<MostSubCategroyAdapter.SingleItemRowHolder> {

    private ArrayList<MostPopularData> itemsList;
    private Context mContext;
    private static MyClickListener myClickListener;
    private int position;

    public MostSubCategroyAdapter(Context context, ArrayList<MostPopularData> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mostpopularitem, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, final int i) {


        holder.product_price.setText(""+ SessionSave.getSession("currency_symbol", mContext)+" "+itemsList.get(i).getProduct_discount_price() );
        holder.product_name.setText(itemsList.get(i).getProduct_title() );

        Glide.with(mContext)
                .load(itemsList.get(i).getProduct_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.noimage)
                .into(holder.productimage);

        if((i%2)==0){
            holder.popularLine1.setVisibility(View.VISIBLE);
        }
        else{
            holder.popularLine1.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView product_name, product_price;
        private ImageView productimage;
        private View popularLine,popularLine1;


        public SingleItemRowHolder(View view) {
            super(view);

            this.product_name = view.findViewById(R.id.product_name);
            this.product_price = view.findViewById(R.id.product_price);
            this.productimage = view.findViewById(R.id.productimage);
            this.popularLine = view.findViewById(R.id.popularLine);
            this.popularLine1 = view.findViewById(R.id.popularLine1);

            ApplyFont.applyFont(mContext,view.findViewById(R.id.product_name));
            ApplyFont.applyFont(mContext,view.findViewById(R.id.product_price));
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
        MostSubCategroyAdapter.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }

}