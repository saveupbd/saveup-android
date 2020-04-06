package com.saveup.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.saveup.android.R;
import com.saveup.model.HomeCategoryData;

import java.util.ArrayList;

/**
 /**
 * @author AAPBD on 18,March,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.adapter
 */


public class DrawerAdapterScreen extends RecyclerView.Adapter<DrawerAdapterScreen.SingleItemRowHolder> {

    private ArrayList<HomeCategoryData> itemsList;
    private Context mContext;
    private BannerListAdapter.MyClickListener myClickListener;
    private int position;

    public DrawerAdapterScreen(Context context, ArrayList<HomeCategoryData> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.draweritem, null);
        return new SingleItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, final int i) {

        String name  = itemsList.get(i).getCategory_Name();
        name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
        holder.category_Name.setText(name);


    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    class SingleItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView category_Name;


        SingleItemRowHolder(View view) {
            super(view);


            this.category_Name = view.findViewById(R.id.category_name);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (myClickListener != null) {
                myClickListener.onItemClick(getAdapterPosition(), v);
            }
        }
    }

    public void setOnItemClickListener(BannerListAdapter.MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface MyClickListener {
         void onItemClick(int position, View v);
    }

}
