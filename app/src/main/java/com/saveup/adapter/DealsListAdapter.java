package com.saveup.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.saveup.android.R;
import com.saveup.model.DealsListData;
import com.saveup.services.OnLoadMoreListener;
import com.saveup.utils.ApplyFont;
import com.saveup.utils.SessionSave;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by AAPBD on 19/4/17.
 */

public class DealsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private ArrayList<DealsListData> itemsList;
    private ArrayList<DealsListData> itemsfilterList;
    private Context mContext;
    private MyClickListener myClickListener;

    private OnLoadMoreListener mOnLoadMoreListener;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean isLoading;
    private Handler mHandler = new Handler();
    private List<SingleItemRowHolder> lstHolders;
    private Runnable updateRemainingTimeRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (lstHolders) {
                long currentTime = System.currentTimeMillis();
                for (SingleItemRowHolder holder : lstHolders) {
                    holder.updateTimeRemaining(currentTime);
                }
            }

        }
    };

    public DealsListAdapter(Context context, ArrayList<DealsListData> itemsList, RecyclerView mRecyclerView) {
        this.itemsList = itemsList;
        this.itemsfilterList = itemsList;
        this.mContext = context;
        lstHolders = new ArrayList<>();
        startUpdateTimer();
        if (mRecyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final GridLayoutManager linearLayoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    if (dy > 0) {
                        totalItemCount = linearLayoutManager.getItemCount();
                        lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                        //Log.e("haint", "Load More 33  " + isLoading + " " + totalItemCount + (lastVisibleItem + visibleThreshold));

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

    private void startUpdateTimer() {
        Timer tmr = new Timer();
        tmr.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(updateRemainingTimeRunnable);
            }
        }, 1000, 1000);
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar1);
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_filter_item, viewGroup, false);
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
    public int getItemViewType(int position) {
        return itemsList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder userViewHolder, final int i) {

        if (userViewHolder instanceof SingleItemRowHolder) {
            final SingleItemRowHolder holder = (SingleItemRowHolder) userViewHolder;
            holder.timerlay.setVisibility(View.VISIBLE);
            holder.top_percentage.setText(itemsList.get(i).getDeal_discount_percentage() + " %OFF");
            holder.top_original_price.setText("" + SessionSave.getSession("currency_symbol", mContext) + " " + itemsList.get(i).getDeal_original_price());
            holder.top_price.setText("" + SessionSave.getSession("currency_symbol", mContext) + " " + itemsList.get(i).getDeal_discount_price());
            holder.top_product_name.setText(itemsList.get(i).getDeal_title());
            holder.top_original_price.setPaintFlags(holder.top_original_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.wishlisticon.setVisibility(View.INVISIBLE);

            Glide.with(mContext)
                    .load(itemsList.get(i).getDeal_image())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.noimage)
                    .into(holder.productimage);
            if ((i % 2) == 0) {
                holder.lineView.setVisibility(View.VISIBLE);
            } else {
                holder.lineView.setVisibility(View.GONE);
            }
            synchronized (lstHolders) {
                lstHolders.add(holder);
            }

            holder.setData(itemsList.get(holder.getAdapterPosition()));

        } else if (userViewHolder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) userViewHolder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }

    }


    public class SingleItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView top_product_name, dayscount, top_price, top_original_price, top_percentage, hrscount, mincount, seccount;
        private ImageView productimage, wishlisticon;
        private View lineView;
        private LinearLayout timerlay, daysLay;
        CountDownTimer timer;
        DealsListData mProduct;

        public void setData(DealsListData item) {
            mProduct = item;
            // tvProduct.setText(item.name);
            updateTimeRemaining(System.currentTimeMillis());
        }

        public void updateTimeRemaining(long currentTime) {
            long timeDiff = mProduct.getEndTime() - currentTime;

            if (timeDiff > 0) {
                int seconds = (int) (timeDiff / 1000) % 60;
                int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
                int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);
                int daysLeft = (int) (timeDiff / (1000 * 60 * 60 * 24));
//                System.out.println("daysLeft " + daysLeft);
                this.hrscount.setText(String.format("%02d", hours));
                this.mincount.setText(String.format("%02d", minutes));
                this.seccount.setText(String.format("%02d", seconds));
                if (daysLeft > 0) {
                    this.dayscount.setText(String.format("%02d", daysLeft));
                } else {
                    this.daysLay.setVisibility(View.GONE);
                }
            } else {
                daysLay.setVisibility(View.GONE);
                this.hrscount.setText(String.format("%02d", 0));
                this.mincount.setText(String.format("%02d", 0));
                this.seccount.setText(String.format("%02d", 0));
            }
        }

        public SingleItemRowHolder(View view) {
            super(view);

            this.top_product_name = view.findViewById(R.id.top_product_name);
            this.top_price = view.findViewById(R.id.top_price);
            this.top_original_price = view.findViewById(R.id.top_original_price);
            this.top_percentage = view.findViewById(R.id.top_percentage);
            this.hrscount = view.findViewById(R.id.hrscount);
            this.mincount = view.findViewById(R.id.mincount);
            this.seccount = view.findViewById(R.id.seccount);
            this.dayscount = view.findViewById(R.id.dayscount);

            this.productimage = view.findViewById(R.id.productimage);
            this.wishlisticon = view.findViewById(R.id.wishlisticon);
            this.lineView = view.findViewById(R.id.lineView);
            this.timerlay = view.findViewById(R.id.timerlay);
            this.daysLay = view.findViewById(R.id.daysLay);
            ApplyFont.applyFont(mContext, view.findViewById(R.id.top_product_name));
            ApplyFont.applyBold(mContext, view.findViewById(R.id.top_original_price));
            ApplyFont.applyBold(mContext, view.findViewById(R.id.top_price));
            ApplyFont.applyBold(mContext, view.findViewById(R.id.top_percentage));
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

    /**
     * Returns the filter for the current set of autocomplete results.
     */
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                // Skip the autocomplete query if no constraints are given.
                if (constraint != null) {
                    // Query the autocomplete API for the (constraint) search string.
                    itemsList = getAutocomplete(constraint.toString());
                    if (itemsList != null) {
                        // The API successfully returned results.
                        results.values = itemsList;
                        results.count = itemsList.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    notifyDataSetChanged();
                } else {
                    // The API did not return any results, invalidate the data set.
                    //notifyDataSetInvalidated();
                    if (itemsList != null) {
                        itemsList.clear();
                        notifyDataSetChanged();
                    }
                }
            }
        };
        return filter;
    }

    private ArrayList<DealsListData> getAutocomplete(String constraint) {
        Log.i("", "Starting autocomplete query for: " + constraint);
        ArrayList<DealsListData> resultList = new ArrayList<>();
        ArrayList<DealsListData> resultListFinal = new ArrayList<>();
        try {
            for (int i = 0; i < resultList.size(); i++) {
                if (resultList.get(i).getDeal_title().contains(constraint)) {
                    resultListFinal.add(resultList.get(i));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultListFinal;
    }
}
