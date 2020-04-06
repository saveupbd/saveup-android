package com.saveup.fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saveup.android.R;
import com.google.gson.JsonObject;
import com.saveup.activity.MainActivity;
import com.saveup.adapter.FilterSortAdapter;
import com.saveup.adapter.ProductListAdapter;
import com.saveup.model.FilterSortData;
import com.saveup.model.ProductData;
import com.saveup.presenter.UserPresenter;
import com.saveup.utils.ApplyFont;
import com.saveup.utils.SessionSave;
import com.saveup.utils.SimpleDividerItemDecoration;
import com.saveup.views.RippleView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.facebook.login.widget.ProfilePictureView.TAG;

/**
 * @author AAPBD on 08,May,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.fragment
 */


public class ProductListFragment extends Fragment {

    private String TAG = ProductListFragment.class.getSimpleName();

    private RippleView filter_Lay, sort_Lay;
    private RecyclerView productList;
    private View mProgressView, mProgressViewLay;
    private LinearLayout bottomLay;
    private View maintopLay, noDataLay;
    private String main_category_id = "",
            sec_category_id = "",
            sub_category_id = "",
            sub_sec_category_id = "";
    private ArrayList<ProductData> productDataArrayList = new ArrayList<>();
    String title = "", sortby = "", discount = "", price_min = "", price_max = "", availability = "", page_title = "", sort_order_by = "";
    String discount_reset = "", sort_order_by_reset = "";
    // private int pageCount = 1;
    private GridLayoutManager gridlayoutManager;
    private UserPresenter userPresenter;
    private Integer current_page = 1, intialLoad = 0;
    private ProductListAdapter homeCatergoryAdap;
    private boolean isFilter = false;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout loadingLay;
    private ArrayList sortcolorArray = new ArrayList();
    private ArrayList pricecolorArray = new ArrayList();
    private ArrayList priceArray = new ArrayList();
    private LinearLayout product_list_array;
    private String viewAllProduct;

    ArrayList<FilterSortData> filterSortDatas = new ArrayList<FilterSortData>();
    private ArrayList filtercolorArray = new ArrayList();
    private String selectedSortData = "", selectedCategoryId = "";
    private SearchView search ;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.productlistfragment, container, false);
        setHasOptionsMenu(true);
        gridlayoutManager = new GridLayoutManager(getActivity(), 2);
        userPresenter = new UserPresenter(ProductListFragment.this);
        maintopLay = view.findViewById(R.id.mainLay);
        mProgressView = view.findViewById(R.id.progressBar);
        bottomLay = view.findViewById(R.id.bottomLay);
        noDataLay = view.findViewById(R.id.noDataLay);
        mProgressViewLay = view.findViewById(R.id.progressBarLay);
        sort_Lay = view.findViewById(R.id.button_sort);
        filter_Lay = view.findViewById(R.id.button_filter);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        loadingLay = view.findViewById(R.id.loadingLay);
        product_list_array = view.findViewById(R.id.product_list_array);
        loadingLay.setVisibility(View.INVISIBLE);
        product_list_array.setVisibility(View.GONE);
        productList = view.findViewById(R.id.productList);
        productList.setHasFixedSize(true);
        productList.setLayoutManager(gridlayoutManager);
        productList.setNestedScrollingEnabled(false);
        productList.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
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
        ((MainActivity) getActivity()).setTag = "productlist";
        try {
            String[] arrayList = getActivity().getResources().getStringArray(R.array.filter_lay);

            for (int i = 0; i < arrayList.length; i++) {

                filtercolorArray.add(i, "#000000");
                sortcolorArray.add(i, "#000000");
                pricecolorArray.add(i, "#000000");
            }
            priceArray.add(getResources().getString(R.string.under) + " " + SessionSave.getSession("currency_symbol", getActivity()) + "1000");
            priceArray.add(SessionSave.getSession("currency_symbol", getActivity()) + "1000 - " + SessionSave.getSession("currency_symbol", getActivity()) + "2500");
            priceArray.add(SessionSave.getSession("currency_symbol", getActivity()) + "2500 - " + SessionSave.getSession("currency_symbol", getActivity()) + "5000");
            priceArray.add(SessionSave.getSession("currency_symbol", getActivity()) + "5000 - " + SessionSave.getSession("currency_symbol", getActivity()) + "10000");
            priceArray.add(getResources().getString(R.string.over) + " " + SessionSave.getSession("currency_symbol", getActivity()) + "10000");
          //  ((MainActivity) getActivity()).searchIcon.setVisible(false);
            final MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.search_layout.setVisibility(View.GONE);
            mainActivity.menu_Name.setVisible(false);
            mainActivity.toggle.setDrawerIndicatorEnabled(false);
            mainActivity.setBadgeCount(getActivity(), SessionSave.getSession("cartCount", getActivity()));
            mainActivity.disableDrawer(true);
            final Drawable upArrow = getResources().getDrawable(R.drawable.back_icon);
            mainActivity.toggle.setHomeAsUpIndicator(upArrow);

            mainActivity.toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainActivity.onBackPressed();
                }
            });

            Bundle bundle = getArguments();

            if (bundle != null) {
                System.out.println("sec_category_id--- >  " + bundle.getString("sec_category_id"));
                main_category_id = bundle.getString("main_category_id");
                sec_category_id = bundle.getString("sec_category_id");
                sub_category_id = bundle.getString("sub_category_id");
                sub_sec_category_id = bundle.getString("sub_sec_category_id");
                title = "" + bundle.getString("title");


                discount = bundle.getString("discount");
                discount_reset = bundle.getString("discount");
                sort_order_by_reset = bundle.getString("sort_order_by");
                price_min = bundle.getString("price_min");
                price_max = bundle.getString("price_max");
                availability = bundle.getString("availability");
                page_title = "" + bundle.getString("page_title");
                sort_order_by = "" + bundle.getString("sort_order_by");
                mainActivity.productList = "" + bundle.getString("page_title");
                mainActivity.titleTxt = "" + bundle.getString("page_title");
                showProgress(true);
                if(bundle.getString("page_title").equals(getString(R.string.gift_deals))){
                    mainActivity.toolbar.setTitle("" + getString(R.string.top_picks));
                }else{
                    mainActivity.toolbar.setTitle("" + bundle.getString("page_title"));
                }
                ((MainActivity) getActivity()).toolbar.setLogo(R.drawable.transpent_logo);

                // userPresenter.productListPage("" + current_page, main_category_id, sec_category_id, sub_category_id, sub_sec_category_id, price_min, price_max, availability, sort_order_by, SessionSave.getSession("user_id", getActivity()), "" + title, discount);

                viewAllProduct = bundle.getString("view_all_btn_data");

                if (viewAllProduct != null) {
                    callProductListApiBasedOnTitle(viewAllProduct);
                    getCategoryListFromSharedPref();

                } else {
                    sort_Lay.setVisibility(View.GONE);
                    filter_Lay.setVisibility(View.GONE);
                    userPresenter.productListPage("" + current_page, main_category_id, sec_category_id, sub_category_id, sub_sec_category_id, price_min, price_max, availability, sort_order_by, SessionSave.getSession("user_id", getActivity()), "" + title, discount);
                }

            }

            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    isFilter = false;
//                    showProgress(true);
                    current_page = 1;
                    if (viewAllProduct != null) {
                        callProductListApiBasedOnTitle(viewAllProduct);
                    } else {
                        sort_Lay.setVisibility(View.GONE);
                        userPresenter.productRefreshLoadListPage("" + current_page, main_category_id, sec_category_id, sub_category_id, sub_sec_category_id, price_min, price_max, availability, sort_order_by, SessionSave.getSession("user_id", getActivity()), "" + title, discount);
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        filter_Lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (filterSortDatas.size() == filtercolorArray.size()) {
//                    for (int i = 0; i < filterSortDatas.size(); i++) {
//                        FilterSortData filterSort = new FilterSortData(filterSortDatas.get(i).getFilter_name(), filterSortDatas.get(i).getFilter_id()
//                                , filtercolorArray.get(i).toString());
//
//                        filterSortDatas.add(i, filterSort);
//                    }
//                }

                if (filterSortDatas.size() > 0) {
                    filterSortView(filterSortDatas, false);
                } else {
                    Log.e("API_RESPONSE", "empty data");
                }
            }
        });

        sort_Lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<FilterSortData> filterSortDatas = new ArrayList<FilterSortData>();
                String[] arrayList = getActivity().getResources().getStringArray(R.array.saveup_sort_lay);
                String[] arrayList1 = getActivity().getResources().getStringArray(R.array.saveup_sort_lay_id);

                for (int i = 0; i < arrayList.length; i++) {
                    FilterSortData filterSort = new FilterSortData(arrayList[i], arrayList1[i], sortcolorArray.get(i).toString());
                    filterSortDatas.add(filterSort);
                }

                filterSortView(filterSortDatas, true);
            }
        });


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        ((MainActivity) getActivity()).cartIconempty.setVisible(false);
     //   ((MainActivity) getActivity()).cartItem.setVisible(false);
        ((MainActivity) getActivity()).editIcon.setVisible(false);
        ((MainActivity) getActivity()).menu_Name.setVisible(false);
        ((MainActivity) getActivity()).searchIcon.setVisible(true);
        ((MainActivity) getActivity()).action_search.setVisible(false);
        ((MainActivity) getActivity()).action_search.expandActionView();

        // Enable Search Focus by Default

//        search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search = (SearchView) MenuItemCompat.getActionView(((MainActivity) getActivity()).action_search);
        search.setIconifiedByDefault(true);
        search.setFocusable(true);
        search.setIconified(false);
        search.requestFocusFromTouch();

        // Cursor Colour

        search.setOnCloseListener(new SearchView.OnCloseListener() {

            @Override
            public boolean onClose() {
                if(search != null){
                    search.clearFocus();
                }

               // getActivity().onBackPressed();
                return false;
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                try {

// in Fragment2, before adding Fragment3:
//                    FragmentManager fragmentManager1 = getFragmentManager();
//                    fragmentManager1.beginTransaction()
//                            .remove(SearchViewFragment.this) // "this" refers to current instance of Fragment2
//                            .commit();
//                    fragmentManager1.popBackStack();

                    Log.d(TAG, " query "+query);

                    Bundle bundle = new Bundle();
                    bundle.putString("main_category_id", "");
                    bundle.putString("sec_category_id", "");
                    bundle.putString("sub_category_id", "");
                    bundle.putString("sub_sec_category_id", "");
                    bundle.putString("title", ""+query);
                    bundle.putString("page_title",query);
                    bundle.putString("discount", "");
                    bundle.putString("price_min", "1");
                    bundle.putString("price_max", "");
                    bundle.putString("availability", "");
                    bundle.putString("sort_order_by", "1");

                    Log.wtf(TAG, " bunlde "+bundle.toString());

                    search.clearFocus();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                Log.wtf(TAG, " onQueryTextChange "+query);

                return true;

            }

        });

       // search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);


    }

    private void getCategoryListFromSharedPref() {

        try {

            if (!SessionSave.getSession("category_list", getActivity()).equals("")) {
                JSONArray category_details = new JSONArray(SessionSave.getSession("category_list", getActivity()));
                System.out.println("category_list--->" + category_details);

                if (category_details.length() > 0) {

                    filterSortDatas.clear();
                    for (int i = 0; i < category_details.length(); i++) {

                        filterSortDatas.add(new FilterSortData(category_details.getJSONObject(i).optString("category_name"),
                                category_details.getJSONObject(i).optString("category_id"), "#000000"));

                    }
                    filterSortDatas.add(new FilterSortData(getString(R.string.reset), category_details.length() + "",
                            "#000000"));

                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void callProductListApiBasedOnTitle(String page_title) {
        if (page_title.equalsIgnoreCase(getString(R.string.latest_deals))) {
            userPresenter.latestProductListPage(title);

        } else if (page_title.equalsIgnoreCase(getString(R.string.hot_deals))) {
            userPresenter.hotDealsProductListPage(title);

        } else if (page_title.equalsIgnoreCase(getString(R.string.gift_deals))) {
            userPresenter.gitftProductListPage(title);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {


        maintopLay.setVisibility(show ? View.GONE : View.VISIBLE);
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showwhishListProgress(final boolean show) {

        mProgressViewLay.setVisibility(show ? View.VISIBLE : View.GONE);

    }

    public void productListData(JsonObject jsonObject) {

        try {

            Log.d(TAG, "API_RESPONSE "+ jsonObject );

            if (jsonObject != null) {

                Log.d(TAG, "productListData: " + current_page + "isFilter " + isFilter);

                if (!isFilter) {
                    productDataArrayList.clear();
                    showProgress(false);
                } else {
                    loadingLay.setVisibility(View.INVISIBLE);
                }

                JSONObject json = new JSONObject(jsonObject.toString());
                if (json.getString("status").equals("200")) {
                    bottomLay.setVisibility(View.VISIBLE);
                    noDataLay.setVisibility(View.GONE);
                    product_list_array.setVisibility(View.VISIBLE);

                    JSONArray product_review = json.getJSONArray("product_list");
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
                            ProductData dealsData = new ProductData(product_id, product_title, product_price, product_discount_price, product_percentage,
                                    product_type  , product_image, pro_no_of_purchase, pro_qty, is_wishlist, merchant_name);
                            productDataArrayList.add(dealsData);
                        }

                        Log.d(TAG, "most_popular_product " + productDataArrayList.size());


                        productList.setVisibility(View.VISIBLE);
//                        HashSet<ProductData> hashSet = new HashSet<>();
//                        hashSet.addAll(productDataArrayList);
//                        productDataArrayList.clear();
//                        productDataArrayList.addAll(hashSet);

                        if (!isFilter) {

                            homeCatergoryAdap = new ProductListAdapter(getActivity(), productDataArrayList, productList, ProductListFragment.this);
                            productList.setAdapter(homeCatergoryAdap);
                        } else {

                            homeCatergoryAdap.notifyDataSetChanged();
                            homeCatergoryAdap.setLoaded();

                        }

                        /*
                        Saveup api doesn't support pagination yet
                         */

//                        homeCatergoryAdap.setOnLoadMoreListener(new OnLoadMoreListener() {
//                            @Override
//                            public void onLoadMore() {
//                                Log.e("homeCatergoryAdap ", "Load More" + intialLoad);
//                                isFilter = true;
//                                current_page++;
//
////                                productDataArrayList.add(null);
////                                homeCatergoryAdap.notifyItemInserted(productDataArrayList.size() - 1);
//                                loadingLay.setVisibility(View.VISIBLE);
//                                TranslateAnimation animation = new TranslateAnimation(0f, 0f, loadingLay.getHeight(), 0f);
//                                animation.setDuration(300);
//                                loadingLay.startAnimation(animation);
//                                //Remove loading item
//                                userPresenter.productListPage("" + current_page, main_category_id, sec_category_id, sub_category_id, sub_sec_category_id, price_min, price_max, availability, sort_order_by, SessionSave.getSession("user_id", getActivity()), "" + title, discount);
//
//
//                            }
//                        });

                        homeCatergoryAdap.setOnItemClickListener(new ProductListAdapter.MyClickListener() {
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
                                    fragmentTransaction.add(R.id.fragment_container, fragment2, "productlist");
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
                            bottomLay.setVisibility(View.VISIBLE);
                            noDataLay.setVisibility(View.VISIBLE);
                            product_list_array.setVisibility(View.GONE);

                        } else {

                            Snackbar.make(bottomLay, "" + json.getString("message"), Snackbar.LENGTH_SHORT).show();
                        }


                    }
                } else {

//                    if (!isFilter) {
//                        bottomLay.setVisibility(View.VISIBLE);
//                        noDataLay.setVisibility(View.VISIBLE);
//                        product_list_array.setVisibility(View.GONE);
//                    } else {
//
//                        Snackbar.make(bottomLay, "" + json.getString("message"), Snackbar.LENGTH_SHORT).show();
//                    }

                    bottomLay.setVisibility(View.VISIBLE);
                    noDataLay.setVisibility(View.VISIBLE);
                    product_list_array.setVisibility(View.GONE);
                    Snackbar.make(bottomLay, "" + json.getString("message"), Snackbar.LENGTH_SHORT).show();

                }
            } else {
                bottomLay.setVisibility(View.GONE);
                mProgressView.setVisibility(View.GONE);
                product_list_array.setVisibility(View.GONE);
                ((MainActivity) getActivity()).showSnackBar(maintopLay, getString(R.string.unable_to_reach_server));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    public void productRefreshListData(JsonObject jsonObject) {

        if (jsonObject != null) {

            Log.d(TAG, "productListData: " + current_page + "isFilter " + isFilter);
            productDataArrayList.clear();
            showProgress(false);
            productListData(jsonObject);
        }
    }


    public void applyFont(View view) {

        ApplyFont.applyFont(getActivity(), view.findViewById(R.id.button_sort));
        ApplyFont.applyFont(getActivity(), view.findViewById(R.id.button_filter));

    }


    private void filterSortView(final ArrayList<FilterSortData> arrayList, final boolean isSort) {

        final Dialog dialog = new Dialog(getActivity(), R.style.dialogwinddow);
        dialog.setContentView(R.layout.sort_filter_layout);
        // set the custom dialog components - text, image and button
        RecyclerView listView = dialog.findViewById(R.id.sort_filter_lay);
        RecyclerView filterlist = dialog.findViewById(R.id.filterlist);
        LinearLayout filterLay = dialog.findViewById(R.id.filterLay);

        TextView titleTxt = dialog.findViewById(R.id.titleTxt);

        if (isSort) {
            titleTxt.setText("" + getResources().getString(R.string.sort));
            filterLay.setVisibility(View.GONE);

        } else {

            titleTxt.setText(getString(R.string.filter));

//            ArrayList<FilterSortData> priceDatas = new ArrayList<FilterSortData>();
//            for (int i = 0; i < priceArray.size(); i++) {
//                FilterSortData filterSort = new FilterSortData(priceArray.get(i).toString(), "" + i, pricecolorArray.get(i).toString());
//                priceDatas.add(filterSort);
//            }
//            titleTxt.setText("" + getResources().getString(R.string.discount));
//            filterLay.setVisibility(View.VISIBLE);
//            filterlist.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//            filterlist.setNestedScrollingEnabled(true);
//            filterlist.setHasFixedSize(true);
//            filterlist.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
//            PriceFilterAdapter filterAdapter = new PriceFilterAdapter(getActivity(), priceDatas);
//            filterlist.setAdapter(filterAdapter);
//
//            filterAdapter.setOnItemClickListener(new PriceFilterAdapter.MyClickListener() {
//                @Override
//                public void onItemClick(int position, View v) {
//
//                    try {
//                        dialog.dismiss();
//                        current_page = 1;
//                        for (int i = 0; i < pricecolorArray.size(); i++) {
//
//                            if (i == position) {
//                                pricecolorArray.set(position, "#ff2353");
//                            } else {
//                                pricecolorArray.set(i, "#000000");
//                            }
//
//                        }
//                        if (sort_order_by.equals("") || sort_order_by.equals("5")) {
//                            sort_order_by = "4";
//                        }
//
//                        showProgress(true);
//                        isFilter = false;
//                        if (position == 0) {
//                            price_min = "0";
//                            price_max = "1000";
//                            userPresenter.productListPage("" + current_page, main_category_id, sec_category_id, sub_category_id, sub_sec_category_id, price_min, price_max, availability, sort_order_by, SessionSave.getSession("user_id", getActivity()), "" + title, "" + discount);
//
//                        } else if (position == 1) {
//                            price_min = "1000";
//                            price_max = "2500";
//                            userPresenter.productListPage("" + current_page, main_category_id, sec_category_id, sub_category_id, sub_sec_category_id, price_min, price_max, availability, sort_order_by, SessionSave.getSession("user_id", getActivity()), "" + title, "" + discount);
//
//                        } else if (position == 2) {
//                            price_min = "2500";
//                            price_max = "5000";
//                            userPresenter.productListPage("" + current_page, main_category_id, sec_category_id, sub_category_id, sub_sec_category_id, price_min, price_max, availability, sort_order_by, SessionSave.getSession("user_id", getActivity()), "" + title, "" + discount);
//
//                        } else if (position == 3) {
//                            price_min = "5000";
//                            price_max = "10000";
//                            userPresenter.productListPage("" + current_page, main_category_id, sec_category_id, sub_category_id, sub_sec_category_id, price_min, price_max, availability, sort_order_by, SessionSave.getSession("user_id", getActivity()), "" + title, "" + discount);
//
//                        } else if (position == 4) {
//                            price_min = "10000";
//                            price_max = "10000000";
//                            userPresenter.productListPage("" + current_page, main_category_id, sec_category_id, sub_category_id, sub_sec_category_id, price_min, price_max, availability, sort_order_by, SessionSave.getSession("user_id", getActivity()), "" + title, "" + discount);
//
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
        }

//        if (availability.equalsIgnoreCase("1")) {
//
//            for (int i = 0; i < arrayList.size(); i++) {
//
//                if (arrayList.get(i).getFilter_id().equals("10")) {
//                    arrayList.set(i, new FilterSortData(arrayList.get(i).getFilter_name(), arrayList.get(i).getFilter_id(), "#ff2353"));
//                }
//
//            }
//        } else {
//            for (int i = 0; i < arrayList.size(); i++) {
//
//                if (arrayList.get(i).getFilter_id().equals("10")) {
//                    arrayList.set(i, new FilterSortData(arrayList.get(i).getFilter_name(), arrayList.get(i).getFilter_id(), "#000000"));
//                }
//
//            }
//        }


        listView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        listView.setNestedScrollingEnabled(true);
        listView.setHasFixedSize(true);
        listView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        FilterSortAdapter itemsAdapter = new FilterSortAdapter(getActivity(), arrayList);
        listView.setAdapter(itemsAdapter);
        TextView cancelTxt = dialog.findViewById(R.id.cancelTxt);

        itemsAdapter.setOnItemClickListener(new FilterSortAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                try {
                    dialog.dismiss();
                    current_page = 1;
                    if (isSort) {
                        for (int i = 0; i < sortcolorArray.size(); i++) {

                            if (i == position) {
                                sortcolorArray.set(position, "#ff2353");
                            } else {
                                sortcolorArray.set(i, "#000000");
                            }

                        }


                        showProgress(true);
                        isFilter = false;
                        current_page = 1;

                        //saveup custom sort

                        switch (position) {
                            case 0:
                                selectedSortData = "low_high";
                                userPresenter.filterProductListPage(selectedSortData, viewAllProduct, getActivity(), selectedCategoryId);
                                break;
                            case 1:
                                selectedSortData = "high_low";
                                userPresenter.filterProductListPage(selectedSortData, viewAllProduct, getActivity(), selectedCategoryId);
                                break;
                            case 2:
                                if (viewAllProduct.equalsIgnoreCase(getString(R.string.latest_deals))) {
                                    userPresenter.latestProductListPage(title);

                                } else if (viewAllProduct.equalsIgnoreCase(getString(R.string.hot_deals))) {
                                    userPresenter.hotDealsProductListPage(title);

                                } else if (viewAllProduct.equalsIgnoreCase(getString(R.string.gift_deals))) {
                                    userPresenter.gitftProductListPage(title);
                                }
                                break;
                        }

//                        if (arrayList.get(position).getFilter_id().equals("7")) {
//                            sort_order_by = sort_order_by_reset;
//                            userPresenter.productListPage("" + current_page, main_category_id, sec_category_id, sub_category_id, sub_sec_category_id, price_min, price_max, availability, sort_order_by, SessionSave.getSession("user_id", getActivity()), "" + title, "" + discount);
//                        } else {
//                            sort_order_by = "" + arrayList.get(position).getFilter_id();
//                            userPresenter.productListPage("" + current_page, main_category_id, sec_category_id, sub_category_id, sub_sec_category_id, price_min, price_max, availability, sort_order_by, SessionSave.getSession("user_id", getActivity()), "" + title, "" + discount);
//                        }

                        // UserPresenter userPresenter = new UserPresenter(ProductListFragment.this);
                        //
                    } else {

//                        for (int i = 0; i < filtercolorArray.size(); i++) {
//
//
//                            if (i == position) {
//                                filtercolorArray.set(position, "#ff2353");
//                            } else {
//                                filtercolorArray.set(i, "#000000");
//                            }
//
//                        }

                        for (int i = 0; i < filterSortDatas.size(); i++) {

                            if (i == position) {
                                filterSortDatas.get(i).setColor_name("#ff2353");
                            } else {
                                filterSortDatas.get(i).setColor_name("#000000");
                            }
                        }

                        showProgress(true);
                        isFilter = false;
                        current_page = 1;

                        if (position == filterSortDatas.size() - 1) {
                            if (viewAllProduct.equalsIgnoreCase(getString(R.string.latest_deals))) {
                                userPresenter.latestProductListPage(title);

                            } else if (viewAllProduct.equalsIgnoreCase(getString(R.string.hot_deals))) {
                                userPresenter.hotDealsProductListPage(title);

                            } else if (viewAllProduct.equalsIgnoreCase(getString(R.string.gift_deals))) {
                                userPresenter.gitftProductListPage(title);
                            }
                        } else {
                            selectedCategoryId = filterSortDatas.get(position).getFilter_id();
                            userPresenter.filterProductListPage(selectedSortData, viewAllProduct, getActivity(), selectedCategoryId);
                        }

                        Log.e("API_RESPONSE", "pos: " + position + " name: " + filterSortDatas.get(position).getFilter_name());

//                        if (sort_order_by.equals("") || sort_order_by.equals("5")) {
//                            sort_order_by = "4";
//                        }
//                        showProgress(true);
//                        isFilter = false;
//                        current_page = 1;
//                        if (arrayList.get(position).getFilter_id().equals("7")) {
//                            price_min = "";
//                            price_max = "";
//                            discount = discount_reset;
//
//                            availability = "1";
//                            for (int i = 0; i < pricecolorArray.size(); i++) {
//
//                                pricecolorArray.set(i, "#000000");
//                            }
//
//                            userPresenter.productListPage("" + current_page, main_category_id, sec_category_id, sub_category_id, sub_sec_category_id, price_min, price_max, availability, sort_order_by, SessionSave.getSession("user_id", getActivity()), "" + title, "" + discount);
//
//                        } else if (arrayList.get(position).getFilter_id().equals("10")) {
//                            if (availability.equalsIgnoreCase("1")) {
//                                availability = "0";
//                            } else {
//                                availability = "1";
//                            }
//                            discount = "";
//                            userPresenter.productListPage("" + current_page, main_category_id, sec_category_id, sub_category_id, sub_sec_category_id, price_min, price_max, availability, sort_order_by, SessionSave.getSession("user_id", getActivity()), "" + title, "" + discount);
//
//                        } else {
//
//                            discount = "" + arrayList.get(position).getFilter_id();
//                            userPresenter.productListPage("" + current_page, main_category_id, sec_category_id, sub_category_id, sub_sec_category_id, price_min, price_max, availability, sort_order_by, SessionSave.getSession("user_id", getActivity()), "" + title, discount);
//                        }

                        //UserPresenter userPresenter = new UserPresenter(ProductListFragment.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        // if button is clicked, close the custom dialog
        cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    public void addWishListData(JsonObject mJsonObject, int position) {
        try {

            if (mJsonObject != null) {


                Log.d(TAG, "productListData: " + current_page);

                JSONObject json = new JSONObject(mJsonObject.toString());
                if (json.getString("status").equals("200")) {

                    if (homeCatergoryAdap != null) {


                        if (json.getString("message").equalsIgnoreCase("Wish list deleted successfully!") || json.getString("message").equalsIgnoreCase("liste de souhaits supprimée avec succés")) {
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

                bottomLay.setVisibility(View.GONE);
                mProgressView.setVisibility(View.GONE);
                ((MainActivity) getActivity()).showSnackBar(maintopLay, "" + getString(R.string.unable_to_reach_server));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
