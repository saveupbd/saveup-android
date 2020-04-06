package com.saveup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.saveup.android.R;
import com.saveup.model.HomeCategoryData;
import com.saveup.utils.ApplyFont;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



/**
 * @author AAPBD on 11,March,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.adapter
 */

public class HomeCategoryListAdapter extends RecyclerView.Adapter<HomeCategoryListAdapter.SingleItemRowHolder> {

    private ArrayList<HomeCategoryData> itemsList;
    private Context mContext;
    private MyClickListener myClickListener;

    public HomeCategoryListAdapter(Context context, ArrayList<HomeCategoryData> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.categoryitem, viewGroup, false);
        return new SingleItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, final int i) {

        String name  = itemsList.get(i).getCategory_Name();
        name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();

        holder.category_name.setText(name);

        if(itemsList.get(i).getCategory_image().equals("")){

            holder.category_image.setImageResource(R.drawable.noimage);
        }else {
            Picasso.get().load((itemsList.get(i).getCategory_image())).error(R.drawable.noimage).into(holder.category_image);
        }
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView category_name;
        private ImageView category_image;

        SingleItemRowHolder(View view) {
            super(view);
            this.category_name = view.findViewById(R.id.category_name);
            this.category_image = view.findViewById(R.id.category_image);
            ApplyFont.applyFont(mContext,view.findViewById(R.id.category_name) );
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