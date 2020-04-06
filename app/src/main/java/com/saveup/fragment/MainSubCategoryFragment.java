package com.saveup.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saveup.android.R;
import com.google.gson.JsonObject;
import com.saveup.activity.MainActivity;
import com.saveup.adapter.MostSubCategroyAdapter;
import com.saveup.adapter.TopBrandAdsAdapter;
import com.saveup.adapter.TopBrandListAdapter;
import com.saveup.adapter.TopOffersListAdapter;
import com.saveup.autoslide.Animations.DescriptionAnimation;
import com.saveup.autoslide.SliderLayout;
import com.saveup.autoslide.SliderTypes.BaseSliderView;
import com.saveup.autoslide.SliderTypes.TextSliderView;
import com.saveup.model.AdsData;
import com.saveup.model.MostPopularData;
import com.saveup.model.SubBannerData;
import com.saveup.model.TopBrandData;
import com.saveup.model.TopOffersData;
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
 * @author AAPBD on 8,April,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.adapter
 */

public class MainSubCategoryFragment extends Fragment {


    private SliderLayout mBannerPagerView;
    private RecyclerView mAdsList, mTopBrandList, mTopOffersList, mMostPopularList;
    private View mProgressView;
    private View nestedScrollView;
    private ArrayList<SubBannerData> bannerDataArrayList = new ArrayList<>();
    private ArrayList<AdsData> adsDataArrayList = new ArrayList<>();
    private ArrayList<TopBrandData> topBrandDataArrayList = new ArrayList<>();
    private ArrayList<TopOffersData> topOffersDataArrayList = new ArrayList<>();
    private ArrayList<MostPopularData> mostsubPopularDataArrayList = new ArrayList<>();
    private LinearLayout topoffersLay, mostpopularproductLay;
    private RelativeLayout popularproductLay;
    private FrameLayout farmelayout;
    private RippleView viewall_subbutton, viewall_subbutton_1;
    private String title_name, sec_category_id, cat_id;
    private boolean isVisibleToUser;

    private TextView topOfferTv, noProductTv;

    // newInstance constructor for creating fragment with arguments
    public static MainSubCategoryFragment newInstance(Bundle bundle) {
        System.out.println("Home Screeen 3");
        MainSubCategoryFragment allFeedFragment = new MainSubCategoryFragment();
        allFeedFragment.setArguments(bundle);
        System.out.println("isVisibleToUser name  ---> " + "1");
        return allFeedFragment;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        Bundle bundle = getArguments();
        if (getActivity() != null && bundle != null && isVisibleToUser) {
            isVisibleToUser = false;
            title_name = "" + bundle.get("title_name");
            sec_category_id = "" + bundle.get("sub_cat_id");
            System.out.println("sec_category_id--- > " + sec_category_id);
            showProgress(true);

            cat_id = bundle.getString("cat_id");

            topOfferTv.setText(title_name);

            UserPresenter userPresenter = new UserPresenter(MainSubCategoryFragment.this);

//            if (!StringUtil.isEmpty(sec_category_id)) {
//                topOfferTv.setText(title_name);
//                userPresenter.getProductListByCategoryId(sec_category_id);
//            }
            userPresenter.productListbyCategory("" + bundle.get("sub_cat_id"),"" + bundle.get("cat_id"), SessionSave.getSession("user_id", getActivity()));
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.mainsubcategory, container, false);
        mBannerPagerView = view.findViewById(R.id.viewpager);
        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        mProgressView = view.findViewById(R.id.progressBar);
        topoffersLay = view.findViewById(R.id.topoffersLay);
        popularproductLay = view.findViewById(R.id.popularproductLay);
        mostpopularproductLay = view.findViewById(R.id.mostpopularproductLay);
        farmelayout = view.findViewById(R.id.farmelayout);
        mAdsList = view.findViewById(R.id.adsList);
        viewall_subbutton = view.findViewById(R.id.viewall_subbutton);
        viewall_subbutton_1 = view.findViewById(R.id.viewall_subbutton_1);
        mAdsList.setHasFixedSize(true);
        mAdsList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mAdsList.setNestedScrollingEnabled(false);
        mTopBrandList = view.findViewById(R.id.dealofdayList);
        mTopBrandList.setHasFixedSize(true);
        mTopBrandList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mTopBrandList.setNestedScrollingEnabled(false);
        mTopOffersList = view.findViewById(R.id.topoffersList);
        mTopOffersList.setHasFixedSize(true);
        mTopOffersList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mTopOffersList.setNestedScrollingEnabled(false);
        mTopOffersList.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        mMostPopularList = view.findViewById(R.id.popularproductList);
        mMostPopularList.setHasFixedSize(true);
        mMostPopularList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mMostPopularList.setNestedScrollingEnabled(false);
        mMostPopularList.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        System.out.println("Home Screeen View View View");

        topOfferTv = view.findViewById(R.id.topoffesTxt);
        noProductTv = view.findViewById(R.id.sub_category_no_product_tv);

        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        applyFont(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).setTag = "subcategory";
        Bundle bundle = getArguments();
        if (bundle != null && isVisibleToUser) {
            isVisibleToUser = false;
            title_name = "" + bundle.get("title_name");
            sec_category_id = "" + bundle.get("sub_cat_id");
            if (sec_category_id == null) {
                sec_category_id = "";
            }
            showProgress(true);

            cat_id = bundle.getString("cat_id");

            topOfferTv.setText(title_name);

            UserPresenter userPresenter = new UserPresenter(MainSubCategoryFragment.this);

//            if (!StringUtil.isEmpty(sec_category_id)) {
//                topOfferTv.setText(title_name);
//                userPresenter.getProductListByCategoryId(sec_category_id);
//            }

            userPresenter.productListbyCategory( "" + bundle.get("sub_cat_id"),"" + bundle.get("cat_id"), SessionSave.getSession("user_id", getActivity()));

        }

        viewall_subbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("main_category_id", "");
                bundle.putString("sec_category_id", "" + sec_category_id);
                bundle.putString("sub_category_id", "");
                bundle.putString("sub_sec_category_id", "");
                bundle.putString("title", "");
                bundle.putString("page_title", "" + getResources().getString(R.string.topoffers));
                bundle.putString("discount", "");
                bundle.putString("price_min", "");
                bundle.putString("price_max", "");
                bundle.putString("availability", "1");
                bundle.putString("sort_order_by", "4");
                ProductListFragment fragment2 = new ProductListFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, fragment2, ((MainActivity) getActivity()).setTag);
                fragment2.setArguments(bundle);
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();

            }
        });
        viewall_subbutton_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("main_category_id", "");
                bundle.putString("sec_category_id", "" + sec_category_id);
                bundle.putString("sub_category_id", "");
                bundle.putString("sub_sec_category_id", "");
                bundle.putString("title", "");
                bundle.putString("page_title", "" + getResources().getString(R.string.mostpopular));
                bundle.putString("discount", "");
                bundle.putString("price_min", "");
                bundle.putString("price_max", "");
                bundle.putString("availability", "1");
                bundle.putString("sort_order_by", "1");
                ProductListFragment fragment2 = new ProductListFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, fragment2, ((MainActivity) getActivity()).setTag);
                fragment2.setArguments(bundle);
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();
            }
        });

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

    public void subCategoryData(JsonObject jsonObject) {

        try {
            if (jsonObject != null) {
                Log.d(TAG, "homePageData: " + jsonObject);
                JSONObject json = new JSONObject(jsonObject.toString());

                if (json.getString("status").equals("200")) {
                    showProgress(false);
                    JSONArray banner_details = json.getJSONArray("banner_details");
                    bannerDataArrayList.clear();

                    if (banner_details.length() > 0) {
                        for (int i = 0; i < banner_details.length(); i++) {
                            String banner_image = banner_details.getJSONObject(i).getString("banner_image");
                            SubBannerData bannerDate = new SubBannerData("", banner_image);
                            bannerDataArrayList.add(bannerDate);
                        }

                        for (int i = 0; i < bannerDataArrayList.size(); i++) {
                            TextSliderView textSliderView = new TextSliderView(getActivity());
                            textSliderView.image(bannerDataArrayList.get(i).getBanner_image()).setScaleType(BaseSliderView.ScaleType.Fit);
                            mBannerPagerView.addSlider(textSliderView);
                        }

                        mBannerPagerView.setCustomAnimation(new DescriptionAnimation());
                        mBannerPagerView.setDuration(4000);
                        mBannerPagerView.setPresetTransformer("Fade");
                        mBannerPagerView.setVisibility(View.VISIBLE);

                    } else {
                        mBannerPagerView.setVisibility(View.GONE);
                    }

                    adsDataArrayList.clear();
                    JSONArray ad_details = json.getJSONArray("ad_details");
                    if (ad_details.length() > 0) {


                        for (int i = 0; i < ad_details.length(); i++) {

                            String ad_image = ad_details.getJSONObject(i).getString("ad_image");


                            AdsData dealsData = new AdsData("", ad_image);
                            adsDataArrayList.add(dealsData);
                        }

                        TopBrandAdsAdapter homeCatergoryAdap = new TopBrandAdsAdapter(getActivity(), adsDataArrayList);
                        mAdsList.setAdapter(homeCatergoryAdap);
                        mAdsList.setVisibility(View.VISIBLE);

                    } else {
                        mAdsList.setVisibility(View.GONE);
                    }

                    topBrandDataArrayList.clear();
                    JSONArray categories_list = json.getJSONArray("categories_list");
                    JSONArray sec_maincategory_list = categories_list.getJSONObject(0).getJSONArray("sec_maincategory_list");
                    if (sec_maincategory_list.length() > 0) {


                        for (int i = 0; i < sec_maincategory_list.length(); i++) {

                            if (title_name != null && title_name.equals(sec_maincategory_list.getJSONObject(i).getString("sec_category_name"))) {
                                JSONArray sub_sec_category_list = sec_maincategory_list.getJSONObject(i).getJSONArray("sub_category_list");

                                for (int j = 0; j < sub_sec_category_list.length(); j++) {
                                    String sub_category_id = sub_sec_category_list.getJSONObject(j).getString("sub_category_id");
                                    String sub_category_name = sub_sec_category_list.getJSONObject(j).getString("sub_category_name");
                                    String sub_category_image = sub_sec_category_list.getJSONObject(j).getString("sub_category_image");
                                    String sub_main_category_id = sub_sec_category_list.getJSONObject(j).getString("sub_main_category_id");
                                    String sec_sub_main_category_id = sub_sec_category_list.getJSONObject(j).getString("sec_sub_main_category_id");

                                    JSONArray sub_sec_category_list1 = sub_sec_category_list.getJSONObject(j).getJSONArray("sub_sec_category_list");
                                    ArrayList<String> sub_sec_category_array = new ArrayList<>();
                                    for (int m = 0; m < sub_sec_category_list1.length(); m++) {
                                        String sub_sec_category_id = sub_sec_category_list1.getJSONObject(m).getString("sub_sec_category_id");
                                        sub_sec_category_array.add(sub_sec_category_id);
                                    }
                                    System.out.println("position -- > " + j + " name " + sub_category_name);

                                    TopBrandData topBrandData = new TopBrandData(sub_category_id, sub_category_name, sub_category_image, sub_main_category_id, sec_sub_main_category_id, sub_sec_category_array);
                                    topBrandDataArrayList.add(topBrandData);
                                }
                            }
                        }

                        TopBrandListAdapter homeCatergoryAdap = new TopBrandListAdapter(getActivity(), topBrandDataArrayList);
                        mTopBrandList.setAdapter(homeCatergoryAdap);
                        mTopBrandList.setVisibility(View.VISIBLE);

                        homeCatergoryAdap.setOnItemClickListener(new TopBrandListAdapter.MyClickListener() {
                            @Override
                            public void onItemClick(int position, View v) {

                                try {
                                    System.out.println("position -- > " + position + " name " + topBrandDataArrayList.get(position).getSub_category_name());

                                    Bundle bundle = new Bundle();
                                    bundle.putString("main_category_id", "");
                                    bundle.putString("sec_category_id", "");
                                    bundle.putString("sub_category_id", topBrandDataArrayList.get(position).getSub_category_id());
                                    bundle.putString("sub_sec_category_id", "");
                                    bundle.putString("discount", "");
                                    bundle.putString("title", "");
                                    bundle.putString("page_title", topBrandDataArrayList.get(position).getSub_category_name());
                                    bundle.putString("price_min", "");
                                    bundle.putString("price_max", "");
                                    bundle.putString("availability", "1");
                                    bundle.putString("sort_order_by", "");
                                    ProductListFragment fragment2 = new ProductListFragment();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.add(R.id.fragment_container, fragment2);
                                    fragment2.setArguments(bundle);
                                    fragmentTransaction.addToBackStack("");
                                    fragmentTransaction.commit();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });


                    } else {
                        mTopBrandList.setVisibility(View.GONE);
                    }

                    topOffersDataArrayList.clear();
                    JSONArray product_top_offer = json.getJSONArray("product_top_offer");
                    if (product_top_offer.length() > 0) {

                        for (int i = 0; i < product_top_offer.length(); i++) {

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
                            SessionSave.saveSession("currency_symbol", product_top_offer.getJSONObject(i).getString("currency_symbol"), getActivity());
                            TopOffersData dealsData = new TopOffersData(product_id, product_title, product_price, product_discount_price, product_percentage, product_type, product_image, merchant_name);
                            topOffersDataArrayList.add(dealsData);
                        }

                        TopOffersListAdapter homeCatergoryAdap = new TopOffersListAdapter(getActivity(), topOffersDataArrayList);
                        mTopOffersList.setAdapter(homeCatergoryAdap);
                        mTopOffersList.setVisibility(View.VISIBLE);
                        topoffersLay.setVisibility(View.VISIBLE);

                        homeCatergoryAdap.setOnItemClickListener(new TopOffersListAdapter.MyClickListener() {
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
                                    fragmentTransaction.add(R.id.fragment_container, fragment2, "category");
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
                        topoffersLay.setVisibility(View.GONE);
                    }
                    mostsubPopularDataArrayList.clear();

                    JSONArray most_popular_product = json.getJSONArray("most_popular_product");
                    if (most_popular_product.length() > 0) {

                        for (int i = 0; i < most_popular_product.length(); i++) {

                            String product_id = most_popular_product.getJSONObject(i).getString("product_id");
                            String product_title = most_popular_product.getJSONObject(i).getString("product_title");
                            String product_price = most_popular_product.getJSONObject(i).getString("product_price");
                            String product_discount_price = most_popular_product.getJSONObject(i).getString("product_discount_price");
                            String product_type = most_popular_product.getJSONObject(i).getString("product_type");
                            String product_image = most_popular_product.getJSONObject(i).getString("product_image");
                            String merchant_name = product_top_offer.getJSONObject(i).optString("merchant_name");

                            MostPopularData dealsData = new MostPopularData(product_id, product_title, product_price, product_discount_price, "",product_type, product_image, merchant_name);
                            mostsubPopularDataArrayList.add(dealsData);
                        }

                        System.out.println("most_popular_product " + mostsubPopularDataArrayList.size());
                        MostSubCategroyAdapter homeCatergoryAdap = new MostSubCategroyAdapter(getActivity(), mostsubPopularDataArrayList);
                        mMostPopularList.setAdapter(homeCatergoryAdap);
                        mMostPopularList.setVisibility(View.VISIBLE);
                        popularproductLay.setVisibility(View.VISIBLE);
                        mostpopularproductLay.post(new Runnable() {
                            public void run() {
                                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (mMostPopularList.getHeight() + 70));
                                farmelayout.setLayoutParams(lp);
                            }
                        });
                        homeCatergoryAdap.setOnItemClickListener(new MostSubCategroyAdapter.MyClickListener() {
                            @Override
                            public void onItemClick(int position, View v) {

                                try {
                                    ((MainActivity) getActivity()).isTouchFromHome = true;
                                    Bundle bundle = new Bundle();
                                    bundle.putString("title", mostsubPopularDataArrayList.get(position).getProduct_title());
                                    bundle.putString("cat_id", mostsubPopularDataArrayList.get(position).getProduct_id());
                                    ProductDetailsFragment fragment2 = new ProductDetailsFragment();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.add(R.id.fragment_container, fragment2, "category");
                                    fragment2.setArguments(bundle);
                                    fragmentTransaction.addToBackStack("");
                                    fragmentTransaction.commit();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    } else {
                        popularproductLay.setVisibility(View.GONE);
                    }


                }else {
                    nestedScrollView.setVisibility(View.GONE);
                    mProgressView.setVisibility(View.GONE);
                    noProductTv.setVisibility(View.VISIBLE);
                    ((MainActivity) getActivity()).showSnackBar(nestedScrollView, json.optString("message"));

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

    public void applyFont(View view) {
        ApplyFont.applyLightFont(getActivity(), view.findViewById(R.id.topoffesTxt));
        ApplyFont.applyLightFont(getActivity(), view.findViewById(R.id.popularTxt));
        ApplyFont.applyBold(getActivity(), view.findViewById(R.id.viewall_button));
        ApplyFont.applyBold(getActivity(), view.findViewById(R.id.viewall_button_2));

    }


    public void productCategoryListData(JsonObject mJsonObject) {

        try {
            showProgress(false);

            if (mJsonObject != null) {

                JSONObject json = new JSONObject(mJsonObject.toString());
                if (json.getString("status").equalsIgnoreCase("200")) {

                    topOffersDataArrayList.clear();

                    JSONArray product_top_offer = json.getJSONArray("product_list");
                    if (product_top_offer.length() > 0) {

                        for (int i = 0; i < product_top_offer.length(); i++) {

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

                            SessionSave.saveSession("currency_symbol", product_top_offer.getJSONObject(i).getString("currency_symbol"), getActivity());
                            TopOffersData dealsData = new TopOffersData(product_id, product_title, product_price, product_discount_price, product_percentage, product_type, product_image, merchant_name);
                            topOffersDataArrayList.add(dealsData);
                        }

                        TopOffersListAdapter homeCatergoryAdap = new TopOffersListAdapter(getActivity(), topOffersDataArrayList);
                        mTopOffersList.setAdapter(homeCatergoryAdap);
                        mTopOffersList.setVisibility(View.VISIBLE);
                        topoffersLay.setVisibility(View.VISIBLE);
                    }


                } else {
                    nestedScrollView.setVisibility(View.GONE);
                    mProgressView.setVisibility(View.GONE);
                    ((MainActivity) getActivity()).showSnackBar(nestedScrollView, "" + "Sorry unable to reach server!!! ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}