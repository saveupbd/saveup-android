package com.saveup.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.saveup.android.R;
import com.saveup.model.OrderDetailsModel;

import java.util.ArrayList;

/**
 * @author AAPBD on 11,March,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.adapter
 */


public class OrderListDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<OrderDetailsModel> itemsList;
    private Context mContext;
    private MyClickListener myClickListener;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public OrderListDetailsAdapter(Context context, ArrayList<OrderDetailsModel> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar1);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.orders_detailsitem, viewGroup, false);
            SingleItemRowHolder mh = new SingleItemRowHolder(v);
            return mh;
        } else if (viewType == VIEW_TYPE_LOADING) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progressbar_item, viewGroup, false);
            LoadingViewHolder mh = new LoadingViewHolder(v);

            return mh;
        }
        return null;


    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int i) {

        if (holder instanceof SingleItemRowHolder) {
            final SingleItemRowHolder userViewHolder = (SingleItemRowHolder) holder;
            OrderDetailsModel model=itemsList.get(i);
            userViewHolder.order_item_name.setText(model.getProduct_title());
            userViewHolder.orderPrice.setText("Price : "+model.getProduct_price());
            userViewHolder.productQuantity.setText("Quantity : "+model.getOrder_qty());
            Glide.with(mContext)
                    .load(itemsList.get(i).getProduct_image())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.noimage)
                    .into(userViewHolder.productimage);

        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return itemsList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView order_item_name, orderPrice, productQuantity,deliveryStatus,paymentType;
        private ImageView productimage;

        public SingleItemRowHolder(View view) {
            super(view);

            this.order_item_name = view.findViewById(R.id.order_item_name);
            this.orderPrice = view.findViewById(R.id.orderPrice);
            this.productQuantity = view.findViewById(R.id.productQuantity);
            this.productimage = view.findViewById(R.id.productimage);
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