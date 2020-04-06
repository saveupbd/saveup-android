package com.saveup.adapter;

import android.content.Context;
import android.graphics.Paint;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saveup.android.R;
import com.saveup.fragment.WishListFragment;
import com.saveup.model.WishListData;
import com.saveup.presenter.UserPresenter;
import com.saveup.services.OnLoadMoreListener;
import com.saveup.utils.ApplyFont;
import com.saveup.utils.SessionSave;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * @author AAPBD on 24,April,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.adapter
 */


public class WishListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<WishListData> itemsList;
    private Context mContext;
    private MyClickListener myClickListener;
    private int count;
    private WishListFragment cartListFragment;
    private OnLoadMoreListener mOnLoadMoreListener;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean isLoading;

    public WishListAdapter(Context context, ArrayList<WishListData> itemsList, WishListFragment fragment, RecyclerView mRecyclerView) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.cartListFragment = fragment;

        if (mRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();

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
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wishlistitem, viewGroup, false);
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
            userViewHolder.deleteIconLay.setTag(i);
            userViewHolder.wishlist_product_name.setText(itemsList.get(i).getProduct_title());
            userViewHolder.wishlist_price.setText(SessionSave.getSession("currency_symbol", mContext) + " " + itemsList.get(i).getProduct_price());
            userViewHolder.wishlist_price.setPaintFlags(userViewHolder.wishlist_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            userViewHolder.wishlist_original_price.setText(SessionSave.getSession("currency_symbol", mContext) + " " + itemsList.get(i).getProduct_discount_price());
            userViewHolder.wishlist_percentage.setText(SessionSave.getSession("currency_symbol", mContext) + " " + itemsList.get(i).getProduct_percentage() + " %OFF");


            userViewHolder.progressBar.setVisibility(View.GONE);
            userViewHolder.deleteIcon.setVisibility(View.VISIBLE);

            Glide.with(mContext)
                    .load(itemsList.get(i).getProduct_image())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.noimage)
                    .into(userViewHolder.productimage);

            userViewHolder.deleteIconLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    userViewHolder.progressBar.setVisibility(View.VISIBLE);
                    userViewHolder.deleteIcon.setVisibility(View.GONE);

                    int pos = (Integer) v.getTag();
                    if (cartListFragment != null && cartListFragment instanceof WishListFragment) {
                        UserPresenter userPresenter = new UserPresenter(cartListFragment);
                        userPresenter.removeWishList("" + SessionSave.getSession("user_id", mContext), "" + itemsList.get(i).getProduct_id(), pos);
                    }
                }
            });
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

        private TextView wishlist_product_name, wishlist_price, wishlist_original_price, wishlist_percentage;
        private ImageView productimage, deleteIcon;
        private RelativeLayout deleteIconLay;
        private ProgressBar progressBar;

        public SingleItemRowHolder(View view) {
            super(view);

            this.wishlist_product_name = view.findViewById(R.id.wishlist_product_name);
            this.wishlist_price = view.findViewById(R.id.wishlist_price);
            this.wishlist_original_price = view.findViewById(R.id.wishlist_original_price);
            this.wishlist_percentage = view.findViewById(R.id.wishlist_percentage);
            this.productimage = view.findViewById(R.id.productimage);
            this.deleteIcon = view.findViewById(R.id.deleteIcon);
            this.deleteIconLay = view.findViewById(R.id.deleteIconLay);
            this.progressBar = view.findViewById(R.id.progressBar1);
            ApplyFont.applyFont(mContext, view.findViewById(R.id.cart_product_name));
            ApplyFont.applyBold(mContext, view.findViewById(R.id.cart_price));
            ApplyFont.applyBold(mContext, view.findViewById(R.id.cart_original_price));
            ApplyFont.applyBold(mContext, view.findViewById(R.id.cart_percentage));
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