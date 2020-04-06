package com.saveup.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.saveup.android.R;
import com.saveup.model.ReviewData;
import com.saveup.utils.ApplyFont;

import java.util.ArrayList;

/**
 * @author AAPBD on 15,April,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.adapter
 */


public class ReviewCommentsAdapter extends RecyclerView.Adapter<ReviewCommentsAdapter.SingleItemRowHolder> {

    private ArrayList<ReviewData> itemsList;
    private Context mContext;
    private MyClickListener myClickListener;
    private int position;

    public ReviewCommentsAdapter(Context context, ArrayList<ReviewData> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reviewitem, viewGroup, false);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, final int i) {


        holder.userName.setText(itemsList.get(i).getUser_name() +"\n"+itemsList.get(i).getReview_title());
        holder.comments.setText(itemsList.get(i).getReview_comments() );
        if(!(itemsList.get(i).getRatings().equals("")))
        holder.starRating.setRating(Float.valueOf(itemsList.get(i).getRatings()));

    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView userName, comments;
        private RatingBar starRating;

        public SingleItemRowHolder(View view) {
            super(view);

            this.userName = view.findViewById(R.id.userName);
            this.comments = view.findViewById(R.id.comments);
            this.starRating = view.findViewById(R.id.starRating);



            ApplyFont.applyMediumFont(mContext, view.findViewById(R.id.userName));
            ApplyFont.applyLightFont(mContext, view.findViewById(R.id.comments));

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
