package com.saveup.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.aapbd.appbajarlib.nagivation.ShareUtils;
import com.aapbd.appbajarlib.notification.AlertMessage;
import com.aapbd.appbajarlib.view.WebViewFormatter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.saveup.activity.MainActivity;
import com.saveup.adapter.DetailsColorAdapter;
import com.saveup.adapter.DetailsSizeAdapter;
import com.saveup.adapter.RelatedProductAdapter;
import com.saveup.adapter.ReviewCommentsAdapter;
import com.saveup.android.R;
import com.saveup.model.ColorData;
import com.saveup.model.MostPopularData;
import com.saveup.model.ReviewData;
import com.saveup.model.SizeData;
import com.saveup.presenter.UserPresenter;
import com.saveup.utils.ApplyFont;
import com.saveup.utils.SessionSave;
import com.saveup.utils.SimpleDividerItemDecoration;
import com.saveup.utils.StringUtil;
import com.saveup.views.RippleView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;
import static com.facebook.login.widget.ProfilePictureView.TAG;

/**
 * Created by AAPBD on 13/4/17.
 */

public class ProductDetailsFragment extends Fragment {


    private ImageView product_image;
    private RecyclerView mColorList, mSizeList, reviewList, mRelatedproductList, mProductImageList;
    private TextView product_name, product_price, product_orginal_price, product_offers, product_mode_cash, product_exchange_offers, product_description, product_item_available_text, product_item_change,
            product_delivarydays_text, product_deal_include_des_tv, product_condition_des_tv, product_redemption_des_tv, product_redeem_tv,
            product_store_name_tv, product_store_address_tv;
    private Button product_writeReview, product_main_writeReview;
    private View mProgressView;
    ReviewCommentsAdapter homeCatergoryAdap;
    private View nestedScrollView;
    private ArrayList<SizeData> arraySizeList = new ArrayList<>();
    private ArrayList<ColorData> arrayColorList = new ArrayList<>();
    private ArrayList<String> arrayImageList = new ArrayList<>();
    private ArrayList<MostPopularData> relatedArrayList = new ArrayList<>();
    private ArrayList<ReviewData> reviewDatasList = new ArrayList<>();
    private LinearLayout mBuynow_Lay, mAddcart_Lay, relatedpopularLay, mBottom_lay;
    private LinearLayout product_review_card;
    private RelativeLayout farmelayout;
    private String colorId, sizeId, productId, store_img, ProductId, productPrice, productdiscount, percentage, product_ship_amt, product_tax_amt,video_url, product_type, loyality_point, total_loyality_point;
    private RippleView button_addCart, button_buyNow;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayout product_out_stock;
    private MainActivity mainActivity;
    private LinearLayout product_color_lay, product_size_lay, product_size_array, product_redeem_linear_layout, open_direction_linear_layout;
    private CardView product_deal_include_card_view, product_conditions_card_view, product_redemption_card_view, product_store_details_card_view,videoPlay;
    private ImageView product_store_iv;
    private int product_quantity;
    private double storeLat, storeLng;

    private RippleView shareDealBtn;
    private LinearLayout redeemAtLinearLayout;
    private String merchantId, storeId, productUrl;

    private ViewPager productImageVp;
    private MyProductImageViewPagerAdapter myViewPagerAdapter;

    private TextView productImageSlideNoTv, productImageTotalSlideTv;
    private WebView partnerVideo;
    private Context con;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.productdetailsfragment, container, false);
        con = view.getContext();



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

        product_deal_include_des_tv = view.findViewById(R.id.product_deal_include_description);
        product_condition_des_tv = view.findViewById(R.id.product_conditions_description);
        product_redemption_des_tv = view.findViewById(R.id.product_redemption_description);

        product_deal_include_card_view = view.findViewById(R.id.product_deal_include_card_view);
        product_conditions_card_view = view.findViewById(R.id.product_condition_card_view);
        product_redemption_card_view = view.findViewById(R.id.product_redemption_card_view);
        videoPlay = view.findViewById(R.id.videoPlay);

        product_redeem_linear_layout = view.findViewById(R.id.product_redeem_linear_layout);
        product_redeem_tv = view.findViewById(R.id.product_redeem_tv);

        product_store_details_card_view = view.findViewById(R.id.product_store_details_card_view);
        product_store_name_tv = view.findViewById(R.id.product_store_name_tv);
        product_store_address_tv = view.findViewById(R.id.product_store_address_tv);
        product_store_iv = view.findViewById(R.id.product_store_image_view);

        product_item_change = view.findViewById(R.id.product_item_change);
        product_delivarydays_text = view.findViewById(R.id.product_delivarydays_text);
        product_writeReview = view.findViewById(R.id.product_writeReview);
        product_main_writeReview = view.findViewById(R.id.product_main_writeReview);
        mAddcart_Lay = view.findViewById(R.id.addcart_Lay);
        mBuynow_Lay = view.findViewById(R.id.buynow_Lay);
        mBottom_lay = view.findViewById(R.id.bottom_lay);
        product_out_stock = view.findViewById(R.id.product_out_stock);
        product_color_lay = view.findViewById(R.id.product_color_lay);
        product_size_lay = view.findViewById(R.id.product_size_lay);
        product_size_array = view.findViewById(R.id.product_size_array);

        mBottom_lay.setVisibility(View.GONE);
        relatedpopularLay = view.findViewById(R.id.relatedpopularLay);
        product_review_card = view.findViewById(R.id.product_review_array);
        farmelayout = view.findViewById(R.id.farmelayout);

        partnerVideo = view.findViewById(R.id.partnerVideo);

        reviewList = view.findViewById(R.id.reviewList);
        reviewList.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity(), VERTICAL, false);
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
        mProductImageList.setLayoutManager(new LinearLayoutManager(getActivity(), VERTICAL, false));
        mProductImageList.setNestedScrollingEnabled(false);

        mColorList = view.findViewById(R.id.colorList);
        mColorList.setHasFixedSize(true);
        mColorList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mColorList.setNestedScrollingEnabled(false);

        mSizeList = view.findViewById(R.id.sizeList);
        mSizeList.setHasFixedSize(true);
        mSizeList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mSizeList.setNestedScrollingEnabled(false);

        ImageView shareThisDeal = view.findViewById(R.id.sharethisdealimageview);
        shareThisDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareThisDeal(v);
            }
        });


        open_direction_linear_layout = view.findViewById(R.id.opendirectionlay);
        open_direction_linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap();
            }
        });

        shareDealBtn = view.findViewById(R.id.product_details_share_deal_btn);
        shareDealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareThisDeal(view);
            }
        });

        redeemAtLinearLayout = view.findViewById(R.id.product_details_redeem_at_linear_layout);
        redeemAtLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callMerchantDetailsFragment();
            }
        });

        productImageVp = view.findViewById(R.id.product_details_image_view_pager);
        productImageSlideNoTv = view.findViewById(R.id.product_details_image_slide_no_tv);
        productImageTotalSlideTv = view.findViewById(R.id.product_details_image_slide_total_no_tv);

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
        arraySizeList.clear();
        arrayColorList.clear();
        arrayImageList.clear();
        ((MainActivity) getActivity()).searchIcon.setVisible(false);
        ((MainActivity) getActivity()).setTag = "productdeatils";
        mainActivity = (MainActivity) getActivity();
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
           // mainActivity.toolbar.setTitle("" + bundle.getString("title"));
            ((MainActivity) getActivity()).toolbar.setLogo(R.drawable.transpent_logo);
            mainActivity.productTitle = "" + bundle.getString("title");
            ProductId = bundle.getString("cat_id");
            UserPresenter userPresenter = new UserPresenter(ProductDetailsFragment.this);
            userPresenter.productDetaislPage("" + bundle.getString("cat_id"), SessionSave.getSession("user_id", getActivity()));

        }

        mAddcart_Lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arrayColorList.size() > 0 && arraySizeList.size() > 0) {
                    if (colorId == null) {
                        Toast.makeText(getActivity(), "" + getResources().getString(R.string.please_select_color), Toast.LENGTH_SHORT).show();
                    } else if (sizeId == null) {
                        Toast.makeText(getActivity(), "" + getResources().getString(R.string.please_select_size), Toast.LENGTH_SHORT).show();
                    } else {
                        addCart();
                    }
                } else if (arrayColorList.size() > 0) {

                    if (colorId == null) {
                        Toast.makeText(getActivity(), "" + getResources().getString(R.string.please_select_color), Toast.LENGTH_SHORT).show();
                    } else {
                        sizeId = "";
                        addCart();
                    }

                } else if (arraySizeList.size() > 0) {

                    if (sizeId == null) {
                        Toast.makeText(getActivity(), "" + getResources().getString(R.string.please_select_size), Toast.LENGTH_SHORT).show();
                    } else {
                        colorId = "";
                        addCart();
                    }

                } else {
                    colorId = "";
                    sizeId = "";
                    //addCart();
                    if(mainActivity.isGuestMode){
                        mainActivity.startLoginActivity();
                    }else{
                        if(product_type.equals("all_item")){
                            showPayAmountDialog(false);
                        }
                    }
                }

            }
        });

        button_addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (arrayColorList.size() > 0 && arraySizeList.size() > 0) {
                    if (colorId == null) {
                        Toast.makeText(getActivity(), "" + getResources().getString(R.string.please_select_color), Toast.LENGTH_SHORT).show();
                    } else if (sizeId == null) {
                        Toast.makeText(getActivity(), "" + getResources().getString(R.string.please_select_size), Toast.LENGTH_SHORT).show();
                    } else {
                        addCart();
                    }
                } else if (arrayColorList.size() > 0) {

                    if (colorId == null) {
                        Toast.makeText(getActivity(), "" + getResources().getString(R.string.please_select_color), Toast.LENGTH_SHORT).show();
                    } else {
                        sizeId = "";
                        addCart();
                    }

                } else if (arraySizeList.size() > 0) {

                    if (sizeId == null) {
                        Toast.makeText(getActivity(), "" + getResources().getString(R.string.please_select_size), Toast.LENGTH_SHORT).show();
                    } else {
                        colorId = "";
                        addCart();
                    }

                } else {
                    colorId = "";
                    sizeId = "";
                    //addCart();
                    if(mainActivity.isGuestMode){
                        mainActivity.startLoginActivity();
                    }else{
                        if(product_type.equals("all_item")){
                            showPayAmountDialog(false);
                        }
                    }
                }
            }
        });

        button_buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (button_buyNow.getText().toString().equalsIgnoreCase("add to wishlist")) {

                    if (arrayColorList.size() > 0 && arraySizeList.size() > 0) {
                        if (colorId == null) {
                            Toast.makeText(getActivity(), "" + getResources().getString(R.string.please_select_color), Toast.LENGTH_SHORT).show();
                        } else if (sizeId == null) {
                            Toast.makeText(getActivity(), "" + getResources().getString(R.string.please_select_size), Toast.LENGTH_SHORT).show();
                        } else {
                            // buyNow();
                            addToWishList();
                        }
                    } else if (arrayColorList.size() > 0) {

                        if (colorId == null) {
                            Toast.makeText(getActivity(), "" + getResources().getString(R.string.please_select_color), Toast.LENGTH_SHORT).show();
                        } else {
                            sizeId = "";
                            //buyNow();
                            addToWishList();
                        }

                    } else if (arraySizeList.size() > 0) {

                        if (sizeId == null) {
                            Toast.makeText(getActivity(), "" + getResources().getString(R.string.please_select_size), Toast.LENGTH_SHORT).show();
                        } else {
                            colorId = "";
                            //  buyNow();
                            addToWishList();
                        }

                    } else {
                        colorId = "";
                        sizeId = "";
                        // buyNow();
                        if(mainActivity.isGuestMode){
                            mainActivity.startLoginActivity();
                        }else{
                            addToWishList();
                        }
                    }
                } else{
                    if(button_buyNow.getText().toString().equalsIgnoreCase("wishlisted")){
                        if(mainActivity.isGuestMode){
                            mainActivity.startLoginActivity();
                        }else{
                            showProgress(true);
                            UserPresenter userPresenter = new UserPresenter(ProductDetailsFragment.this);
                            userPresenter.removeWishList("" + SessionSave.getSession("user_id", getActivity()), "" + productId, 0);
                        }
                    }
                }


            }
        });
        mBuynow_Lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (arrayColorList.size() > 0 && arraySizeList.size() > 0) {
                    if (colorId == null) {
                        Toast.makeText(getActivity(), "" + getResources().getString(R.string.please_select_color), Toast.LENGTH_SHORT).show();
                    } else if (sizeId == null) {
                        Toast.makeText(getActivity(), "" + getResources().getString(R.string.please_select_size), Toast.LENGTH_SHORT).show();
                    } else {
                        // buyNow();
                        addToWishList();
                    }
                } else if (arrayColorList.size() > 0) {

                    if (colorId == null) {
                        Toast.makeText(getActivity(), "" + getResources().getString(R.string.please_select_color), Toast.LENGTH_SHORT).show();
                    } else {
                        sizeId = "";
                        // buyNow();
                        addToWishList();
                    }

                } else if (arraySizeList.size() > 0) {

                    if (sizeId == null) {
                        Toast.makeText(getActivity(), "" + getResources().getString(R.string.please_select_size), Toast.LENGTH_SHORT).show();
                    } else {
                        colorId = "";
                        // buyNow();
                        addToWishList();
                    }

                } else {
                    colorId = "";
                    sizeId = "";
                    //buyNow();
                    if(mainActivity.isGuestMode){
                        mainActivity.startLoginActivity();
                    }else{
                        addToWishList();
                    }
                }
            }
        });

        product_writeReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("store_img", store_img);
                bundle.putString("store_name", mainActivity.productTitle);
                bundle.putString("store_id", ProductId);

                ProductReviewFragment fragment2 = new ProductReviewFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, fragment2, "review");
                fragment2.setArguments(bundle);
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();
            }
        });

        product_main_writeReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("store_img", store_img);
                bundle.putString("store_name", mainActivity.productTitle);
                bundle.putString("store_id", ProductId);

                ProductReviewFragment fragment2 = new ProductReviewFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, fragment2, "review");
                fragment2.setArguments(bundle);
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();
            }
        });


    }

    Dialog dialog;
    TextView titleTxt;
    TextView messageTxt;
    TextView messageTxt2;
    TextInputLayout payAmountTextInputLayout;
    TextInputEditText amountET;
    ProgressBar progress1;
    TextView cancel;
    TextView proceed;
    TextView ok;
   // Button promoCodeApplyButton;
   // TextInputEditText promoCodeET;
   // TextInputLayout couponTextInputLayout;
   // ProgressBar progressBar;

    private void showDialogProgress(boolean show){
        if(show){
            progress1.setVisibility(View.VISIBLE);
            //progressBar.setVisibility(View.VISIBLE);
            cancel.setClickable(false);
            proceed.setClickable(false);
            //promoCodeApplyButton.setBackgroundColor(getResources().getColor(R.color.yellow));
            //promoCodeApplyButton.setEnabled(false);
            amountET.setEnabled(false);
            //promoCodeET.setEnabled(false);
        }else{
            progress1.setVisibility(View.INVISIBLE);
           // progressBar.setVisibility(View.INVISIBLE);
            cancel.setClickable(true);
            proceed.setClickable(true);
            //promoCodeApplyButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            //promoCodeApplyButton.setEnabled(true);
            amountET.setEnabled(true);
           // promoCodeET.setEnabled(true);
        }
    }

    private void showPayAmountDialog(boolean retry){

        dialog = new Dialog(getActivity(), R.style.dialogwinddow);
        dialog.setContentView(R.layout.merchant_number_layout);

        titleTxt = dialog.findViewById(R.id.titleTxt);
        if(retry){
            titleTxt.setText(getString(R.string.pay_amount_error_try_again));
        }else{
            titleTxt.setText(getString(R.string.get_discount));
        }

        payAmountTextInputLayout = dialog.findViewById(R.id.text_input_layout);
        payAmountTextInputLayout.setHint(getString(R.string.pay_amount_hint));

        amountET = dialog.findViewById(R.id.number_input);

        cancel = dialog.findViewById(R.id.cancelTxt);
        proceed = dialog.findViewById(R.id.proceedTxt);
        progress1 = dialog.findViewById(R.id.progress1);


       /* promoCodeApplyButton = dialog.findViewById(R.id.payment_continue_apply_coupon_code_btn);
        promoCodeET = dialog.findViewById(R.id.payment_continue_coupon_code_et);
        couponTextInputLayout = dialog.findViewById(R.id.coupon_text_input_layout);
        couponTextInputLayout.setHint(getString(R.string.add_coupon_code));
        progressBar = dialog.findViewById(R.id.progress);



        promoCodeApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promoCodeApplyButton.setText(getString(R.string.applying));
                showDialogProgress(true);
                UserPresenter userPresenter = new UserPresenter(ProductDetailsFragment.this);
                userPresenter.applyCouponCode( promoCodeET.getText().toString().trim(),SessionSave.getSession("user_id", getActivity()));

            }
        });


        promoCodeET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() > 0) {
                    promoCodeApplyButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    promoCodeApplyButton.setEnabled(true);
                } else {
                    promoCodeApplyButton.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    promoCodeApplyButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                hideKeyboard();
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amountET.getText().toString().trim().isEmpty()){
                    payAmountTextInputLayout.setError(getString(R.string.pay_amount_error));
                }else{
                    showDialogProgress(true);
                    //dialog.dismiss();
                    UserPresenter userPresenter = new UserPresenter(ProductDetailsFragment.this);
                    userPresenter.orderByOTP(SessionSave.getSession("user_id", getActivity()), storeId,productId, amountET.getText().toString().trim());
                }
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /*public void CouponCodeResponse(JsonObject mJsonObject) {

        showDialogProgress(false);

        try {
            if (mJsonObject != null) {

                JSONObject json = new JSONObject(mJsonObject.toString());
                if (json.getString("status").equals("200")) {

                    int discountPrice = json.getJSONObject("coupon_details").optInt("coupon_amount");

                    payAmountTextInputLayout.setHint(getString(R.string.discount_pay_amount_hint));
                    if (discountPrice > 0) {
                        amountET.setText(String.valueOf(discountPrice));
                    }
                    amountET.setEnabled(false);
                    promoCodeET.setEnabled(false);
                    promoCodeApplyButton.setText(getString(R.string.applied));
                    promoCodeApplyButton.setBackgroundColor(getResources().getColor(R.color.green_dark));
                    couponTextInputLayout.setHint(getString(R.string.added_coupon_code));

                }else{

                    //real
                    promoCodeApplyButton.setText(getString(R.string.apply));
                    couponTextInputLayout.setError(getString(R.string.invalid_coupon_code));

                }
            }else{

                //test
               *//* payAmountTextInputLayout.setHint(getString(R.string.discount_pay_amount_hint));
                amountET.setText("100");
                amountET.setEnabled(false);
                promoCodeET.setEnabled(false);
                promoCodeApplyButton.setText(getString(R.string.applied));
                promoCodeApplyButton.setBackgroundColor(getResources().getColor(R.color.green_dark));
                couponTextInputLayout.setHint(getString(R.string.added_coupon_code));*//*


                //real
                promoCodeApplyButton.setText(getString(R.string.apply));
                couponTextInputLayout.setError(getString(R.string.invalid_coupon_code));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }*/



    public void orderByOTPData(JsonObject jsonObject){

        try {
            if (jsonObject != null) {
                showProgress(false);
                JSONObject json = new JSONObject(jsonObject.toString());
                if (json.getString("status").equals("200")) {
                   // TestOTP = json.get("code").toString();
                    showOTPDialog(json.get("message").toString());
                } else {
                    showPayAmountDialog(true);
                }
            }else{
                showResultDialog("Something went wrong, please try later");
            }
        }catch (Exception ex) {
            ex.printStackTrace();
            showResultDialog("Something went wrong, please try later");
        }
    }



    String TestOTP = "";
    private void showOTPDialog(String message){

        if(dialog != null){
            dialog.dismiss();
        }

        dialog = new Dialog(getActivity(), R.style.dialogwinddow);
        dialog.setContentView(R.layout.merchant_number_layout);

        titleTxt = dialog.findViewById(R.id.titleTxt);
        titleTxt.setText(getString(R.string.merchant_otp));

        messageTxt = dialog.findViewById(R.id.message);
        messageTxt.setVisibility(View.VISIBLE);
        messageTxt.setText(message);

        payAmountTextInputLayout = (TextInputLayout) dialog.findViewById(R.id.text_input_layout);

        amountET = dialog.findViewById(R.id.number_input);
        amountET.setHint(getString(R.string.merchant_otp_hint));
        amountET.setText(TestOTP);
        cancel = dialog.findViewById(R.id.cancelTxt);
        proceed = dialog.findViewById(R.id.proceedTxt);
        dialog.findViewById(R.id.coupon_container).setVisibility(View.GONE);
        progress1 = dialog.findViewById(R.id.progress1);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                hideKeyboard();
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amountET.getText().toString().trim().isEmpty()){
                    payAmountTextInputLayout.setError(getString(R.string.otp_error));
                }else{
                    //dialog.dismiss();
                    showDialogProgress(true);
                    UserPresenter userPresenter = new UserPresenter(ProductDetailsFragment.this);
                    userPresenter.orderVerifyByOTP(SessionSave.getSession("user_id", getActivity()), storeId, amountET.getText().toString().trim());
                }
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void orderVerifiedOTPData(JsonObject jsonObject){

        try {

            if (jsonObject != null) {
                showProgress(false);

                JSONObject json = new JSONObject(jsonObject.toString());
                if (json.getString("status").equals("200")) {
                    loyality_point = json.getString("loyality_point");
                    total_loyality_point = json.getString("total_loyality_point");
                    SessionSave.saveSession("total_loyality_point", total_loyality_point, getContext());
                    showResultDialog(json.get("message").toString());
                }else{
                    showResultDialog(json.get("message").toString());
                }
            }else{
                showResultDialog("OTP Code Incorrect");
            }

        }catch (Exception ex) {
            ex.printStackTrace();
            showResultDialog("OTP Code Incorrect");
        }
    }

    private void showResultDialog(String message){

        if(dialog != null){
            dialog.dismiss();
        }

        dialog = new Dialog(getActivity(), R.style.dialogwinddow);
        dialog.setContentView(R.layout.merchant_number_layout);

        titleTxt = dialog.findViewById(R.id.titleTxt);
        titleTxt.setText(getString(R.string.merchant_otp));

        messageTxt = dialog.findViewById(R.id.message);
        messageTxt.setVisibility(View.VISIBLE);
        messageTxt.setText(message);

        if(loyality_point != null && !loyality_point.isEmpty()){
            messageTxt2 = dialog.findViewById(R.id.messag2);
            messageTxt2.setVisibility(View.VISIBLE);
            messageTxt2.setText(String.format(getResources().getString(R.string.rewards_total_loyality_point_messgae), loyality_point, total_loyality_point));
            loyality_point = new String();
        }

        payAmountTextInputLayout = (TextInputLayout) dialog.findViewById(R.id.text_input_layout);

        amountET = dialog.findViewById(R.id.number_input);
        amountET.setVisibility(View.GONE);
        cancel = dialog.findViewById(R.id.cancelTxt);
        cancel.setVisibility(View.GONE);
        proceed = dialog.findViewById(R.id.proceedTxt);
        proceed.setVisibility(View.GONE);
        ok = dialog.findViewById(R.id.okTxt);
        ok.setVisibility(View.VISIBLE);
        dialog.findViewById(R.id.coupon_container).setVisibility(View.GONE);
        progress1 = dialog.findViewById(R.id.progress1);



        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                hideKeyboard();
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }



    private void addCart() {
        showProgress(true);
        UserPresenter userPresenter = new UserPresenter(ProductDetailsFragment.this);
        userPresenter.addCartPage("" + productId, sizeId, colorId, "1", SessionSave.getSession("user_id", getActivity()));

    }

    private void addToWishList() {

        showProgress(true);
        UserPresenter userPresenter = new UserPresenter(ProductDetailsFragment.this);
        userPresenter.addWishListPageFromProductDetails(SessionSave.getSession("user_id", getActivity()), productId);
    }

    private void buyNow() {
        Bundle bundle = new Bundle();
        bundle.putString("product_img", store_img);
        bundle.putString("product_name", mainActivity.productTitle);
        bundle.putString("product_id", ProductId);
        bundle.putString("product_size_id", sizeId);
        bundle.putString("product_color_id", colorId);
        bundle.putString("pricelist", productPrice + "-" + productdiscount + "-" + percentage);
        bundle.putString("product_ship_amt", product_ship_amt);
        bundle.putString("product_tax_amt", product_tax_amt);
        bundle.putInt("product_quantity", product_quantity);


        ProductBuyNowFragment fragment2 = new ProductBuyNowFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, fragment2, "review");
        fragment2.setArguments(bundle);
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.commit();

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getActivity().getResources().getInteger(android.R.integer.config_shortAnimTime);

            nestedScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
            mBottom_lay.setVisibility(show ? View.GONE : View.VISIBLE);
            nestedScrollView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    nestedScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
                    mBottom_lay.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mBottom_lay.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    public void productDetailsData(JsonObject jsonObject) {

        try {

            if (jsonObject != null) {
                showProgress(false);

                JSONObject json1 = new JSONObject(jsonObject.toString());
                JSONObject json = json1.getJSONObject("product_details");
                if (json1.getString("status").equals("200")) {

                    JSONArray product_image_array = json.getJSONArray("product_image");
                    arraySizeList.clear();
                    arrayColorList.clear();
                    arrayImageList.clear();
                    relatedArrayList.clear();
                    if (product_image_array.length() > 0) {
                        for (int i = 0; i < product_image_array.length(); i++) {

                            String images = product_image_array.getJSONObject(i).getString("images");
                            arrayImageList.add(images);

                        }

                        addImageSlideNo(0);
                        myViewPagerAdapter = new MyProductImageViewPagerAdapter(arrayImageList);
                        productImageVp.setAdapter(myViewPagerAdapter);
                        productImageVp.addOnPageChangeListener(viewPagerPageChangeListener);

//                        ProductImageAdapter detailsSizeAdapter = new ProductImageAdapter(getActivity(), arrayImageList);
//                        mProductImageList.setAdapter(detailsSizeAdapter);
//                        mProductImageList.setVisibility(View.VISIBLE);
//
//                        detailsSizeAdapter.setOnItemClickListener(new ProductImageAdapter.MyClickListener() {
//                            @Override
//                            public void onItemClick(int position, View v) {
//
//                                try {
//
//                                    Glide.with(getActivity())
//                                            .load(arrayImageList.get(position))
//                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                            .error(R.drawable.noimage)
//                                            .into(product_image);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        });

                    } else {
                        mProductImageList.setVisibility(View.GONE);
                    }
                    store_img = product_image_array.getJSONObject(0).getString("images");

                    productUrl = json.optString("url");
                    productUrl = json.optString("url");

                    Glide.with(getActivity())
                            .load(product_image_array.getJSONObject(0).getString("images"))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.drawable.noimage)
                            .into(product_image);
                    productId = "" + json.getString("product_id");
                    product_name.setText("" + json.getString("product_title"));
                    productPrice = json.getString("product_original_price");
                    productdiscount = json.getString("product_discount_price");
                    percentage = json.getString("product_discount_percentage");
                    product_ship_amt = json.getString("product_ship_amt");
                    product_tax_amt = json.getString("product_including_tax");
                    video_url = json.getString("video_url");
                    product_type = json.getString("product_type"); //all_item,individual_item

                    if(product_type.equals("all_item")){
                        product_price.setVisibility(View.GONE);
                        product_orginal_price.setVisibility(View.GONE);
                        product_offers.setVisibility(View.VISIBLE);
                    }


                    System.out.println("video_url"+video_url);

                    if (video_url ==null ||video_url.equalsIgnoreCase("null")){
                        videoPlay.setVisibility(View.GONE);

                    }

                    if (video_url!=null){

                        if (video_url!="null"){
                            System.out.println("Partner_video>>"+video_url);
                            videoPlay.setVisibility(View.VISIBLE);
                            WebViewFormatter.formatWebViewWithClient(partnerVideo, true, true);
                            WebSettings settings = partnerVideo.getSettings();
                            settings.setDomStorageEnabled(true);
                            partnerVideo.loadUrl(video_url);
                        }

                    }


                    //description
                    if (!StringUtil.isEmpty(json.optString("deal_include")) && !json.optString("deal_include").equalsIgnoreCase("null")) {
                        product_deal_include_card_view.setVisibility(View.VISIBLE);
                        product_deal_include_des_tv.setText(Html.fromHtml(json.optString("deal_include")));
                    }
                    if (!StringUtil.isEmpty(json.optString("conditions")) && !json.optString("conditions").equalsIgnoreCase("null")) {
                        product_conditions_card_view.setVisibility(View.VISIBLE);
                        product_condition_des_tv.setText(Html.fromHtml(json.optString("conditions")));
                    }
                    if (!StringUtil.isEmpty(json.optString("redemption")) && !json.optString("redemption").equalsIgnoreCase("null")) {
                        product_redemption_card_view.setVisibility(View.VISIBLE);
                        product_redemption_des_tv.setText(Html.fromHtml(json.optString("redemption")));
                    }

                    //redeem day
                    /*if (json.getInt("day") != 0) {
                        product_redeem_linear_layout.setVisibility(View.VISIBLE);

                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.DATE, json.getInt("day"));

                        product_redeem_tv.setText(getString(R.string.redeem_offer_from_today_until, new SimpleDateFormat("EEEE d MMM yyyy").format(calendar.getTime())));
                    }*/

                    //store details
                    JSONObject storeJson = json.optJSONObject("store_details");

                    if (storeJson != null && storeJson.length() > 0) {

                        product_store_details_card_view.setVisibility(View.VISIBLE);

                        storeLat = storeJson.getDouble("store_latitude");
                        storeLng = storeJson.getDouble("store_longitude");

                        if (storeLat != 0 && storeLng != 0) {
                            open_direction_linear_layout.setVisibility(View.VISIBLE);
                        }

                        product_store_name_tv.setText(storeJson.optString("merchant_name"));
                        product_store_address_tv.setText(storeJson.optString("store_name"));


                        merchantId = storeJson.optString("merchant_id");
                        storeId = storeJson.optString("store_id");

                        Glide.with(getActivity())
                                .load(storeJson.optString("merchant_img"))
                                .error(R.drawable.noimage)
                                .into(product_store_iv);
                    }

                    // is wishlisted or not
                    if (json.optBoolean("showWishlist")) {
                        resetWishListButton(false);
                    }


                    product_quantity = json.getInt("product_quantity") - json.getInt("product_purchase_qty");
                    if (json.getString("product_sold_status").equals("0")) {
                        product_out_stock.setVisibility(View.VISIBLE);
                        mBottom_lay.setVisibility(View.GONE);
                    } else {
                        product_out_stock.setVisibility(View.GONE);
                        mBottom_lay.setVisibility(View.VISIBLE);
                    }
                    product_price.setText(json.getString("currency_symbol") + " " + json.getString("product_discount_price"));


                    product_orginal_price.setText(json.getString("currency_symbol") + " " + json.getString("product_original_price"));
                    product_orginal_price.setPaintFlags(product_orginal_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    String text = "<font color=#000000>" + getResources().getString(R.string.product_delivary_details) + "</font> <font color=#ff2353>" + " " + json.getString("product_delivery") + getResources().getString(R.string.days) + "</font>";
                    product_delivarydays_text.setText(Html.fromHtml(text));
                    product_offers.setText("" + json.getString("product_discount") + " % OFF");
                    product_description.setText(Html.fromHtml(json.getString("product_description")));

                    // apply readmore

                    // AppConstant.makeTextViewResizable(product_description,3, product_description.getText().toString(), true);


                    JSONArray product_size_details = json.getJSONArray("product_size_details");
                    if (product_size_details.length() > 0) {
                        arraySizeList.clear();
                        for (int i = 0; i < product_size_details.length(); i++) {
                            String size_id = product_size_details.getJSONObject(i).getString("size_id");
                            String size_name = product_size_details.getJSONObject(i).getString("size_name");
                            String product_size_id = product_size_details.getJSONObject(i).getString("product_size_id");


                            SizeData sizeData = new SizeData(size_id, size_name, product_size_id, "#FFFFFF");
                            arraySizeList.add(sizeData);


                        }

                        final DetailsSizeAdapter detailsSizeAdapter = new DetailsSizeAdapter(getActivity(), arraySizeList);
                        mSizeList.setAdapter(detailsSizeAdapter);
                        mSizeList.setVisibility(View.VISIBLE);
                        detailsSizeAdapter.setOnItemClickListener(new DetailsSizeAdapter.MyClickListener() {
                            @Override
                            public void onItemClick(int position, View v) {

                                try {


                                    System.out.println("colorId : " + arraySizeList.get(position).getProduct_size_id());

                                    sizeId = arraySizeList.get(position).getProduct_size_id();
                                    for (int i = 0; i < arraySizeList.size(); i++) {

                                        if (i == position) {
                                            SizeData sizeData = new SizeData(arraySizeList.get(position).getSize_id(), arraySizeList.get(position).getSize_name(), arraySizeList.get(position).getProduct_size_id(), "#ff2353");
                                            arraySizeList.set(position, sizeData);
                                        } else {
                                            SizeData sizeData = new SizeData(arraySizeList.get(i).getSize_id(), arraySizeList.get(i).getSize_name(), arraySizeList.get(i).getProduct_size_id(), "#FFFFFF");
                                            arraySizeList.set(i, sizeData);
                                        }

                                    }

                                    detailsSizeAdapter.notifyDataSetChanged();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });


                    } else {
                        mSizeList.setVisibility(View.GONE);
                        product_size_lay.setVisibility(View.GONE);
                    }

                    JSONArray product_color_details = json.getJSONArray("product_color_details");
                    if (product_color_details.length() > 0) {
                        arrayColorList.clear();
                        for (int i = 0; i < product_color_details.length(); i++) {
                            String color_id = product_color_details.getJSONObject(i).getString("color_id");
                            String color_code = product_color_details.getJSONObject(i).getString("color_code");
                            String color_name = product_color_details.getJSONObject(i).getString("color_name");
                            String product_color_id = product_color_details.getJSONObject(i).getString("product_color_id");
                            ColorData colorData = new ColorData(color_id, color_name, color_code, product_color_id, "#FFFFFF");
                            arrayColorList.add(colorData);

                        }

                        final DetailsColorAdapter colorAdapter = new DetailsColorAdapter(getActivity(), arrayColorList);
                        mColorList.setAdapter(colorAdapter);
                        mColorList.setVisibility(View.VISIBLE);

                        colorAdapter.setOnItemClickListener(new DetailsColorAdapter.MyClickListener() {
                            @Override
                            public void onItemClick(int position, View v) {

                                try {
                                    System.out.println("colorId : " + arrayColorList.get(position).getProduct_color_id());

                                    colorId = arrayColorList.get(position).getProduct_color_id();

                                    for (int i = 0; i < arrayColorList.size(); i++) {

                                        if (i == position) {
                                            ColorData colorData = new ColorData(arrayColorList.get(position).getColor_id(), arrayColorList.get(position).getColor_name(), arrayColorList.get(position).getColor_code(), arrayColorList.get(position).getProduct_color_id(), "#ff2353");
                                            arrayColorList.set(position, colorData);
                                        } else {
                                            ColorData colorData = new ColorData(arrayColorList.get(i).getColor_id(), arrayColorList.get(i).getColor_name(), arrayColorList.get(i).getColor_code(), arrayColorList.get(i).getProduct_color_id(), "#FFFFFF");
                                            arrayColorList.set(i, colorData);
                                        }

                                    }

                                    colorAdapter.notifyDataSetChanged();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                    } else {
                        mColorList.setVisibility(View.GONE);
                        product_color_lay.setVisibility(View.GONE);

                    }


                    if (arrayColorList.size() == 0 && arraySizeList.size() == 0) {
                        product_size_array.setVisibility(View.GONE);
                    }

                    JSONArray product_review = json.getJSONArray("product_review");
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
                        if (json.optBoolean("showReviewForm")) {
                            product_writeReview.setVisibility(View.VISIBLE);
                        }
                    } else {
                        reviewList.setVisibility(View.GONE);
                        product_review_card.setVisibility(View.GONE);
                        if (json.optBoolean("showReviewForm")) {
                            product_main_writeReview.setVisibility(View.VISIBLE);
                        }
                    }

                    JSONArray related_products = json.getJSONArray("related_products");
                    if (related_products.length() > 0) {

                        for (int i = 0; i < related_products.length(); i++) {

                            String product_id = related_products.getJSONObject(i).getString("product_id");
                            String product_title = related_products.getJSONObject(i).getString("product_title");
                            String product_price = related_products.getJSONObject(i).getString("product_price");
                            String product_discount_price = related_products.getJSONObject(i).getString("product_discount_price");
                            String product_image = related_products.getJSONObject(i).getString("product_image");
                            String product_type = related_products.getJSONObject(i).getString("product_type");
                            String product_percentage = "";
                            if(product_type.equals("all_item")){
                                product_percentage = related_products.getJSONObject(i).getString("product_discount");
                            }else{
                                product_percentage = related_products.getJSONObject(i).getString("product_discount_price");
                            }
                            String merchant_name = related_products.getJSONObject(i).optString("merchant_name");

                            MostPopularData dealsData = new MostPopularData(product_id, product_title, product_price, product_discount_price, product_percentage,product_type, product_image, merchant_name);
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

                        //is review enabled
//                        if (!json.optBoolean("showReviewForm")) {
//                            product_main_writeReview.setVisibility(View.GONE);
//                            product_writeReview.setVisibility(View.GONE);
//                        }

                        homeCatergoryAdap.setOnItemClickListener(new RelatedProductAdapter.MyClickListener() {
                            @Override
                            public void onItemClick(int position, View v) {

                                try {

                                    nestedScrollView.scrollTo(0, 0);
                                    showProgress(true);
                                    ((MainActivity) getActivity()).toolbar.setTitle("" + relatedArrayList.get(position).getProduct_title());
                                    ((MainActivity) getActivity()).toolbar.setLogo(R.drawable.transpent_logo);
                                    UserPresenter userPresenter = new UserPresenter(ProductDetailsFragment.this);
                                    userPresenter.productDetaislPage("" + relatedArrayList.get(position).getProduct_id(), SessionSave.getSession("user_id", getActivity()));


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

    private void callMerchantDetailsFragment() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isfromMap", true);
        bundle.putString("store_id", merchantId);
        StoreListFragment fragment2 = new StoreListFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, fragment2);
        fragment2.setArguments(bundle);
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.commit();
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


                    //partial payment
                    String partial_payment = json1.getString("partial_payment");
                    String total_commission = json1.getString("total_commission");

                    SessionSave.saveSession("partial_payment", partial_payment, getActivity());
                    SessionSave.saveSession("total_commission", total_commission, getActivity());

                    //showing dialog when adding two items in cart

//                    if(json1.optInt("cart_count") == 2) {
//
//                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//                        builder.setTitle(R.string.purchasing_multiple_vouchers);
//                        builder.setMessage(R.string.multiple_product_alert);
//
//                        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.dismiss();
//                            }
//                        }).setCancelable(true);
//                        builder.show();
//                    }

                    ((MainActivity) getActivity()).setBadgeCount(getActivity(), SessionSave.getSession("cartCount", getActivity()));
                    Snackbar.make(nestedScrollView, "" + json1.getString("message"), Snackbar.LENGTH_SHORT).show();


                } else {
                    showProgress(false);

                    if (json1.getString("message").equalsIgnoreCase("please add product from the same store!")) {
                        AlertMessage.showMessage(getActivity(), getString(R.string.error), "Please add product from the same store or clear existing cart first.");
                    } else {
                        Snackbar.make(nestedScrollView, "" + json1.getString("message"), Snackbar.LENGTH_SHORT).show();
                    }


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


    /*
    Open map
     */
    public void openMap() {

        // GPSTracker tracker = new GPSTracker(con);

        if (storeLat != 0 && storeLng != 0) {
            //MapByIntent.showDirection(con, tracker.getLatitude(), tracker.getLongitude(), storeLat, storeLng);

            String uri = "http://maps.google.com/maps?daddr=" + storeLat + "," + storeLng;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {

                try {
                    Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(unrestrictedIntent);
                } catch (ActivityNotFoundException innerEx) {
                    Toast.makeText(getActivity(), R.string.please_install_map, Toast.LENGTH_LONG).show();
                }
            }

            //map navigation code

//            Uri navigationIntentUri = Uri.parse("google.navigation:q=" + storeLat + "," + storeLng);
//            Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
//            mapIntent.setPackage("com.google.android.apps.maps");
//            startActivity(mapIntent);

        } else {

            AlertMessage.showMessage(getActivity(), getString(R.string.error), getString(R.string.store_location_not_found));

//            AlertDialog.Builder alertDialog = new AlertDialog.Builder(con);
//            alertDialog.setTitle("Turn on GPS");
//            alertDialog.setMessage("You have to turn on GPS from the settings.");
//            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    Intent intent = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
//                    con.startActivity(intent);
//                }
//            });
//            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.cancel();
//                }
//            });
//            alertDialog.show();
        }


    }


    public void shareThisDeal(View v) {

        ShareUtils.toTextToAll(con, "Hey, checkout this awesome voucher from SaveUpBD App!" + "\n" + productUrl);
    }

    public void applyFont(View view) {

        ApplyFont.applyMediumFont(getActivity(), view.findViewById(R.id.product_name));
        ApplyFont.applyMediumFont(getActivity(), view.findViewById(R.id.product_offers));
        ApplyFont.applyMediumFont(getActivity(), view.findViewById(R.id.product_orginal_price));
        ApplyFont.applyBold(getActivity(), view.findViewById(R.id.product_price));
        ApplyFont.applyMediumFont(getActivity(), view.findViewById(R.id.policyLay));
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

    private void resetWishListButton(boolean status) {

        if (status) {
            button_buyNow.setText(getString(R.string.add_to_wishlist));
            button_buyNow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_wishlist, 0, 0, 0);
        } else {
            button_buyNow.setText(getString(R.string.wishlisted));
            button_buyNow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_wishlisted, 0, 0, 0);
        }
    }

    //add wishlist response

    public void addWishListData(JsonObject mJsonObject) {

        try {
            showProgress(false);
            if (mJsonObject != null) {

                JSONObject jsonObject = new JSONObject(mJsonObject.toString());

                if (jsonObject.getString("status").equals("200")) {
                    resetWishListButton(false);
                }
                ((MainActivity) getActivity()).showSnackBar(nestedScrollView, jsonObject.optString("message"));

            } else {
                nestedScrollView.setVisibility(View.GONE);
                ((MainActivity) getActivity()).showSnackBar(nestedScrollView, "" + getString(R.string.unable_to_reach_server));
            }
        } catch (Exception e) {
            showProgress(false);
            e.printStackTrace();
        }
    }


    //remove wishlist response
    public void removeWishListData(JsonObject mJsonObject) {
        try {
            showProgress(false);
            if (mJsonObject != null) {

                JSONObject json = new JSONObject(mJsonObject.toString());
                if (json.getString("status").equals("200")) {
                    resetWishListButton(true);
                }
                ((MainActivity) getActivity()).showSnackBar(nestedScrollView, json.optString("message"));

            } else {
                nestedScrollView.setVisibility(View.GONE);
                ((MainActivity) getActivity()).showSnackBar(nestedScrollView, "" + getString(R.string.unable_to_reach_server));
            }
        } catch (Exception ex) {
            showProgress(false);
            ex.printStackTrace();
        }
    }

    //product image view pager adapter

    /**
     * View pager adapter
     */
    public class MyProductImageViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        List<String> productImageList;

        public MyProductImageViewPagerAdapter(List<String> productImageList) {
            this.productImageList = productImageList;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.product_details_image_layout, container, false);
            container.addView(view);

            ImageView productImage = view.findViewById(R.id.product_details_image);

            Glide.with(getActivity())
                    .load(productImageList.get(position))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.noimage)
                    .into(productImage);
            return view;
        }

        @Override
        public int getCount() {
            return productImageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

    }

    //slides bottom dots
    private void addImageSlideNo(int currentPage) {

        productImageSlideNoTv.setText(currentPage + 1 + "");
        productImageTotalSlideTv.setText(String.valueOf(arrayImageList.size()));
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addImageSlideNo(position);

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void hideKeyboard(){
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}
