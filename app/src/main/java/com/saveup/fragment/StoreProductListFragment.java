package com.saveup.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.saveup.activity.MainActivity;
import com.saveup.adapter.StoreProductListAdapter;
import com.saveup.android.R;
import com.saveup.model.ProductData;
import com.saveup.utils.ApplyFont;
import com.saveup.utils.SessionSave;
import com.saveup.utils.SimpleDividerItemDecoration;
import com.saveup.views.RippleView;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.facebook.login.widget.ProfilePictureView.TAG;

/**
 * @author AAPBD on 03,May,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.fragment
 */


public class StoreProductListFragment extends Fragment {


    private RippleView filter_Lay, sort_Lay;
    private RecyclerView productList;
    private View mProgressView, bottom_lay, mProgressViewLay;
    private View maintopLay, noDataLay;
    private String main_category_id = "",
            sec_category_id = "",
            sub_category_id = "",
            sub_sec_category_id = "";
    private ArrayList<ProductData> productDataArrayList = new ArrayList<>();
    String title = "", sortby = "", discount = "";
    // private int pageCount = 1;
    private GridLayoutManager gridlayoutManager;
    private Integer current_page = 1;
    private StoreProductListAdapter homeCatergoryAdap;
    private boolean isFilter = false;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout product_list_array;
    private LinearLayout bottomLay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.productlistfragment, container, false);

        gridlayoutManager = new GridLayoutManager(getActivity(), 2);

        maintopLay = view.findViewById(R.id.mainLay);
        mProgressView = view.findViewById(R.id.progressBar);
        noDataLay = view.findViewById(R.id.noDataLay);
        mProgressViewLay = view.findViewById(R.id.progressBarLay);
        bottom_lay = view.findViewById(R.id.bottom_lay);
        bottom_lay.setVisibility(View.GONE);
        sort_Lay = view.findViewById(R.id.button_sort);
        filter_Lay = view.findViewById(R.id.button_filter);
        product_list_array = view.findViewById(R.id.product_list_array);
        product_list_array.setVisibility(View.GONE);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        productList = view.findViewById(R.id.productList);
        bottomLay = view.findViewById(R.id.bottomLay);
        productList.setHasFixedSize(true);
        productList.setLayoutManager(gridlayoutManager);
        productList.setNestedScrollingEnabled(false);
        //productList.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
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
        ((MainActivity) getActivity()).searchIcon.setVisible(false);
        ((MainActivity) getActivity()).titleTxt = "Le";
        productListData();


    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getActivity().getResources().getInteger(android.R.integer.config_shortAnimTime);

            maintopLay.setVisibility(show ? View.GONE : View.VISIBLE);
            maintopLay.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    maintopLay.setVisibility(show ? View.GONE : View.VISIBLE);
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
            maintopLay.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showwhishListProgress(final boolean show) {

        mProgressViewLay.setVisibility(show ? View.VISIBLE : View.GONE);

    }

    public void productListData() {

        try {

            if (SessionSave.getSession("storedetails", getActivity()) != null) {


                productDataArrayList.clear();


                JSONObject json = new JSONObject(SessionSave.getSession("storedetails", getActivity()));
                if (json.getString("status").equals("200")) {
                    maintopLay.setVisibility(View.VISIBLE);
                    noDataLay.setVisibility(View.GONE);
                    bottomLay.setVisibility(View.VISIBLE);
                    product_list_array.setVisibility(View.VISIBLE);
                    JSONArray product_review = json.getJSONArray("product_list_by_shop");
                    if (product_review.length() > 0) {

                        for (int i = 0; i < product_review.length(); i++) {


                            String product_id = product_review.getJSONObject(i).getString("product_id");
                            String product_title = product_review.getJSONObject(i).getString("product_title");
                            String product_price = product_review.getJSONObject(i).getString("product_price");
                            String product_discount_price = product_review.getJSONObject(i).getString("product_discount_price");
                            String product_type = product_review.getJSONObject(i).getString("product_type");
                            String product_percentage = "";
                            if(product_type.equals("all_item")){
                                product_percentage = product_review.getJSONObject(i).getString("product_discount");
                            }else{
                                product_percentage = product_review.getJSONObject(i).getString("product_discount_price");
                            }
                            String product_image = product_review.getJSONObject(i).getString("product_image");
                            String pro_no_of_purchase = product_review.getJSONObject(i).getString("pro_no_of_purchase");
                            String pro_qty = product_review.getJSONObject(i).getString("pro_qty");
                            Boolean is_wishlist = product_review.getJSONObject(i).getBoolean("is_wishlist");
                            String merchant_name = product_review.getJSONObject(i).optString("merchant_name");

                            ProductData dealsData = new ProductData(product_id, product_title, product_price, product_discount_price, product_percentage,product_type
                                    , product_image, pro_no_of_purchase, pro_qty, is_wishlist, merchant_name);
                            productDataArrayList.add(dealsData);
                        }


                        System.out.println("most_popular_product " + productDataArrayList.size());

                        productList.setVisibility(View.VISIBLE);

                        homeCatergoryAdap = new StoreProductListAdapter(getActivity(), productDataArrayList, productList, StoreProductListFragment.this);
                        productList.setAdapter(homeCatergoryAdap);

                        homeCatergoryAdap.setOnItemClickListener(new StoreProductListAdapter.MyClickListener() {
                            @Override
                            public void onItemClick(int position, View v) {
                                ((MainActivity) getActivity()).isTouchFromHome = false;
                                ((MainActivity) getActivity()).titleTxt = title;
                                try {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("cat_id", productDataArrayList.get(position).getProduct_id());
                                    bundle.putString("title", productDataArrayList.get(position).getProduct_title());
                                    ProductDetailsFragment fragment2 = new ProductDetailsFragment();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.add(R.id.fragment_container, fragment2, "storelist");
                                    fragment2.setArguments(bundle);
                                    fragmentTransaction.addToBackStack("");
                                    fragmentTransaction.commit();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                    } else {

                        productList.setVisibility(View.GONE);
                        maintopLay.setVisibility(View.GONE);
                        noDataLay.setVisibility(View.VISIBLE);
                        bottomLay.setVisibility(View.VISIBLE);
                        product_list_array.setVisibility(View.GONE);


                    }
                } else {

                    maintopLay.setVisibility(View.GONE);
                    noDataLay.setVisibility(View.VISIBLE);
                    bottomLay.setVisibility(View.VISIBLE);
                    product_list_array.setVisibility(View.GONE);


                }
            } else {

                maintopLay.setVisibility(View.GONE);
                mProgressView.setVisibility(View.GONE);
                bottomLay.setVisibility(View.VISIBLE);
                product_list_array.setVisibility(View.GONE);

                ((MainActivity) getActivity()).showSnackBar(maintopLay, "" + "Sorry unable to reach server!!! ");
            }
        } catch (
                Exception ex
                )

        {
            ex.printStackTrace();

        }

    }

//    private Integer getCurrentPage() {
//        return current_page;
//    }
//
//    private void setCurrentPage(Integer current_page) {
//        this.current_page = current_page;
//    }

    public void applyFont(View view) {

        ApplyFont.applyFont(getActivity(), view.findViewById(R.id.button_sort));
        ApplyFont.applyFont(getActivity(), view.findViewById(R.id.button_filter));

    }

    public void addWishListData(JsonObject mJsonObject, int position) {
        try {

            if (mJsonObject != null) {


                Log.d(TAG, "productListData: " + current_page);

                JSONObject json = new JSONObject(mJsonObject.toString());
                if (json.getString("status").equals("200")) {

                    if (homeCatergoryAdap != null) {


                        if (json.getString("message").equalsIgnoreCase("Wish list deleted successfully!")) {
                            productDataArrayList.set(position, new ProductData(productDataArrayList.get(position).getProduct_id(), productDataArrayList.get(position).getProduct_title()
                                    , productDataArrayList.get(position).getProduct_price(), productDataArrayList.get(position).getProduct_discount_price(), productDataArrayList.get(position).getProduct_percentage(), productDataArrayList.get(position).getProduct_type()
                                    , productDataArrayList.get(position).getProduct_image(), productDataArrayList.get(position).getPro_no_of_purchase(), productDataArrayList.get(position).getPro_qty(), false, productDataArrayList.get(position).getMerchant_name()));

                            homeCatergoryAdap.notifyItemChanged(position);
                        } else {
                            productDataArrayList.set(position, new ProductData(productDataArrayList.get(position).getProduct_id(), productDataArrayList.get(position).getProduct_title()
                                    , productDataArrayList.get(position).getProduct_price(), productDataArrayList.get(position).getProduct_discount_price(), productDataArrayList.get(position).getProduct_percentage(),productDataArrayList.get(position).getProduct_type()
                                    , productDataArrayList.get(position).getProduct_image(), productDataArrayList.get(position).getPro_no_of_purchase(), productDataArrayList.get(position).getPro_qty(), true, productDataArrayList.get(position).getMerchant_name()));

                            homeCatergoryAdap.notifyItemChanged(position);
                        }


                        Snackbar.make(maintopLay, "" + json.getString("message"), Snackbar.LENGTH_SHORT).show();

                    }

                } else {
                    Snackbar.make(maintopLay, "" + json.getString("message"), Snackbar.LENGTH_SHORT).show();
                }
            } else {

                maintopLay.setVisibility(View.GONE);
                mProgressView.setVisibility(View.GONE);
                ((MainActivity) getActivity()).showSnackBar(maintopLay, "" + "Sorry unable to reach server!!! ");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
