package com.saveup.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saveup.activity.MainActivity;
import com.saveup.adapter.DealsofDayListAdapter;
import com.saveup.adapter.FiftyOfferListAdapter;
import com.saveup.adapter.HomeCategoryListAdapter;
import com.saveup.adapter.MostPopularListAdapter;
import com.saveup.adapter.TopOffersListAdapter;
import com.saveup.autoslide.Animations.DescriptionAnimation;
import com.saveup.autoslide.SliderLayout;
import com.saveup.autoslide.SliderTypes.BaseSliderView;
import com.saveup.autoslide.SliderTypes.TextSliderView;
import com.saveup.android.R;
import com.saveup.model.BannerData;
import com.saveup.model.DealsofDayData;
import com.saveup.model.FiftyOfferData;
import com.saveup.model.HomeCategoryData;
import com.saveup.model.MostPopularData;
import com.saveup.model.SubCategoryData;
import com.saveup.model.TopOffersData;
import com.saveup.presenter.UserPresenter;
import com.saveup.utils.ApplyFont;
import com.saveup.utils.SessionSave;
import com.saveup.utils.SimpleDividerItemDecoration;
import com.saveup.views.RippleView;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static com.facebook.login.widget.ProfilePictureView.TAG;

/**
 * @author AAPBD on 16,April,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.adapter
 */

public class HomeScreenFragment extends Fragment implements TabLayout.OnTabSelectedListener {

    private String TAG = HomeScreenFragment.class.getSimpleName();

    private RecyclerView mCategoryList, mDealsofDatList, mTopOffersList, mFiftyOfferList, mMostPopularList;
    private ArrayList<HomeCategoryData> homeCategoryDataArrayList = new ArrayList<>();
    private ArrayList<SubCategoryData> subCategoryDataArrayList;
    private ArrayList<BannerData> bannerDataArrayList = new ArrayList<>();
    private ArrayList<DealsofDayData> dealsofDayDataArrayList = new ArrayList<>();
    private ArrayList<FiftyOfferData> fiftyOfferDataArrayList = new ArrayList<>();
    private ArrayList<TopOffersData> topOffersDataArrayList = new ArrayList<>();
    private ArrayList<MostPopularData> mostPopularDataArrayList = new ArrayList<>();
    private View mProgressView;
    private View nestedScrollView;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    private long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;
    private long updatedTime = 0L;
    private TextView hrscount, mincount, dayscount, seccount;
    private RippleView grapnow_button, viewall_button, viewall_button_1, viewall_button_2;
    private SliderLayout mBannerPagerView;
    public LinearLayout daysLay, tabviewLayout, mostpopularproductLay;
    private CustomPagerAdapter mCustomPagerAdapter;
    private ViewPager categoryviewPager;
    private TabLayout tabLayout;
    private RelativeLayout popularproductLay;
    private LinearLayout dealscategoryLay, dealofdayLay;
    private Handler mHandler = new Handler();
    private Date enddate;
    private MaterialButton getDiscounts;
    private FloatingActionButton fab;

    private View bannerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homescreen, container, false);
        setHasOptionsMenu(true);
        hrscount = view.findViewById(R.id.hrscount);
        mincount = view.findViewById(R.id.mincount);
        seccount = view.findViewById(R.id.seccount);
        dayscount = view.findViewById(R.id.dayscount);
        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        mProgressView = view.findViewById(R.id.progressBar);
        categoryviewPager = view.findViewById(R.id.categoryviewpager);
        tabLayout = view.findViewById(R.id.tabsItem);
        daysLay = view.findViewById(R.id.daysLay);
        tabviewLayout = view.findViewById(R.id.tabviewLayout);
        dealscategoryLay = view.findViewById(R.id.dealscategoryLay);
        mostpopularproductLay = view.findViewById(R.id.mostpopularproductLay);
        dealofdayLay = view.findViewById(R.id.dealofdayLay);
        popularproductLay = view.findViewById(R.id.farmelayout);
        mBannerPagerView = view.findViewById(R.id.viewpager);

        grapnow_button = view.findViewById(R.id.grapnow_button);
        viewall_button = view.findViewById(R.id.viewall_button);
        viewall_button_1 = view.findViewById(R.id.viewall_button_1);
        viewall_button_2 = view.findViewById(R.id.viewall_button_2);
        mCategoryList = view.findViewById(R.id.categoryList);
        mCategoryList.setHasFixedSize(true);
        mCategoryList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mCategoryList.setNestedScrollingEnabled(false);


        mDealsofDatList = view.findViewById(R.id.dealofdayList);
        mDealsofDatList.setHasFixedSize(true);
        mDealsofDatList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mDealsofDatList.setNestedScrollingEnabled(false);


        mFiftyOfferList = view.findViewById(R.id.uptofiftyList);
        mFiftyOfferList.setHasFixedSize(true);
        mFiftyOfferList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mFiftyOfferList.setNestedScrollingEnabled(false);


        mTopOffersList = view.findViewById(R.id.topoffersList);
        mTopOffersList.setHasFixedSize(true);
        mTopOffersList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mTopOffersList.setNestedScrollingEnabled(false);
       // mTopOffersList.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        mMostPopularList = view.findViewById(R.id.popularproductList);
        mMostPopularList.setHasFixedSize(true);
        mMostPopularList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mMostPopularList.setNestedScrollingEnabled(false);
       // mMostPopularList.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        applyFont(view);

       /* getDiscounts = view.findViewById(R.id.button_get_discounts);
        getDiscounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).initiateSearch();
            }
        });*/

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).startMerchantFragment(true, false);
            }
        });

        bannerView = view.findViewById(R.id.homescreen_view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((MainActivity) getActivity()).setTag = "homepage";
        showProgress(true);
        UserPresenter userPresenter = new UserPresenter(HomeScreenFragment.this);
        userPresenter.homePageApi();
        getActivity().setTitle("");

        grapnow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", "");
                    DealsListFragment fragment2 = new DealsListFragment();
                    fragment2.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.fragment_container, fragment2);
                    fragmentTransaction.addToBackStack("");
                    fragmentTransaction.commit();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        //latest product view all button

        viewall_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).isTouchFromHome = false;
                ((MainActivity) getActivity()).latestDealSearch = true;
                Bundle bundle = new Bundle();
                bundle.putString("main_category_id", "");
                bundle.putString("sec_category_id", "");
                bundle.putString("sub_category_id", "");
                bundle.putString("sub_sec_category_id", "");
                bundle.putString("title", "");
                bundle.putString("page_title", "" + getResources().getString(R.string.latest_deals));
                bundle.putString("discount", "");
                bundle.putString("price_min", "");
                bundle.putString("price_max", "");
                bundle.putString("availability", "1");
                bundle.putString("sort_order_by", "4");
                bundle.putString("view_all_btn_data", getResources().getString(R.string.latest_deals));
                ProductListFragment fragment2 = new ProductListFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, fragment2, ((MainActivity) getActivity()).setTag);
                fragment2.setArguments(bundle);
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();


            }
        });

        //hot deals view all button


        viewall_button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).isTouchFromHome = false;
                ((MainActivity) getActivity()).hotDealSearch = true;

                Bundle bundle = new Bundle();
                bundle.putString("main_category_id", "");
                bundle.putString("sec_category_id", "");
                bundle.putString("sub_category_id", "");
                bundle.putString("sub_sec_category_id", "");
                bundle.putString("title", "");
                bundle.putString("page_title", getResources().getString(R.string.hot_deals));
                bundle.putString("discount", "7");
                bundle.putString("price_min", "");
                bundle.putString("price_max", "");
                bundle.putString("availability", "1");
                bundle.putString("sort_order_by", "4");
                bundle.putString("view_all_btn_data", getResources().getString(R.string.hot_deals));
                ProductListFragment fragment2 = new ProductListFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, fragment2, ((MainActivity) getActivity()).setTag);
                fragment2.setArguments(bundle);
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();
            }
        });

        //gift product view all button

        viewall_button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).isTouchFromHome = false;
                ((MainActivity) getActivity()).giftDealSearch = true;

                Bundle bundle = new Bundle();
                bundle.putString("main_category_id", "");
                bundle.putString("sec_category_id", "");
                bundle.putString("sub_category_id", "");
                bundle.putString("sub_sec_category_id", "");
                bundle.putString("title", "");
                bundle.putString("page_title", getResources().getString(R.string.gift_deals));
                bundle.putString("discount", "");
                bundle.putString("price_min", "1");
                bundle.putString("price_max", "");
                bundle.putString("availability", "1");
                bundle.putString("sort_order_by", "1");
                bundle.putString("view_all_btn_data", getResources().getString(R.string.gift_deals));
                ProductListFragment fragment2 = new ProductListFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, fragment2, ((MainActivity) getActivity()).setTag);
                fragment2.setArguments(bundle);
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();
            }
        });

        dealscategoryLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", "");
                    DealsListFragment fragment2 = new DealsListFragment();
                    fragment2.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.fragment_container, fragment2);
                    fragmentTransaction.addToBackStack("");
                    fragmentTransaction.commit();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }


    public void homePageData(JsonObject jsonObject) {

        try {

            if (jsonObject != null) {
               // Log.d(TAG, "homePageData: " + jsonObject.toString());

                SessionSave.saveSession("drawer", jsonObject.toString(), getActivity());
                JSONObject json = new JSONObject(jsonObject.toString());


                if (json.getString("status").equals("200")) {
//                customHandler.postDelayed(updateTimerThread, 0);

                    if (!json.getString("Total_deal_end_time").equals("")) {

                        String deal_end_date = json.getString("Total_deal_end_time");
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        enddate = format.parse(deal_end_date);
                        startUpdateTimer();
                    }


                    showProgress(false);

                    JSONArray category_details = json.getJSONArray("category_details");

                    if (category_details.length() > 0) {

                        SessionSave.saveSession("category_list", "" + category_details, getActivity());

                        for (int i = 0; i < category_details.length(); i++) {


                            String category_id = category_details.getJSONObject(i).getString("category_id");
                            String category_name = category_details.getJSONObject(i).getString("category_name");
                            String category_image = category_details.getJSONObject(i).getString("category_image");

                            Log.d(TAG, "category_name: " + category_name);


                            JSONArray sub_category_list = category_details.getJSONObject(i).getJSONArray("sub_category_list");
                            subCategoryDataArrayList = new ArrayList<>();
                            if (sub_category_list.length() > 0) {
                                for (int j = 0; j < sub_category_list.length(); j++) {

                                    String category_id1 = sub_category_list.getJSONObject(j).getString("category_id");
                                    String category_name2 = sub_category_list.getJSONObject(j).getString("category_name");
                                    String category_image3 = sub_category_list.getJSONObject(j).getString("category_image");

                                    Log.d(TAG, "sub_category_name: " + category_name2);

                                    SubCategoryData subData = new SubCategoryData(category_id1, category_name2, category_image3);
                                    subCategoryDataArrayList.add(subData);
                                }
                            } else {

                                subCategoryDataArrayList.clear();
                            }
                            Log.d(TAG, "subCategoryDataArrayList ----> " + subCategoryDataArrayList.size() + " positon " + i);
                            HomeCategoryData homeData = new HomeCategoryData(category_id, category_name, category_image, subCategoryDataArrayList);
                            homeCategoryDataArrayList.add(homeData);


                        }

                        HomeCategoryListAdapter homeCatergoryAdap = new HomeCategoryListAdapter(getActivity(), homeCategoryDataArrayList);
                        mCategoryList.setAdapter(homeCatergoryAdap);
                        mCategoryList.setVisibility(View.VISIBLE);
                        homeCatergoryAdap.setOnItemClickListener(new HomeCategoryListAdapter.MyClickListener() {
                            @Override
                            public void onItemClick(int position, View v) {
                                Log.d(TAG, "onItemClick: "+position);
                                try {
                                    Log.d(TAG, "onItemClick: " + homeCategoryDataArrayList.get(position).getmSubCategoryData().size());

                                    ArrayList<SubCategoryData> subCategoryData = new ArrayList<>(homeCategoryDataArrayList.get(position).getmSubCategoryData());
                                    subCategoryData.add(0, new SubCategoryData(homeCategoryDataArrayList.get(position).getCategory_id(),
                                            homeCategoryDataArrayList.get(position).getCategory_Name(), homeCategoryDataArrayList.get(position).getCategory_image()));

                                    //    homeCategoryDataArrayList.get(position).setmSubCategoryData(subCategoryData);
                                    if (subCategoryData.size() > 0) {
                                        nestedScrollView.setVisibility(View.GONE);
                                        tabviewLayout.setVisibility(View.VISIBLE);
                                        ((MainActivity) getActivity()).isCategoryTouched = false;
                                        ((MainActivity) getActivity()).searchIcon.setVisible(true);
                                        getActivity().setTitle("");
                                        viewCategory(subCategoryData, "1");


                                    } else {

                                        Snackbar.make(nestedScrollView, "" + "No sub category found!!!", Snackbar.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    } else {
                        mCategoryList.setVisibility(View.GONE);
                    }

                    JSONArray banner_details = json.getJSONArray("banner_details");

                    Log.d(TAG, " banner_details  size "+banner_details.length());

                    if (banner_details.length() > 0) {

                        for (int i = 0; i < banner_details.length(); i++) {

                            String banner_id = banner_details.getJSONObject(i).getString("banner_id");
                            String banner_title = banner_details.getJSONObject(i).getString("banner_title");
                            String banner_redirect_url = banner_details.getJSONObject(i).getString("banner_redirect_url");
                            String banner_image = banner_details.getJSONObject(i).getString("banner_image");
                            BannerData bannerDate = new BannerData(banner_id, banner_title, banner_redirect_url, banner_image);
                            bannerDataArrayList.add(bannerDate);
                            Log.d(TAG, " banner title "+banner_title);
                        }

                        for (int i = 0; i < bannerDataArrayList.size(); i++) {
                            TextSliderView textSliderView = new TextSliderView(getActivity());
                            // initialize a SliderLayout
                            textSliderView
                                    .image(bannerDataArrayList.get(i).getBanner_image())
                                    .setScaleType(BaseSliderView.ScaleType.Fit);
                            mBannerPagerView.addSlider(textSliderView);
                        }
                        mBannerPagerView.setCustomAnimation(new DescriptionAnimation());
                        mBannerPagerView.setDuration(4000);
                        mBannerPagerView.setPresetTransformer("Fade");
                        mBannerPagerView.setVisibility(View.VISIBLE);


                        bannerView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    int position = mBannerPagerView.getCurrentPosition();

                                    Log.d(TAG, " clicked banner title "+bannerDataArrayList.get(position).getBanner_title());

                                    ((MainActivity) getActivity()).isTouchFromHome = true;
                                    Bundle bundle = new Bundle();
                                    bundle.putString("cat_id", bannerDataArrayList.get(position).getBanner_id());
                                    bundle.putString("title", bannerDataArrayList.get(position).getBanner_title());
                                    ProductDetailsFragment fragment2 = new ProductDetailsFragment();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.add(R.id.fragment_container, fragment2, "home");
                                    fragment2.setArguments(bundle);
                                    fragmentTransaction.addToBackStack("");
                                    fragmentTransaction.commit();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    } else {
                        mBannerPagerView.setVisibility(View.GONE);
                    }

                    JSONArray deals_of_day_details = json.getJSONArray("deals_of_day_details");
                    Log.d(TAG, " deals_of_day_details  size "+deals_of_day_details.length());

                    if (deals_of_day_details.length() > 0) {
                        dealofdayLay.setVisibility(View.VISIBLE);

                        for (int i = 0; i < deals_of_day_details.length(); i++) {
                            String product_id = deals_of_day_details.getJSONObject(i).getString("deal_id");
                            String product_price = deals_of_day_details.getJSONObject(i).getString("deal_price");
                            String product_discount_price = deals_of_day_details.getJSONObject(i).getString("deal_discount_price");
                            String product_percentage = deals_of_day_details.getJSONObject(i).getString("deal_percentage");
                            String product_type = deals_of_day_details.getJSONObject(i).getString("product_type");
                            String product_image = deals_of_day_details.getJSONObject(i).getString("deal_image");

                            Log.d(TAG, " deals_of_day_ product price "+product_price);

                            SessionSave.saveSession("currency_symbol", deals_of_day_details.getJSONObject(i).getString("currency_symbol"), getActivity());

                            DealsofDayData dealsData = new DealsofDayData(product_id, "", product_price, product_discount_price, product_percentage,product_type, product_image);
                            dealsofDayDataArrayList.add(dealsData);
                        }

                        DealsofDayListAdapter homeCatergoryAdap = new DealsofDayListAdapter(getActivity(), dealsofDayDataArrayList);
                        mDealsofDatList.setAdapter(homeCatergoryAdap);
                        mDealsofDatList.setVisibility(View.VISIBLE);


                        homeCatergoryAdap.setOnItemClickListener(new DealsofDayListAdapter.MyClickListener() {
                            @Override
                            public void onItemClick(int position, View v) {

                                try {
                                    ((MainActivity) getActivity()).titleTxt = dealsofDayDataArrayList.get(position).getProduct_title();
                                    ((MainActivity) getActivity()).isTouchFromHome = true;
                                    try {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("deal_id", dealsofDayDataArrayList.get(position).getProduct_id());
                                        bundle.putString("title", dealsofDayDataArrayList.get(position).getProduct_title());
                                        DealsListDetailsFragment fragment2 = new DealsListDetailsFragment();
                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.add(R.id.fragment_container, fragment2, "home");
                                        fragment2.setArguments(bundle);
                                        fragmentTransaction.addToBackStack("");
                                        fragmentTransaction.commit();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });


                    } else {
                        mDealsofDatList.setVisibility(View.GONE);
                        dealofdayLay.setVisibility(View.GONE);
                    }

                    //latest product

                    JSONArray product_top_offer = json.getJSONArray("product_top_offer");
                    Log.d(TAG, " product_top_offer  size "+product_top_offer.length());

                    if (product_top_offer.length() > 0) {

                        for (int i = 0; i < product_top_offer.length() && i <12; i++) {

                            String product_id = product_top_offer.getJSONObject(i).getString("product_id");
                            String product_title = product_top_offer.getJSONObject(i).getString("product_title");
                            String product_price = product_top_offer.getJSONObject(i).getString("product_price");
                            String product_discount_price = product_top_offer.getJSONObject(i).getString("product_discount_price");
                            String product_type = product_top_offer.getJSONObject(i).getString("product_type");
                            String product_percentage = "";
                            if(product_type.equals("all_item")){
                                product_percentage = product_top_offer.getJSONObject(i).getString("product_discount");
                            }else{
                                product_percentage = product_top_offer.getJSONObject(i).getString("product_discount_price");
                            }
                            String product_image = product_top_offer.getJSONObject(i).getString("product_image");
                            String merchant_name = product_top_offer.getJSONObject(i).optString("merchant_name");


                            SessionSave.saveSession("currency_code", product_top_offer.getJSONObject(i).getString("currency_code"), getActivity());
                            SessionSave.saveSession("currency_symbol", product_top_offer.getJSONObject(i).getString("currency_symbol"), getActivity());
                            TopOffersData dealsData = new TopOffersData(product_id, product_title, product_price, product_discount_price, product_percentage, product_type, product_image, merchant_name);

                            Log.d(TAG, " product_top_offer  "+dealsData.toString());

                            topOffersDataArrayList.add(dealsData);
                        }

                        TopOffersListAdapter topOffersListAdapter = new TopOffersListAdapter(getActivity(), topOffersDataArrayList);

                        mTopOffersList.setAdapter(topOffersListAdapter);
                        mTopOffersList.setVisibility(View.VISIBLE);

                        topOffersListAdapter.setOnItemClickListener(new TopOffersListAdapter.MyClickListener() {
                            @Override
                            public void onItemClick(int position, View v) {

                                try {
                                    ((MainActivity) getActivity()).isTouchFromHome = true;
                                    Bundle bundle = new Bundle();
                                    bundle.putString("cat_id", topOffersDataArrayList.get(position).getProduct_id());
                                    bundle.putString("title", topOffersDataArrayList.get(position).getProduct_title());
                                    ProductDetailsFragment fragment2 = new ProductDetailsFragment();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.add(R.id.fragment_container, fragment2, "home");
                                    fragment2.setArguments(bundle);
                                    fragmentTransaction.addToBackStack("");
                                    fragmentTransaction.commit();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });


                    } else {
                        mTopOffersList.setVisibility(View.GONE);
                    }


                    //hot deals

                    JSONArray product_fifty_percent = json.getJSONArray("product_fifty_percent");
                    Log.d(TAG, " product_fifty_percent  size "+product_fifty_percent.length());

                    if (product_fifty_percent.length() > 0) {

                        for (int i = 0; i < product_fifty_percent.length(); i++) {

                            String product_id = product_fifty_percent.getJSONObject(i).getString("product_id");
                            String product_title = product_fifty_percent.getJSONObject(i).getString("product_title");
                            String product_price = product_fifty_percent.getJSONObject(i).getString("product_price");
                            String product_discount_price = product_fifty_percent.getJSONObject(i).getString("product_discount_price");
                            String product_type = product_fifty_percent.getJSONObject(i).getString("product_type");
                            String product_percentage = "";
                            if(product_type.equals("all_item")){
                                product_percentage = product_fifty_percent.getJSONObject(i).getString("product_discount");
                            }else{
                                product_percentage = product_fifty_percent.getJSONObject(i).getString("product_discount_price");
                            }
                            String product_image = product_fifty_percent.getJSONObject(i).getString("product_image");
                            String merchant_name = product_fifty_percent.getJSONObject(i).optString("merchant_name");



                            SessionSave.saveSession("currency_code", product_fifty_percent.getJSONObject(i).getString("currency_code"), getActivity());

                            FiftyOfferData dealsData = new FiftyOfferData(product_id, product_title, product_price, product_discount_price, product_percentage, product_type, product_image, merchant_name);

                            Log.d(TAG, " product_fifty_percent   "+dealsData.toString());

                            fiftyOfferDataArrayList.add(dealsData);
                        }

                        FiftyOfferListAdapter homeCatergoryAdap = new FiftyOfferListAdapter(getActivity(), fiftyOfferDataArrayList);
                        mFiftyOfferList.setAdapter(homeCatergoryAdap);
                        mFiftyOfferList.setVisibility(View.VISIBLE);

                        homeCatergoryAdap.setOnItemClickListener(new FiftyOfferListAdapter.MyClickListener() {
                            @Override
                            public void onItemClick(int position, View v) {

                                try {
                                    ((MainActivity) getActivity()).isTouchFromHome = true;
                                    Bundle bundle = new Bundle();
                                    bundle.putString("cat_id", fiftyOfferDataArrayList.get(position).getProduct_id());
                                    bundle.putString("title", fiftyOfferDataArrayList.get(position).getProduct_title());
                                    ProductDetailsFragment fragment2 = new ProductDetailsFragment();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.add(R.id.fragment_container, fragment2, "home");
                                    fragment2.setArguments(bundle);
                                    fragmentTransaction.addToBackStack("");
                                    fragmentTransaction.commit();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });


                    } else {
                        mFiftyOfferList.setVisibility(View.GONE);
                    }

                    //gift product

                    JSONArray most_popular_product = json.getJSONArray("most_popular_product");
                    Log.d(TAG, " most_popular_product  size "+most_popular_product.length());

                    if (most_popular_product.length() > 0) {

                        for (int i = 0; i < most_popular_product.length(); i++) {

                            String product_id = most_popular_product.getJSONObject(i).getString("product_id");
                            String product_title = most_popular_product.getJSONObject(i).getString("product_title");
                            String product_price = most_popular_product.getJSONObject(i).getString("product_price");
                            String product_discount_price = most_popular_product.getJSONObject(i).getString("product_discount_price");
                            String product_image = most_popular_product.getJSONObject(i).getString("product_image");
                            String product_type = most_popular_product.getJSONObject(i).getString("product_type");
                            String product_percentage = "";
                            if(product_type.equals("all_item")){
                                product_percentage = most_popular_product.getJSONObject(i).getString("product_discount");
                            }else{
                                product_percentage = most_popular_product.getJSONObject(i).getString("product_discount_price");
                            }
                            String merchant_name = most_popular_product.getJSONObject(i).optString("merchant_name");


                            SessionSave.saveSession("currency_code", most_popular_product.getJSONObject(i).getString("currency_code"), getActivity());

                            MostPopularData dealsData = new MostPopularData(product_id, product_title,
                                    product_price, product_discount_price, product_percentage, product_type, product_image, merchant_name);

                            Log.d(TAG, " most_popular_product   "+dealsData.toString());

                            mostPopularDataArrayList.add(dealsData);
                        }

                        Log.d("PopularSize", String.valueOf(mostPopularDataArrayList.size()));

                        MostPopularListAdapter homeCatergoryAdap = new MostPopularListAdapter(getActivity(), mostPopularDataArrayList);
                        mMostPopularList.setAdapter(homeCatergoryAdap);
                        mMostPopularList.setVisibility(View.VISIBLE);
//                        popularproductLay
//                                .post(new Runnable() {
//                                    public void run() {
//                                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (mostpopularproductLay.getHeight()) - 80);
//                                        popularproductLay.setLayoutParams(lp);
//                                    }
//                                });
                        homeCatergoryAdap.setOnItemClickListener(new MostPopularListAdapter.MyClickListener() {
                            @Override
                            public void onItemClick(int position, View v) {

                                try {
                                    ((MainActivity) getActivity()).isTouchFromHome = true;
                                    Bundle bundle = new Bundle();
                                    bundle.putString("cat_id", mostPopularDataArrayList.get(position).getProduct_id());
                                    bundle.putString("title", mostPopularDataArrayList.get(position).getProduct_title());
                                    ProductDetailsFragment fragment2 = new ProductDetailsFragment();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.add(R.id.fragment_container, fragment2, "home");
                                    fragment2.setArguments(bundle);
                                    fragmentTransaction.addToBackStack("");
                                    fragmentTransaction.commit();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                    } else {
                        mMostPopularList.setVisibility(View.GONE);
                    }


                }
            } else {

                nestedScrollView.setVisibility(View.GONE);
                mProgressView.setVisibility(View.GONE);
                ((MainActivity) getActivity()).showSnackBar(nestedScrollView, getString(R.string.unable_to_reach_server));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

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


    @Override
    public void onTabSelected(TabLayout.Tab tab) {


    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    private void viewCategory(ArrayList<SubCategoryData> arrayList, String cat_id) {

        ((MainActivity) getActivity()).search_layout.setVisibility(View.GONE);
        ((MainActivity) getActivity()).searchIcon.setVisible(true);
        ((MainActivity) getActivity()).menu_Name.setVisible(false);

        System.out.println("msubCategoryList " + arrayList.size());

        mCustomPagerAdapter = new CustomPagerAdapter(getFragmentManager(), arrayList.size(), cat_id, arrayList);
        categoryviewPager.setClipToPadding(false);
        categoryviewPager.setOffscreenPageLimit(0);
        categoryviewPager.setAdapter(mCustomPagerAdapter);

        tabLayout.setupWithViewPager(categoryviewPager);

        for (int i = 0; i < arrayList.size(); i++) {

            if (arrayList.size() <= 4) {
                tabLayout.setTabMode(TabLayout.MODE_FIXED);
                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                tabLayout.setMinimumWidth(0);
            } else {
                tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            }
            tabLayout.getTabAt(i).setText(arrayList.get(i).getSub_category_Name());
        }
    }

    public class CustomPagerAdapter extends FragmentStatePagerAdapter {
        int MaxCount = 0;
        String cat_id = "";
        ArrayList<SubCategoryData> subArrayList;

        public CustomPagerAdapter(FragmentManager fm, int lengh, String cat_id_, ArrayList<SubCategoryData> arrayList) {
            super(fm);
            MaxCount = lengh;
            cat_id = cat_id_;
            subArrayList = arrayList;

        }

        @Override
        public Fragment getItem(int position) {
            System.out.println("MaxCount " + MaxCount);

            if (MaxCount > 0) {
                Bundle bundle = new Bundle();
                bundle.putString("cat_id", "" + cat_id);
                bundle.putString("sub_cat_id", "" + subArrayList.get(position).getSub_category_id());
                bundle.putString("title_name", "" + subArrayList.get(position).getSub_category_Name());
                return MainSubCategoryFragment.newInstance(bundle);
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("cat_id", "" + cat_id);
                bundle.putString("sub_cat_id", "" + subArrayList.get(position).getSub_category_id());
                bundle.putString("title_name", "" + subArrayList.get(position).getSub_category_Name());
                return MainSubCategoryFragment.newInstance(bundle);
            }
        }

        @Override
        public int getCount() {
            return MaxCount;
        }
    }

    public void mainPageView() {
        ((MainActivity) getActivity()).search_layout.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).isCategoryTouched = true;
        tabviewLayout.setVisibility(View.GONE);
        nestedScrollView.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).searchIcon.setVisible(false);
        ((MainActivity) getActivity()).menu_Name.setVisible(true);
    }

    public void applyFont(View view) {

        ApplyFont.applyLightFont(getActivity(), view.findViewById(R.id.txtdealofday));
        ApplyFont.applyLightFont(getActivity(), view.findViewById(R.id.topoffesTxt));
        ApplyFont.applyLightFont(getActivity(), view.findViewById(R.id.popularTxt));
        ApplyFont.applyFont(getActivity(), view.findViewById(R.id.hrsLay));
        ApplyFont.applyFont(getActivity(), view.findViewById(R.id.minLay));
        ApplyFont.applyFont(getActivity(), view.findViewById(R.id.secLay));
        ApplyFont.applyBold(getActivity(), view.findViewById(R.id.grapnow_button));
        ApplyFont.applyBold(getActivity(), view.findViewById(R.id.viewall_button));
        ApplyFont.applyBold(getActivity(), view.findViewById(R.id.viewall_button_1));
        ApplyFont.applyBold(getActivity(), view.findViewById(R.id.viewall_button_2));
        ApplyFont.applyLightFont(getActivity(), view.findViewById(R.id.fiftyoffTxt));
        ApplyFont.applyBold(getActivity(), view.findViewById(R.id.button_addCart));
        ApplyFont.applyBold(getActivity(), view.findViewById(R.id.button_buyNow));


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);

        menu.getItem(0).setVisible(false);

        if(tabviewLayout.getVisibility()==View.VISIBLE){
            ((MainActivity) getActivity()).searchIcon.setVisible(true);
            ((MainActivity) getActivity()).menu_Name.setVisible(false);
        }
    }


    private Runnable updateRemainingTimeRunnable = new Runnable() {
        @Override
        public void run() {

            long currentTime = System.currentTimeMillis();

            updateTimeRemaining(currentTime, enddate.getTime());

        }
    };

    private void startUpdateTimer() {
        Timer tmr = new Timer();
        tmr.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(updateRemainingTimeRunnable);
            }
        }, 1000, 1000);
    }

    public void updateTimeRemaining(long currentTime, long endTime) {

        long timeDiff = endTime - currentTime;

        System.out.println("timeDiff -- > " + timeDiff);
        if (timeDiff > 0) {
            int seconds = (int) (timeDiff / 1000) % 60;
            int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
            int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);

            hrscount.setText(String.format("%02d", hours));
            mincount.setText(String.format("%02d", minutes));
            seccount.setText(String.format("%02d", seconds));

            int daysLeft = (int) (timeDiff / (1000 * 60 * 60 * 24));

            if (daysLeft > 0) {
                dayscount.setText(String.format("%02d", daysLeft) + " ");
            } else {
                daysLay.setVisibility(View.GONE);
            }


        } else {
            daysLay.setVisibility(View.GONE);
            hrscount.setText(String.format("%02d", 0));
            mincount.setText(String.format("%02d", 0));
            seccount.setText(String.format("%02d", 0));
        }
    }
}
