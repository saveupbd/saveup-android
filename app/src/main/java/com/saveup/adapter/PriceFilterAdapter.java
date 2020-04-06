package com.saveup.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saveup.android.R;
import com.saveup.model.FilterSortData;

import java.util.ArrayList;

/**
 * Created by AAPBD on 6/6/17.
 */

public class PriceFilterAdapter extends RecyclerView.Adapter<PriceFilterAdapter.SingleItemRowHolder> {

    private ArrayList<FilterSortData> itemsList;
    private Context mContext;
    private static MyClickListener myClickListener;
    private int position;

    public PriceFilterAdapter(Context context, ArrayList<FilterSortData> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.filter_sort_item, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, final int i) {

        holder.titleTxt.setText(itemsList.get(i).getFilter_name());

        holder.titleTxt.setTextColor(Color.parseColor(itemsList.get(i).getColor_name()));


    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView titleTxt;
        private LinearLayout filter_sort_lay;


        public SingleItemRowHolder(View view) {
            super(view);


            this.titleTxt = view.findViewById(R.id.titleTxt);
            this.filter_sort_lay = view.findViewById(R.id.filter_sort_lay);

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
        PriceFilterAdapter.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }

}
