package com.saveup.adapter;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.saveup.android.R;
import com.saveup.fragment.OrderTabDynamicFragment;
import com.saveup.model.OrderData;
import com.saveup.services.OnLoadMoreListener;
import com.saveup.utils.ApplyFont;

import java.util.ArrayList;

/**
 * @author AAPBD on 11,March,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.adapter
 */


public class OrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<OrderData> itemsList;
    private Context mContext;
    private MyClickListener myClickListener;
    private int count;
    private OrderTabDynamicFragment storeListFragment;
    private OnLoadMoreListener mOnLoadMoreListener;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean isLoading;

    public OrderListAdapter(Context context, ArrayList<OrderData> itemsList, OrderTabDynamicFragment fragment, RecyclerView mRecyclerView) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.storeListFragment = fragment;

        if (mRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();

            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0) {
                        totalItemCount = linearLayoutManager.getItemCount();
                        lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                        Log.e("haint", "Load More 33  " + isLoading + " " + totalItemCount + " " + (lastVisibleItem + visibleThreshold));


                        if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                            if (mOnLoadMoreListener != null) {
                                mOnLoadMoreListener.onLoadMore();
                            }
                            isLoading = true;
                        }
                    }
                }
            });
        }
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
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ordersitem, viewGroup, false);
            SingleItemRowHolder mh = new SingleItemRowHolder(v);
            return mh;
        } else if (viewType == VIEW_TYPE_LOADING) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progressbar_item, viewGroup, false);
            LoadingViewHolder mh = new LoadingViewHolder(v);

            return mh;
        }
        return null;


    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int i) {

        if (holder instanceof SingleItemRowHolder) {
            final SingleItemRowHolder userViewHolder = (SingleItemRowHolder) holder;
            userViewHolder.txtID.setText(itemsList.get(i).getOrder_id());
            userViewHolder.txtName.setText(itemsList.get(i).getShipping_name());
            userViewHolder.txtOrderTotal.setText(itemsList.get(i).getOrder_total());
            userViewHolder.txtOrderDate.setText(itemsList.get(i).getOrder_date());

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

        private TextView txtID, txtName, txtOrderTotal, txtOrderDate;

        public SingleItemRowHolder(View view) {
            super(view);

            this.txtID = view.findViewById(R.id.txtID);
            this.txtName = view.findViewById(R.id.txtName);
            this.txtOrderTotal = view.findViewById(R.id.txtOrderTotal);
            this.txtOrderDate = view.findViewById(R.id.txtOrderDate);

            ApplyFont.applyMediumFont(mContext, view.findViewById(R.id.txtID));
            ApplyFont.applyLightFont(mContext, view.findViewById(R.id.txtName));
            ApplyFont.applyLightFont(mContext, view.findViewById(R.id.txtOrderTotal));
            ApplyFont.applyLightFont(mContext, view.findViewById(R.id.txtOrderDate));

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


    public void setLoaded() {
        isLoading = false;
    }

}