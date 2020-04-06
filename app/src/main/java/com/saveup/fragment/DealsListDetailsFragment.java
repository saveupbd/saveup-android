package com.saveup.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saveup.activity.MainActivity;
import com.saveup.adapter.ProductImageAdapter;
import com.saveup.adapter.RelatedProductAdapter;
import com.saveup.adapter.ReviewCommentsAdapter;
import com.saveup.android.R;
import com.saveup.model.MostPopularData;
import com.saveup.model.ReviewData;
import com.saveup.presenter.UserPresenter;
import com.saveup.utils.ApplyFont;
import com.saveup.utils.SessionSave;
import com.saveup.utils.SimpleDividerItemDecoration;
import com.saveup.views.RippleView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
 * @author AAPBD on 19,April,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.adapter
 */


public class DealsListDetailsFragment extends Fragment {

    private ImageView product_image;
    private RecyclerView reviewList, mRelatedproductList, mProductImageList;
    private TextView product_name, product_price, product_orginal_price, product_offers, product_mode_cash, product_exchange_offers, product_description, product_item_available_text, product_item_change;
    private View mProgressView;
    private View nestedScrollView;
    private Button product_writeReview, product_main_writeReview;
    private ArrayList<String> arrayImageList = new ArrayList<>();
    private ArrayList<MostPopularData> relatedArrayList = new ArrayList<>();
    private ArrayList<ReviewData> reviewDatasList = new ArrayList<>();
    private LinearLayout mBuynow_Lay, mAddcart_Lay, relatedpopularLay, mBottom_lay;
    private RelativeLayout farmelayout;
    private String colorId = "", sizeId = "",product_tax_amt,product_ship_amt ,dealsId = "", store_img, ProductId, productPrice, productdiscount, percentage;
    private RippleView button_addCart, button_buyNow;
    private CardView card_product_size_array, card_product_delivary_array;
    private LinearLayout product_review_card;
    ReviewCommentsAdapter homeCatergoryAdap;
    private TextView dealstimer;
    private LinearLayout dealstimerLay;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayout product_out_stock;
    private  int available_limit;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.productdetailsfragment, container, false);
        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        mProgressView = view.findViewById(R.id.progressBar);

        product_image = view.findViewById(R.id.product_image);
        product_name = view.findViewById(R.id.product_name);
        product_price = view.findViewById(R.id.product_price);
        product_orginal_price = view.findViewById(R.id.product_orginal_price);
        product_offers = view.findViewById(R.id.product_offers);
        product_mode_cash = view.findViewById(R.id.product_mode_cash);
        button_addCart = view.findViewById(R.id.button_addCart);
        button_buyNow = view.findViewById(R.id.button_buyNow);
        product_exchange_offers = view.findViewById(R.id.product_exchange_offers);
        product_description = view.findViewById(R.id.product_description);
        product_item_available_text = view.findViewById(R.id.product_item_available_text);
        dealstimer = view.findViewById(R.id.dealstimer);
        dealstimerLay = view.findViewById(R.id.dealstimerLay);
        product_out_stock= view.findViewById(R.id.product_out_stock);
        product_item_change = view.findViewById(R.id.product_item_change);
        card_product_size_array = view.findViewById(R.id.card_product_size_array);
        card_product_size_array.setVisibility(View.GONE);
        card_product_delivary_array = view.findViewById(R.id.card_product_delivary_array);
        card_product_delivary_array.setVisibility(View.GONE);

        product_writeReview = view.findViewById(R.id.product_writeReview);
        product_main_writeReview = view.findViewById(R.id.product_main_writeReview);

        mAddcart_Lay = view.findViewById(R.id.addcart_Lay);
        mBuynow_Lay = view.findViewById(R.id.buynow_Lay);
        mBottom_lay = view.findViewById(R.id.bottom_lay);
        mBottom_lay.setVisibility(View.GONE);
        mAddcart_Lay.setVisibility(View.GONE);
        relatedpopularLay = view.findViewById(R.id.relatedpopularLay);
        product_review_card = view.findViewById(R.id.product_review_array);
        farmelayout = view.findViewById(R.id.farmelayout);

        reviewList = view.findViewById(R.id.reviewList);
        reviewList.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        reviewList.setLayoutManager(linearLayoutManager);
        reviewList.setNestedScrollingEnabled(false);
        reviewList.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        mRelatedproductList = view.findViewById(R.id.relatedproductList);
        mRelatedproductList.setHasFixedSize(true);
        mRelatedproductList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRelatedproductList.setNestedScrollingEnabled(false);
        //mRelatedproductList.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        mProductImageList = view.findViewById(R.id.productImageList);
        mProductImageList.setHasFixedSize(true);
        mProductImageList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mProductImageList.setNestedScrollingEnabled(false);
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
        ((MainActivity) getActivity()).searchIcon.setVisible(false);
        arrayImageList.clear();
        ((MainActivity) getActivity()).setTag = "dealsdeatils";
        final MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.search_layout.setVisibility(View.GONE);
        mainActivity.menu_Name.setVisible(false);
        mainActivity.toggle.setDrawerIndicatorEnabled(false);
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
            showProgress(true);
            mainActivity.toolbar.setTitle("" + bundle.getString("title"));
            ((MainActivity) getActivity()).toolbar.setLogo(R.drawable.transpent_logo);
            mainActivity.productTitle = "" + bundle.getString("title");
            dealsId = "" + bundle.getString("deal_id");
            UserPresenter userPresenter = new UserPresenter(DealsListDetailsFragment.this);
            userPresenter.dealsDetaislPage("" + bundle.getString("deal_id"));


        }

        mAddcart_Lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showProgress(true);
                UserPresenter userPresenter = new UserPresenter(DealsListDetailsFragment.this);
                userPresenter.dealsaddcartPage("" + dealsId, "1", SessionSave.getSession("user_id", getActivity()));


            }
        });

        button_addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showProgress(true);
                UserPresenter userPresenter = new UserPresenter(DealsListDetailsFragment.this);
                userPresenter.dealsaddcartPage("" + dealsId, "1", SessionSave.getSession("user_id", getActivity()));

            }
        });
        button_buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Bundle bundle = new Bundle();
                bundle.putString("product_img", store_img);
                bundle.putString("product_name", mainActivity.productTitle);
                bundle.putString("product_id", dealsId);
                bundle.putString("product_size_id", "");
                bundle.putString("product_color_id", "");
                bundle.putString("pricelist", productPrice + "-" + productdiscount + "-" + percentage);
                bundle.putString("product_ship_amt", product_ship_amt);
                bundle.putString("product_tax_amt", product_tax_amt);
                bundle.putInt("available_limit", available_limit);


                DealBuyNowFragment fragment2 = new DealBuyNowFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, fragment2, "review");
                fragment2.setArguments(bundle);
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();

            }
        });
        mBuynow_Lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Bundle bundle = new Bundle();
                bundle.putString("product_img", store_img);
                bundle.putString("product_name", mainActivity.productTitle);
                bundle.putString("product_id", dealsId);
                bundle.putString("product_size_id", "");
                bundle.putString("product_color_id", "");
                bundle.putString("pricelist", productPrice + "-" + productdiscount + "-" + percentage);
                bundle.putString("product_ship_amt", product_ship_amt);
                bundle.putString("product_tax_amt", product_tax_amt);



                DealBuyNowFragment fragment2 = new DealBuyNowFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, fragment2, "review");
                fragment2.setArguments(bundle);
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();

            }
        });

        product_writeReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dealsId != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("store_img", store_img);
                    bundle.putString("store_name", mainActivity.productTitle);
                    bundle.putString("store_id", dealsId);

                    DealsReviewFragment fragment2 = new DealsReviewFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.fragment_container, fragment2, "review");
                    fragment2.setArguments(bundle);
                    fragmentTransaction.addToBackStack("");
                    fragmentTransaction.commit();
                }
            }
        });

        product_main_writeReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dealsId != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("store_img", store_img);
                    bundle.putString("store_name", mainActivity.productTitle);
                    bundle.putString("store_id", dealsId);

                    DealsReviewFragment fragment2 = new DealsReviewFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.fragment_container, fragment2, "review");
                    fragment2.setArguments(bundle);
                    fragmentTransaction.addToBackStack("");
                    fragmentTransaction.commit();
                }
            }
        });
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getActivity().getResources().getInteger(android.R.integer.config_shortAnimTime);

            nestedScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
          //  mBottom_lay.setVisibility(show ? View.GONE : View.VISIBLE);
            nestedScrollView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    nestedScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
//                    mBottom_lay.setVisibility(show ? View.GONE : View.VISIBLE);
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
           // mBottom_lay.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void dealsDetailsData(JsonObject jsonObject) {

        try {


            if (jsonObject != null) {


                JSONObject json1 = new JSONObject(jsonObject.toString());
                if (json1.getString("status").equals("200")) {
                    showProgress(false);
                    JSONObject json = json1.getJSONObject("deal_list");
                    JSONArray deal_image_array = json.getJSONArray("deal_image");
                    arrayImageList.clear();
                    relatedArrayList.clear();
                    if (deal_image_array.length() > 0) {
                        for (int i = 0; i < deal_image_array.length(); i++) {

                            String images = deal_image_array.getJSONObject(i).getString("images");
                            arrayImageList.add(images);

                        }

                        ProductImageAdapter detailsSizeAdapter = new ProductImageAdapter(getActivity(), arrayImageList);
                        mProductImageList.setAdapter(detailsSizeAdapter);
                        mProductImageList.setVisibility(View.VISIBLE);

                        detailsSizeAdapter.setOnItemClickListener(new ProductImageAdapter.MyClickListener() {
                            @Override
                            public void onItemClick(int position, View v) {

                                try {

                                    Glide.with(getActivity())
                                            .load(arrayImageList.get(position))
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .error(R.drawable.noimage)
                                            .into(product_image);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                    } else {
                        mProductImageList.setVisibility(View.GONE);
                    }


                    productPrice = json.getString("deal_original_price");
                    productdiscount = json.getString("deal_discount_price");
                    percentage = json.getString("deal_discount_percentage");
//                    if(json.has("deal_sold_status") && json.getString("deal_sold_status").equals("0")){
//                        product_out_stock.setVisibility(View.VISIBLE);
//                        mBottom_lay.setVisibility(View.GONE);
//                    }else {
//                        product_out_stock.setVisibility(View.GONE);
//                        mBottom_lay.setVisibility(View.VISIBLE);
//                    }
                    Glide.with(getActivity())
                            .load(deal_image_array.getJSONObject(0).getString("images"))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.drawable.noimage)
                            .into(product_image);
                    dealsId = "" + json.getString("deal_id");
                    store_img = deal_image_array.getJSONObject(0).getString("images");
                    product_name.setText("" + json.getString("deal_title")); //deal_including_tax
                    product_tax_amt ="" + json.getString("deal_including_tax");
                    product_ship_amt ="" + json.getString("deal_ship_amt");
                    available_limit = json.getInt("deal_max_limit") -json.getInt("deal_no_of_purchase");
                    if(available_limit ==0){
                        product_out_stock.setVisibility(View.VISIBLE);
                        mBottom_lay.setVisibility(View.GONE);
                    }else {
                        product_out_stock.setVisibility(View.GONE);
                        mBottom_lay.setVisibility(View.VISIBLE);
                    }

                    ((MainActivity) getActivity()).productTitle = ""+ json.getString("deal_title");
                    ((MainActivity) getActivity()).toolbar.setTitle("" + json.getString("deal_title"));
                    product_price.setText(json.getString("deal_currency_symbol") + " " + json.getString("deal_discount_price"));
                    product_orginal_price.setText(json.getString("deal_currency_symbol") + " " + json.getString("deal_original_price"));
                    product_orginal_price.setPaintFlags(product_orginal_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    product_offers.setText("" + json.getString("deal_discount_percentage") + " % OFF");
                    String deal_end_date = json.getString("deal_end_date");
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    enddate = format.parse(deal_end_date);
                    dealstimerLay.setVisibility(View.VISIBLE);
                    startUpdateTimer();

                    product_description.setText("" + json.getString("deal_description"));
                    JSONArray product_review = json.getJSONArray("deal_review");
                    if (product_review.length() > 0) {

                        for (int i = 0; i < product_review.length(); i++) {


                            String review_title = product_review.getJSONObject(i).getString("review_title");
                            String review_comments = product_review.getJSONObject(i).getString("review_comments");
                            String ratings = product_review.getJSONObject(i).getString("ratings");
                            String review_date = product_review.getJSONObject(i).getString("review_date");
                            String user_id = product_review.getJSONObject(i).getString("user_id");
                            String user_name = product_review.getJSONObject(i).getString("user_name");
                            String user_img = product_review.getJSONObject(i).getString("user_img");
                            ReviewData dealsData = new ReviewData(review_title, review_comments, ratings, review_date, user_id, user_name, user_img);
                            reviewDatasList.add(dealsData);
                        }

                        System.out.println("most_popular_product " + relatedArrayList.size());
                        homeCatergoryAdap = new ReviewCommentsAdapter(getActivity(), reviewDatasList);
                        reviewList.setAdapter(homeCatergoryAdap);
                        reviewList.setVisibility(View.VISIBLE);
                        product_review_card.setVisibility(View.VISIBLE);
                        product_main_writeReview.setVisibility(View.GONE);


                    } else {
                        reviewList.setVisibility(View.GONE);
                        product_review_card.setVisibility(View.GONE);
                        product_main_writeReview.setVisibility(View.VISIBLE);
                    }

                    JSONArray related_products = json.getJSONArray("related_deals");
                    if (related_products.length() > 0) {

                        for (int i = 0; i < related_products.length(); i++) {

                            String product_id = related_products.getJSONObject(i).getString("deal_id");
                            String product_title = related_products.getJSONObject(i).getString("deal_title");
                            String product_price = related_products.getJSONObject(i).getString("deal_original_price");
                            String product_discount_price = related_products.getJSONObject(i).getString("deal_discount_price");
                            String product_type = related_products.getJSONObject(i).getString("product_type");
                            String product_image = related_products.getJSONObject(i).getString("deal_image");
                            String deal_discount_percentage = related_products.getJSONObject(i).getString("deal_discount_percentage");
                            MostPopularData dealsData = new MostPopularData(product_id, product_title, product_price, product_discount_price, deal_discount_percentage, product_type, product_image, "");
                            relatedArrayList.add(dealsData);
                        }


                        System.out.println("most_popular_product " + relatedArrayList.size());
                        RelatedProductAdapter homeCatergoryAdap = new RelatedProductAdapter(getActivity(), relatedArrayList);
                        mRelatedproductList.setAdapter(homeCatergoryAdap);
                        mRelatedproductList.setVisibility(View.VISIBLE);
                        relatedpopularLay.setVisibility(View.VISIBLE);
                        relatedpopularLay.post(new Runnable() {
                            public void run() {
                                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (relatedpopularLay.getHeight() - 25));
                                farmelayout.setLayoutParams(lp);
                            }
                        });

                        homeCatergoryAdap.setOnItemClickListener(new RelatedProductAdapter.MyClickListener() {
                            @Override
                            public void onItemClick(int position, View v) {

                                try {

                                    nestedScrollView.scrollTo(0, 0);
                                    showProgress(true);
                                    ((MainActivity) getActivity()).toolbar.setTitle("" + relatedArrayList.get(position).getProduct_title());
                                    ((MainActivity) getActivity()).toolbar.setLogo(R.drawable.transpent_logo);
                                    UserPresenter userPresenter = new UserPresenter(DealsListDetailsFragment.this);
                                    userPresenter.dealsDetaislPage("" + relatedArrayList.get(position).getProduct_id());


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });


                    } else {
                        mRelatedproductList.setVisibility(View.GONE);
                        relatedpopularLay.setVisibility(View.GONE);
                    }


                } else {
                    nestedScrollView.setVisibility(View.GONE);
                    mProgressView.setVisibility(View.GONE);
                    Snackbar.make(nestedScrollView, "" + json1.getString("message"), Snackbar.LENGTH_SHORT).show();
                }

            } else {
                nestedScrollView.setVisibility(View.GONE);
                mProgressView.setVisibility(View.GONE);
                ((MainActivity) getActivity()).showSnackBar(nestedScrollView, "" + "Sorry unable to reach server!!! ");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    private Handler mHandler = new Handler();
    private Date enddate;
    private Runnable updateRemainingTimeRunnable = new Runnable() {
        @Override
        public void run() {

            long currentTime = System.currentTimeMillis();

            updateTimeRemaining(currentTime,enddate.getTime());

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

        if (timeDiff > 0) {
            int seconds = (int) (timeDiff / 1000) % 60;
            int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
            int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);
            int numOfDays = (int) (timeDiff / (1000 * 60 * 60 * 24));

            if(numOfDays>0) {
                dealstimer.setText(" Deals End with " +String.format("%02d", numOfDays) + " days " + String.format("%02d", hours) + " hrs " + String.format("%02d", minutes) + " mins " + String.format("%02d", seconds) + " secs");

            }else {
                dealstimer.setText(" Deals End with " + String.format("%02d", hours) + " hrs " + String.format("%02d", minutes) + " mins " + String.format("%02d", seconds) + " secs");
            }

        } else {
            dealstimer.setText(" Deals End with " + String.format("%02d", 0) + " hrs " + String.format("%02d", 0) + " mins " + String.format("%02d", 0) + " secs");

        }
    }

    public void addCartData(JsonObject jsonObject) {

        try {

            if (jsonObject != null) {


                JSONObject json1 = new JSONObject(jsonObject.toString());

                showProgress(false);
                if (json1.getString("status").equals("200")) {


                    Log.d(TAG, "addCartData: " + json1.getJSONArray("cart_details").length());


                    if (SessionSave.getSession("cartCount", getActivity()).equals("")) {

                        SessionSave.saveSession("cartCount", "" + 1, getActivity());


                    } else {
                        int count = Integer.parseInt(SessionSave.getSession("cartCount", getActivity())) + 1;

                        SessionSave.saveSession("cartCount", "" + count, getActivity());
                    }


                    ((MainActivity) getActivity()).setBadgeCount(getActivity(), SessionSave.getSession("cartCount", getActivity()));
                    Snackbar.make(nestedScrollView, "" + json1.getString("message"), Snackbar.LENGTH_SHORT).show();


                } else {
                    showProgress(false);
                    nestedScrollView.setVisibility(View.GONE);
                    Snackbar.make(nestedScrollView, "" + json1.getString("message"), Snackbar.LENGTH_SHORT).show();

                }
            } else {

                nestedScrollView.setVisibility(View.GONE);
                mProgressView.setVisibility(View.GONE);
                ((MainActivity) getActivity()).showSnackBar(nestedScrollView, "" + "Sorry unable to reach server!!! ");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public void applyFont(View view) {

        ApplyFont.applyMediumFont(getActivity(), view.findViewById(R.id.product_name));
        ApplyFont.applyMediumFont(getActivity(), view.findViewById(R.id.product_offers));
        ApplyFont.applyMediumFont(getActivity(), view.findViewById(R.id.product_orginal_price));
        ApplyFont.applyBold(getActivity(), view.findViewById(R.id.product_price));
        ApplyFont.applyMediumFont(getActivity(), view.findViewById(R.id.policyLay));
        ApplyFont.applyMediumFont(getActivity(), view.findViewById(R.id.product_size_array));
        ApplyFont.applyMediumFont(getActivity(), view.findViewById(R.id.product_size_array));
        ApplyFont.applyMediumFont(getActivity(), view.findViewById(R.id.product_description_text));
        ApplyFont.applyLightFont(getActivity(), view.findViewById(R.id.product_description));
        ApplyFont.applyBold(getActivity(), view.findViewById(R.id.product_writeReview));
        ApplyFont.applyLightFont(getActivity(), view.findViewById(R.id.relatedpopularTxt));

    }

    public void reviewListUpdate(JSONArray product_review) {

        try {
            if (product_review.length() > 0) {

                for (int i = 0; i < product_review.length(); i++) {


                    String review_title = product_review.getJSONObject(i).getString("review_title");
                    String review_comments = product_review.getJSONObject(i).getString("review_comments");
                    String ratings = product_review.getJSONObject(i).getString("ratings");
                    String review_date = product_review.getJSONObject(i).getString("review_date");
                    String user_id = product_review.getJSONObject(i).getString("user_id");
                    String user_name = product_review.getJSONObject(i).getString("user_name");
                    String user_img = product_review.getJSONObject(i).getString("user_img");
                    ReviewData dealsData = new ReviewData(review_title, review_comments, ratings, review_date, user_id, user_name, user_img);
                    reviewDatasList.add(dealsData);
                }


            }

            if (homeCatergoryAdap != null) {
                linearLayoutManager.setReverseLayout(true);
                linearLayoutManager.setStackFromEnd(true);
                homeCatergoryAdap.notifyDataSetChanged();
            } else {
                homeCatergoryAdap = new ReviewCommentsAdapter(getActivity(), reviewDatasList);
                reviewList.setAdapter(homeCatergoryAdap);
                reviewList.setVisibility(View.VISIBLE);
                product_review_card.setVisibility(View.VISIBLE);

                product_main_writeReview.setVisibility(View.GONE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
