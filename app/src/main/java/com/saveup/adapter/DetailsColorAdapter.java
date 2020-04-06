package com.saveup.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.saveup.android.R;
import com.saveup.model.ColorData;
import com.github.pavlospt.roundedletterview.RoundedLetterView;

import java.util.ArrayList;

/**
 * Created by AAPBD on 13/4/17.
 */

public class DetailsColorAdapter extends RecyclerView.Adapter<DetailsColorAdapter.SingleItemRowHolder> {

    private ArrayList<ColorData> itemsList;
    private Context mContext;
    private static MyClickListener myClickListener;
    private int position;

    public DetailsColorAdapter(Context context, ArrayList<ColorData> itemsList) {
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


        holder.rlv_name_view.setTitleText("");
        if(!(itemsList.get(i).getColor_code().equals("")) &&itemsList.get(i).getColor_code().contains("#") && !(itemsList.get(i).getColor_code().contains("#FFFFFF")) ) {
            holder.rlv_name_view.setBackgroundColor(Color.parseColor(itemsList.get(i).getColor_code()));
            holder.white_background.setVisibility(View.GONE);
            holder.rlv_name_view.setVisibility(View.VISIBLE);
        }else {

            holder.white_background.setVisibility(View.VISIBLE);
            holder.rlv_name_view_white.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.rlv_name_view.setVisibility(View.GONE);
        }


        holder.sizeLay.setBackgroundColor(Color.parseColor(itemsList.get(i).getBackgroundColor()));

    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RoundedLetterView rlv_name_view,rlv_name_view_white;
        private LinearLayout sizeLay, white_background;

        public SingleItemRowHolder(View view) {
            super(view);


            this.rlv_name_view = view.findViewById(R.id.rlv_name_view);
            this.rlv_name_view_white = view.findViewById(R.id.rlv_name_view_white);
            this.sizeLay = view.findViewById(R.id.sizeLay);
            this.white_background = view.findViewById(R.id.white_background);

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
        DetailsColorAdapter.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }

}