package com.saveup.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.saveup.android.R;
import com.saveup.model.SizeData;
import com.github.pavlospt.roundedletterview.RoundedLetterView;

import java.util.ArrayList;

/**
 * Created by AAPBD on 13/4/17.
 */

public class DetailsSizeAdapter extends RecyclerView.Adapter<DetailsSizeAdapter.SingleItemRowHolder> {

    private ArrayList<SizeData> itemsList;
    private Context mContext;
    private static MyClickListener myClickListener;
    private int position;

    public DetailsSizeAdapter(Context context, ArrayList<SizeData> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sizeitem, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, final int i) {




        holder.rlv_name_view.setTitleText("" + itemsList.get(i).getSize_name());
        holder.rlv_name_view.setBackgroundColor(mContext.getResources().getColor(R.color.lineview));

        holder.sizeLay.setBackgroundColor(Color.parseColor(itemsList.get(i).getBackgroundcolor()));
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RoundedLetterView rlv_name_view;
        private LinearLayout sizeLay;


        public SingleItemRowHolder(View view) {
            super(view);


            this.rlv_name_view = view.findViewById(R.id.rlv_name_view);
            this.sizeLay = view.findViewById(R.id.sizeLay);
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
        DetailsSizeAdapter.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }

}
