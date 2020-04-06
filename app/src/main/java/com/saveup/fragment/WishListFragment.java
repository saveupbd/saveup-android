package com.saveup.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saveup.activity.MainActivity;
import com.saveup.adapter.WishListAdapter;
import com.saveup.android.R;
import com.saveup.model.WishListData;
import com.saveup.presenter.UserPresenter;
import com.saveup.utils.ApplyFont;
import com.saveup.utils.SessionSave;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.facebook.login.widget.ProfilePictureView.TAG;

/**
 * @author AAPBD on 24,April,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.fragment
 */


public class WishListFragment extends Fragment {

    private RecyclerView wishList;
    private View mProgressView;
    private View nestedScrollView, noDataLay;

    private ArrayList<WishListData> wishListDataArrayList = new ArrayList<>();
    String title = "", sortby = "", discount = "";
    // private int pageCount = 1;
    private LinearLayoutManager linearLayoutManager;
    private UserPresenter userPresenter;
    private Integer current_page = 1;
    private WishListAdapter homeCatergoryAdap;
    private boolean isFilter = false;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout loadingLay;
    private TextView noDatatext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.wishlistfragment, container, false);

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        userPresenter = new UserPresenter(WishListFragment.this);
        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        mProgressView = view.findViewById(R.id.progressBar);
        noDataLay = view.findViewById(R.id.noDataLay);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        wishList = view.findViewById(R.id.wishList);
        loadingLay = view.findViewById(R.id.loadingLay);
        noDatatext = view.findViewById(R.id.noDatatext);
        loadingLay.setVisibility(View.INVISIBLE);
//        wishList.setHasFixedSize(true);
        wishList.setLayoutManager(linearLayoutManager);
        wishList.setNestedScrollingEnabled(false);
        applyFont(view);
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).setTag = "wishlist";
        try {
            ((MainActivity) getActivity()).searchIcon.setVisible(false);
            final MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.search_layout.setVisibility(View.GONE);
            mainActivity.menu_Name.setVisible(false);
            mainActivity.toggle.setDrawerIndicatorEnabled(false);
            mainActivity.toolbar.setVisibility(View.VISIBLE);
            mainActivity.disableDrawer(true);
            mainActivity.toolbar.setTitle("" + getResources().getString(R.string.myWishlist));
            ((MainActivity) getActivity()).toolbar.setLogo(R.drawable.transpent_logo);
            final Drawable upArrow = getResources().getDrawable(R.drawable.back_icon);
            mainActivity.toggle.setHomeAsUpIndicator(upArrow);
            mainActivity.toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mainActivity.onBackPressed();
                }
            });

            mainActivity.setBadgeCount(getActivity(), SessionSave.getSession("cartCount", getActivity()));

            showProgress(true);
            // UserPresenter userPresenter = new UserPresenter(ProductListFragment.this);
            userPresenter.wishListPage("" + current_page, SessionSave.getSession("user_id", getActivity()));


            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    isFilter = false;
                    wishListDataArrayList.clear();
                    current_page = 1;
                    userPresenter.wishListPage("" + current_page, SessionSave.getSession("user_id", getActivity()));
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getActivity().getResources().getInteger(android.R.integer.config_shortAnimTime);

            nestedScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
            nestedScrollView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    nestedScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            nestedScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void applyFont(View view) {

        ApplyFont.applyFont(getActivity(), view.findViewById(R.id.noDataLay));

    }

    public void wishListData(JsonObject jsonObject) {

        try {

            if (jsonObject != null) {


                Log.d(TAG, "productListData: " + current_page);

                if (!isFilter) {
                    wishListDataArrayList.clear();
                    showProgress(false);
                } else {
                    loadingLay.setVisibility(View.INVISIBLE);
//                    wishListDataArrayList.remove(wishListDataArrayList.size() - 1);
//                    homeCatergoryAdap.notifyItemRemoved(wishListDataArrayList.size());

                }


                JSONObject json = new JSONObject(jsonObject.toString());
                if (json.getString("status").equals("200")) {
                    nestedScrollView.setVisibility(View.VISIBLE);
                    noDataLay.setVisibility(View.GONE);

                    JSONArray product_review = json.getJSONArray("product_wish_list");
                    if (product_review.length() > 0) {

                        for (int i = 0; i < product_review.length(); i++) {

                            String product_id = product_review.getJSONObject(i).getString("product_id");
                            String product_title = product_review.getJSONObject(i).getString("product_title");
                            String product_price = product_review.getJSONObject(i).getString("product_original_price");
                            String product_discount_price = product_review.getJSONObject(i).getString("product_discount_price");
                            String product_percentage = product_review.getJSONObject(i).getString("product_discount_percentage");
                            String product_image = product_review.getJSONObject(i).getString("product_image");

                            WishListData dealsData = new WishListData(product_id, product_title, product_price, product_discount_price, product_percentage
                                    , product_image);
                            wishListDataArrayList.add(dealsData);
                        }


                        System.out.println("most_popular_product " + wishListDataArrayList.size());

                        wishList.setVisibility(View.VISIBLE);

                        if (!isFilter) {
                            homeCatergoryAdap = new WishListAdapter(getActivity(), wishListDataArrayList, WishListFragment.this, wishList);
                            wishList.setAdapter(homeCatergoryAdap);
                        } else {

                            homeCatergoryAdap.notifyDataSetChanged();
                            homeCatergoryAdap.setLoaded();

                        }

                        //saveup doesn't support pagination yet

//                        homeCatergoryAdap.setOnLoadMoreListener(new OnLoadMoreListener() {
//                            @Override
//                            public void onLoadMore() {
//                                Log.e("haint", "Load More");
//                                isFilter = true;
//                                current_page++;
//                                loadingLay.setVisibility(View.VISIBLE);
//                                TranslateAnimation animation = new TranslateAnimation(0f, 0f, loadingLay.getHeight(), 0f);
//                                animation.setDuration(300);
//                                loadingLay.startAnimation(animation);
//                                userPresenter.wishListPage("" + current_page, SessionSave.getSession("user_id", getActivity()));
//
//                            }
//                        });

                        homeCatergoryAdap.setOnItemClickListener(new WishListAdapter.MyClickListener() {
                            @Override
                            public void onItemClick(int position, View v) {
                                ((MainActivity) getActivity()).isTouchFromHome = false;
                                ((MainActivity) getActivity()).titleTxt = title;
                                try {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("cat_id", wishListDataArrayList.get(position).getProduct_id());
                                    bundle.putString("title", wishListDataArrayList.get(position).getProduct_title());
                                    ProductDetailsFragment fragment2 = new ProductDetailsFragment();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.add(R.id.fragment_container, fragment2, "wishlist");
                                    fragment2.setArguments(bundle);
                                    fragmentTransaction.addToBackStack("");
                                    fragmentTransaction.commit();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                    } else {

                        if (!isFilter) {
                            wishList.setVisibility(View.GONE);
                            nestedScrollView.setVisibility(View.GONE);
                            noDataLay.setVisibility(View.VISIBLE);

                            noDatatext.setText("No wishlist available ");
                        } else {

//                            Snackbar.make(nestedScrollView, "" + json.getString("message"), Snackbar.LENGTH_SHORT).show();
                        }


                    }
                } else {

                    if (!isFilter) {
                        nestedScrollView.setVisibility(View.GONE);
                        noDataLay.setVisibility(View.VISIBLE);
                        noDatatext.setText("No wishlist available ");
                    } else {

//                        Snackbar.make(nestedScrollView, "" + json.getString("message"), Snackbar.LENGTH_SHORT).show();
                    }


                }
            } else {

                nestedScrollView.setVisibility(View.GONE);
                mProgressView.setVisibility(View.GONE);
                ((MainActivity) getActivity()).showSnackBar(nestedScrollView, "" + "Sorry unable to reach server!!! ");
            }
        } catch (
                Exception ex
                )

        {
            ex.printStackTrace();

        }

    }


    public void removeWishListData(JsonObject mJsonObject, int position) {
        try {
            if (mJsonObject != null) {


                Log.d("23.0.3", "productListData: " + position);


                JSONObject json = new JSONObject(mJsonObject.toString());
                if (json.getString("status").equals("200")) {

                    wishListDataArrayList.remove(position);
                    wishList.removeViewAt(position);
                    homeCatergoryAdap.notifyDataSetChanged();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }
}
