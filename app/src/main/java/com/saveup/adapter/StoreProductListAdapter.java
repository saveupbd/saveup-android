package com.saveup.adapter;

import android.content.Context;
import android.graphics.Paint;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saveup.android.R;
import com.saveup.fragment.StoreProductListFragment;
import com.saveup.model.ProductData;
import com.saveup.presenter.UserPresenter;
import com.saveup.services.OnLoadMoreListener;
import com.saveup.utils.ApplyFont;
import com.saveup.utils.SessionSave;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * @author AAPBD on 11,May,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.adapter
 */


public class StoreProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ProductData> itemsList;
    private Context mContext;
    private MyClickListener myClickListener;
    private int position;
    private OnLoadMoreListener mOnLoadMoreListener;
    public  int VIEW_TYPE_ITEM = 0;
    public  int VIEW_TYPE_LOADING = 1;
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean isLoading;
    private StoreProductListFragment productListFragment;

    public StoreProductListAdapter(Context context, ArrayList<ProductData> itemsList, RecyclerView mRecyclerView, StoreProductListFragment fragment) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.productListFragment= fragment;

        if (mRecyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final GridLayoutManager linearLayoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();

            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0) {
                        totalItemCount = linearLayoutManager.getItemCount();
                        lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                        Log.e("haint", "Load More 33  " + isLoading + " " + totalItemCount + (lastVisibleItem + visibleThreshold));
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

    public StoreProductListAdapter(Context context, ArrayList<ProductData> itemsList) {
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {

        if (holder instanceof SingleItemRowHolder) {
            final SingleItemRowHolder userViewHolder = (SingleItemRowHolder) holder;
            userViewHolder.timerlay.setVisibility(View.GONE);
            userViewHolder.top_percentage.setText(itemsList.get(i).getProduct_percentage() + " %OFF");
            userViewHolder.top_original_price.setText("" + SessionSave.getSession("currency_symbol", mContext) + " " +itemsList.get(i).getProduct_price());
            userViewHolder.top_price.setText("" + SessionSave.getSession("currency_symbol", mContext) + " " + itemsList.get(i).getProduct_discount_price());
            userViewHolder.top_product_name.setText(itemsList.get(i).getProduct_title());
            userViewHolder.top_merchant_name.setText(itemsList.get(i).getMerchant_name());
            userViewHolder.top_original_price.setPaintFlags(userViewHolder.top_original_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            Glide.with(mContext)
                    .load(itemsList.get(i).getProduct_image())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.noimage)
                    .into(userViewHolder.productimage);
            if (itemsList.get(i).is_wishlist()) {
                userViewHolder.wishlisticon.setBackgroundResource(R.drawable.favorite_icon);
            } else {
                userViewHolder.wishlisticon.setBackgroundResource(R.drawable.unfavorite_icon);
            }
            /*if ((i % 2) == 0) {
                userViewHolder.lineView.setVisibility(View.VISIBLE);
            } else {
                userViewHolder.lineView.setVisibility(View.GONE);
            }*/
            userViewHolder.lineView.setVisibility(View.GONE);

            userViewHolder.progressBar.setVisibility(View.GONE);
            userViewHolder.wishlisticon.setVisibility(View.VISIBLE);

            userViewHolder.wishlisticonLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Integer pos= (Integer) v.getTag();


                    userViewHolder.progressBar.setVisibility(View.VISIBLE);
                    userViewHolder.wishlisticon.setVisibility(View.GONE);
                    String pro_id = itemsList.get(pos).getProduct_id();

                    if(productListFragment !=null && productListFragment instanceof  StoreProductListFragment) {
                        UserPresenter userPresenter = new UserPresenter(productListFragment);
                        userPresenter.addWishListPage("" + SessionSave.getSession("user_id", mContext), "" + pro_id, pos);
                    }


                }
            });

            if(itemsList.get(i).getProduct_type().equals("all_item")){
                userViewHolder.top_price.setVisibility(View.GONE);
                userViewHolder.top_original_price.setVisibility(View.GONE);
            }
            userViewHolder.wishlisticonLay.setTag(i);
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
            loadingViewHolder.progressBar.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return itemsList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }
    public class SingleItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView top_product_name, top_price, top_original_price, top_percentage, top_merchant_name;
        private ImageView productimage, wishlisticon;
        private View lineView;
        private LinearLayout timerlay;
        private RelativeLayout wishlisticonLay;
        private ProgressBar progressBar;
        public SingleItemRowHolder(View view) {
            super(view);

            this.top_product_name = view.findViewById(R.id.top_product_name);
            this.top_price = view.findViewById(R.id.top_price);
            this.top_original_price = view.findViewById(R.id.top_original_price);
            this.top_percentage = view.findViewById(R.id.top_percentage);
            this.productimage = view.findViewById(R.id.productimage);
            this.wishlisticon = view.findViewById(R.id.wishlisticon);
            this.lineView = view.findViewById(R.id.lineView);
            this.timerlay = view.findViewById(R.id.timerlay);
            this.wishlisticonLay = view.findViewById(R.id.wishlisticonLay);
            this.progressBar = view.findViewById(R.id.progressBar1);
            this.top_merchant_name = view.findViewById(R.id.top_product_merchant_name);



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
}
