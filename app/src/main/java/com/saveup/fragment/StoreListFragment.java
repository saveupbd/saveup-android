package com.saveup.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
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
import com.saveup.adapter.FilterSortAdapter;
import com.saveup.adapter.StoreListAdapter;
import com.saveup.android.R;
import com.saveup.model.FilterSortData;
import com.saveup.model.StoreData;
import com.saveup.model.SubCategoryData;
import com.saveup.presenter.UserPresenter;
import com.saveup.utils.ApplyFont;
import com.saveup.utils.SessionSave;
import com.google.gson.JsonObject;
import com.saveup.utils.SimpleDividerItemDecoration;
import com.saveup.views.RippleView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AAPBD on 26/4/17.
 */

public class StoreListFragment extends Fragment implements TabLayout.OnTabSelectedListener {

    public static final String TAG = StoreListFragment.class.getSimpleName();
    private RecyclerView storeList;
    public View mProgressView;
    public View nestedScrollView, noDataLay;

    private ArrayList<StoreData> storeDataArrayList = new ArrayList<>();
    String title = "", sortby = "", discount = "";
    // private int pageCount = 1;
    private LinearLayoutManager linearLayoutManager;
    private UserPresenter userPresenter;
    private Integer current_page = 1;
    private StoreListAdapter homeCatergoryAdap;
    private boolean isFilter = false;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RippleView marchentSortBtn;
    // TabLayoutView
    public View tabviewLayout;
    public View tabMainLayout;
    private TabLayout tabsLayout;
    private ViewPager storedetailsViewer;
    private CustomPagerAdapter mCustomPagerAdapter;
    private LinearLayout loadingLay;
    public boolean isfromMap = false;
    public boolean isforSearch = false;
    public boolean isfromRewards = false;

    private ArrayList<FilterSortData> filterSortDatas = new ArrayList<FilterSortData>();
    private ArrayList sortcolorArray = new ArrayList();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.storelistfragment, container, false);

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        userPresenter = new UserPresenter(StoreListFragment.this);
        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        tabviewLayout = view.findViewById(R.id.tabviewLayout);
        tabMainLayout = view.findViewById(R.id.tabsmainLayout);
        mProgressView = view.findViewById(R.id.progressBar);
        noDataLay = view.findViewById(R.id.noDataLay);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        loadingLay = view.findViewById(R.id.loadingLay);
        loadingLay.setVisibility(View.INVISIBLE);
        storeList = view.findViewById(R.id.storeList);
        marchentSortBtn = view.findViewById(R.id.marchent_sort_btn);

        storedetailsViewer = view.findViewById(R.id.storedetailsViewer);
        tabsLayout = view.findViewById(R.id.tabsLayout);
        storeList.setHasFixedSize(true);
        storeList.setLayoutManager(linearLayoutManager);
        storeList.setNestedScrollingEnabled(false);
        applyFont(view);
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        getCategoryListFromSharedPref();

        marchentSortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (filterSortDatas.size() > 0) {
                    filterSortView(filterSortDatas);
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).setTag = "stores";
        ((MainActivity) getActivity()).searchIcon.setVisible(false);
        try {


            Bundle bundle = getArguments();
            isfromMap = bundle.getBoolean("isfromMap");
            isforSearch = bundle.getBoolean("isforSearch");
            isfromRewards = bundle.getBoolean("isFromRewards");
            String store_id = bundle.getString("store_id");
            System.out.println("Info window clicked isfromMap " + isfromMap);
            System.out.println("Info window clicked Store_id " + store_id);
            // UserPresenter userPresenter = new UserPresenter(ProductListFragment.this);

            final MainActivity mainActivity = (MainActivity) getActivity();
            if(!isforSearch){
                mainActivity.search_layout.setVisibility(View.GONE);
            }
            mainActivity.menu_Name.setVisible(false);

            mainActivity.toggle.setDrawerIndicatorEnabled(false);
            mainActivity.toolbar.setVisibility(View.VISIBLE);
            mainActivity.disableDrawer(false);
            mainActivity.toolbar.setTitle(getString(R.string.merchant));
            ((MainActivity) getActivity()).toolbar.setLogo(R.drawable.transpent_logo);
            final Drawable upArrow = getResources().getDrawable(R.drawable.back_icon);
            mainActivity.toggle.setHomeAsUpIndicator(upArrow);
            mainActivity.toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mainActivity.onBackPressed();
                }
            });

            showProgress(true);



            if (!isfromMap) {
                userPresenter.storeListPage("" + current_page);
            } else {
                showProgress(true);
                //UserPresenter userPresenter = new UserPresenter(StoreListFragment.this);
                userPresenter.shopdetailsPage(store_id);

            }

            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mSwipeRefreshLayout.setRefreshing(false);
                    isFilter = false;
                    showProgress(true);
                    current_page = 1;
                    userPresenter.storeListPage("" + current_page);
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

    public void applyFont(View view) {

        ApplyFont.applyFont(getActivity(), view.findViewById(R.id.noDataLay));

    }

    public void storeListData(JsonObject jsonObject) {

        try {

            if (jsonObject != null) {

                Log.d(TAG, "productListData: " + current_page);

                if (!isFilter) {
                    storeDataArrayList.clear();
                    showProgress(false);
                } else {
                    loadingLay.setVisibility(View.INVISIBLE);
//                    storeDataArrayList.remove(storeDataArrayList.size() - 1);
//                    homeCatergoryAdap.notifyItemRemoved(storeDataArrayList.size());

                }


                JSONObject json = new JSONObject(jsonObject.toString());
                if (json.getString("status").equals("200")) {
                    nestedScrollView.setVisibility(View.VISIBLE);
                    noDataLay.setVisibility(View.GONE);

                    JSONArray product_review = json.getJSONArray("store_details");
                    if (product_review.length() > 0) {

                        for (int i = 0; i < product_review.length(); i++) {

                            String store_id = product_review.getJSONObject(i).getString("store_id");
                            String store_name = product_review.getJSONObject(i).getString("store_name");
                            String store_img = product_review.getJSONObject(i).getString("store_img");
                            String store_status = product_review.getJSONObject(i).getString("store_status");
                            String product_count = product_review.getJSONObject(i).getString("product_count");
                            String deal_count = product_review.getJSONObject(i).getString("deal_count");

                            StoreData dealsData = new StoreData(store_id, store_name, store_img, store_status, product_count, deal_count);
                            storeDataArrayList.add(dealsData);
                        }


                        System.out.println("most_popular_product " + storeDataArrayList.size());

                        storeList.setVisibility(View.VISIBLE);

                        if (!isFilter) {
                            homeCatergoryAdap = new StoreListAdapter(getActivity(), storeDataArrayList,  storeList);
                            storeList.setAdapter(homeCatergoryAdap);
                        } else {

                            homeCatergoryAdap.notifyDataSetChanged();
                            homeCatergoryAdap.setLoaded();

                        }

                        /*
                               Saveup api doesn't support pagination yet.
                         */

//                        homeCatergoryAdap.setOnLoadMoreListener(new OnLoadMoreListener() {
//                            @Override
//                            public void onLoadMore() {
//                                Log.e("haint", "Load More");
//
//                                isFilter = true;
//                                current_page++;
//
//
////                                storeDataArrayList.add(null);
////                                homeCatergoryAdap.notifyItemInserted(storeDataArrayList.size() - 1);
//
//                                loadingLay.setVisibility(View.VISIBLE);
//                                TranslateAnimation animation = new TranslateAnimation(0f, 0f, loadingLay.getHeight(), 0f);
//                                animation.setDuration(300);
//                                loadingLay.startAnimation(animation);
//                                //Remove loading item
//                                userPresenter.storeListPage("" + current_page);
//
//                            }
//                        });


                        homeCatergoryAdap.setOnItemClickListener(new StoreListAdapter.MyClickListener() {
                            @Override
                            public void onItemClick(int position, View v) {

                                try {
                                    showProgress(true);
                                    UserPresenter userPresenter = new UserPresenter(StoreListFragment.this);
                                    userPresenter.shopdetailsPage(storeDataArrayList.get(position).getStore_id());

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    } else {

                        if (!isFilter) {
                            storeList.setVisibility(View.GONE);
                            nestedScrollView.setVisibility(View.GONE);
                            noDataLay.setVisibility(View.VISIBLE);


                        } else {

//                            Snackbar.make(nestedScrollView, "" + json.getString("message"), Snackbar.LENGTH_SHORT).show();
                        }


                    }
                } else {

                    if (!isFilter) {
                        nestedScrollView.setVisibility(View.GONE);
                        noDataLay.setVisibility(View.VISIBLE);
                    } else {

//                        Snackbar.make(nestedScrollView, "" + json.getString("message"), Snackbar.LENGTH_SHORT).show();
                    }


                }
            } else {

                nestedScrollView.setVisibility(View.GONE);
                mProgressView.setVisibility(View.GONE);
                ((MainActivity) getActivity()).showSnackBar(nestedScrollView, getString(R.string.unable_to_reach_server));
            }
        } catch (
                Exception ex
        ) {
            ex.printStackTrace();

        }

    }


    private void filterSortView(final ArrayList<FilterSortData> arrayList) {

        final Dialog dialog = new Dialog(getActivity(), R.style.dialogwinddow);
        dialog.setContentView(R.layout.sort_filter_layout);
        // set the custom dialog components - text, image and button
        RecyclerView listView = dialog.findViewById(R.id.sort_filter_lay);
        RecyclerView filterlist = dialog.findViewById(R.id.filterlist);
        LinearLayout filterLay = dialog.findViewById(R.id.filterLay);

        TextView titleTxt = dialog.findViewById(R.id.titleTxt);

        titleTxt.setText("" + getResources().getString(R.string.filter));
        filterLay.setVisibility(View.GONE);


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
//                    for (int i = 0; i < sortcolorArray.size(); i++) {
//
//                        if (i == position) {
//                            sortcolorArray.set(position, "#ff2353");
//                        } else {
//                            sortcolorArray.set(i, "#000000");
//                        }
//                    }

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

                    //saveup custom sort

                    if (position == arrayList.size() - 1) {
                        userPresenter.storeListPage("" + current_page);
                    } else {
                        userPresenter.filterMerchant(arrayList.get(position).getFilter_id());
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


    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public void reviewListUpdate(JSONArray store_review) {

        List<Fragment> allFragments = getActivity().getSupportFragmentManager().getFragments();
        for (Fragment fragmento : allFragments) {
            if (fragmento instanceof StoreListDetailsFragment) {
                ((StoreListDetailsFragment) fragmento).reviewListUpdate(store_review);
            }
        }

    }

    public void storeDetailsView(JsonObject jsonObject) {
        try {

            final MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.search_layout.setVisibility(View.GONE);
            mainActivity.menu_Name.setVisible(false);

            mainActivity.toggle.setDrawerIndicatorEnabled(false);
            mainActivity.toolbar.setVisibility(View.VISIBLE);
            mainActivity.disableDrawer(false);
            mainActivity.toolbar.setTitle(getString(R.string.merchant_details));
            ((MainActivity) getActivity()).toolbar.setLogo(R.drawable.transpent_logo);
            final Drawable upArrow = getResources().getDrawable(R.drawable.back_icon);
            mainActivity.toggle.setHomeAsUpIndicator(upArrow);

            mainActivity.toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mainActivity.onBackPressed();
                }
            });

            showProgress(false);
            
            tabviewLayout.setVisibility(View.VISIBLE);
            tabMainLayout.setVisibility(View.VISIBLE);
            nestedScrollView.setVisibility(View.GONE);
            noDataLay.setVisibility(View.GONE);
            tabsLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            if (jsonObject != null) {
                JSONObject json = new JSONObject(jsonObject.toString());
                if (json.getString("status").equals("200")) {

                    SessionSave.saveSession("storedetails", "" + jsonObject.toString(), getActivity());
                    Log.d(TAG, "productListData: " + current_page);

                    mCustomPagerAdapter = new CustomPagerAdapter(getChildFragmentManager());
                    mCustomPagerAdapter.addFragment(new StoreListDetailsFragment(), getResources().getString(R.string.store).toUpperCase());
                    // mCustomPagerAdapter.addFragment(new StoreDealsListFragment(), getResources().getString(R.string.deals).toUpperCase());
                    mCustomPagerAdapter.addFragment(new StoreProductListFragment(), getResources().getString(R.string.deals).toUpperCase());
                    storedetailsViewer.setClipToPadding(false);
                    storedetailsViewer.setOffscreenPageLimit(0);
                    storedetailsViewer.setAdapter(mCustomPagerAdapter);
                    tabsLayout.setupWithViewPager(storedetailsViewer);
                    mCustomPagerAdapter.notifyDataSetChanged();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addWishListData(JsonObject mJsonObject, int pos) {
        StoreProductListFragment fragment = new StoreProductListFragment();
        fragment.addWishListData(mJsonObject, pos);
    }


    public class CustomPagerAdapter extends FragmentStatePagerAdapter {
        int MaxCount = 0;
        String cat_id = "";
        ArrayList<SubCategoryData> subArrayList;

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public CustomPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
