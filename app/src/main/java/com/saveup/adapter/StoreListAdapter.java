package com.saveup.adapter;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.saveup.android.R;
import com.saveup.fragment.StoreListFragment;
import com.saveup.model.StoreData;
import com.saveup.services.OnLoadMoreListener;
import com.saveup.utils.ApplyFont;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * Created by AAPBD on 26/4/17.
 */

public class StoreListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<StoreData> itemsList;
    private Context mContext;
    private MyClickListener myClickListener;
    private OnLoadMoreListener mOnLoadMoreListener;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean isLoading;

    public StoreListAdapter(Context context, ArrayList<StoreData> itemsList, RecyclerView mRecyclerView) {
        this.itemsList = itemsList;
        this.mContext = context;

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
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.storeitem, viewGroup, false);
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

        Log.wtf("cray", " onBindViewHolder "+i);

        if (holder instanceof SingleItemRowHolder) {
            final SingleItemRowHolder userViewHolder = (SingleItemRowHolder) holder;
            userViewHolder.store_name.setText(itemsList.get(i).getStore_name());

            if (itemsList.get(i).getDeal_count().equals("0")) {
                String deals = "<font color=#000000>"+ mContext.getResources().getString(R.string.deals)+"</font> <font color=#ff2353>" + "  N/A" + "</font>";
                userViewHolder.store_deal_count.setText(Html.fromHtml(deals));
            } else {
                String deals = "<font color=#000000>"+ mContext.getResources().getString(R.string.deals)+"</font> <font color=#ff2353>" + " " + itemsList.get(i).getDeal_count() + "</font>";
                userViewHolder.store_deal_count.setText(Html.fromHtml(deals));
            }

            if (itemsList.get(i).getProduct_count().equals("0")) {
                String product = "<font color=#000000>"+ mContext.getResources().getString(R.string.deals)+"</font> <font color=#ff2353>" + " N/A" + "</font>";
                userViewHolder.store_product_count.setText(Html.fromHtml(product));
            } else {
                String product = "<font color=#000000>"+ mContext.getResources().getString(R.string.deals)+"</font> <font color=#ff2353>" + " " + itemsList.get(i).getProduct_count() + "</font>";
                userViewHolder.store_product_count.setText(Html.fromHtml(product));
            }

            Glide.with(mContext)
                    .load(itemsList.get(i).getStore_img())
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


    public void setLoaded() {
        isLoading = false;
    }

}