package com.saveup.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aapbd.appbajarlib.notification.AlertMessage;
import com.google.gson.JsonObject;
import com.saveup.activity.MainActivity;
import com.saveup.adapter.OrderListDetailsAdapter;
import com.saveup.android.R;
import com.saveup.model.OrderDetailsModel;
import com.saveup.presenter.UserPresenter;
import com.saveup.utils.ApplyFont;
import com.saveup.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * @author AAPBD on 9,May,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.adapter
 */


public class OrderDetailsFragment extends Fragment {

    private String couponCode, redeemedAt, expiresAt, merchantName;

    private TextView orderDate, grandTotal, itemsCount;
    private Button redeemCouponBtn;
    private ImageView productimage;
    private String orderId = "";
    private RecyclerView orderRecyclerview;
    private ProgressBar progress;
    private UserPresenter userPresenter;
    private ArrayList<OrderDetailsModel> items = new ArrayList<>();
    private OrderListDetailsAdapter orderListDetailsAdapter;

    private TextView couponCodeTv, redeemedAtTv, expiresAtTv, redeemCouponTv, merchantNameTv,remaining_amount;
    private CardView couponDetailsCardView;

    private int pageIndex;
    private String orderDateData = "", grandTotalData = "";

    private LinearLayout orderExpireLinearLayout,lnrRemainigAmount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.orderdetailsfargment, container, false);

        orderDate = view.findViewById(R.id.orderDate);
        itemsCount = view.findViewById(R.id.itemsCount);
        grandTotal = view.findViewById(R.id.grandTotal);
        productimage = view.findViewById(R.id.productimage);
        userPresenter = new UserPresenter(this);
        orderRecyclerview = view.findViewById(R.id.orderRecyclerview);
        progress = view.findViewById(R.id.progress);
        redeemCouponBtn = view.findViewById(R.id.redeem_coupon_btn);

        couponDetailsCardView = view.findViewById(R.id.order_details_redeem_card_view);
        couponCodeTv = view.findViewById(R.id.order_details_coupon_tv);
        redeemedAtTv = view.findViewById(R.id.order_details_redeemed_at_tv);
        expiresAtTv = view.findViewById(R.id.order_details_expires_tv);
        redeemCouponTv = view.findViewById(R.id.redeem_coupon_tv);
        merchantNameTv = view.findViewById(R.id.order_details_merchant_name_tv);
        remaining_amount = view.findViewById(R.id.remaining_amount);
        lnrRemainigAmount = view.findViewById(R.id.lnrRemainigAmount);

        orderExpireLinearLayout = view.findViewById(R.id.order_details_expire_linear_layout);

        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        redeemCouponBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!StringUtil.isEmpty(couponCode)) {

                    if (!expiresAtTv.getText().toString().equalsIgnoreCase(getString(R.string.expired))) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(getString(R.string.redeem_coupon));
                        builder.setMessage(getString(R.string.are_you_sure_redeem_coupon));
                        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                if (progress.getVisibility() == View.GONE) {
                                    progress.setVisibility(View.VISIBLE);
                                }
                                userPresenter.redeemCouponByUser(couponCode);

                                System.out.println("checking_coupon_conde>>>" + couponCode);

                            }
                        }).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                        builder.setCancelable(false);
                        builder.show();
                    } else {
                        AlertMessage.showMessage(getActivity(), getString(R.string.error), getString(R.string.voucher_expired));
                    }
                }
            }
        });

        applyFont(view);

        return view;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Bundle bundle = getArguments();


        ((MainActivity) getActivity()).searchIcon.setVisible(false);
     //   ((MainActivity) getActivity()).cartItem.setVisible(false);
        ((MainActivity) getActivity()).cartIconempty.setVisible(false);
        ((MainActivity) getActivity()).editIcon.setVisible(false);

        final MainActivity mainActivity = (MainActivity) getActivity();

        if (bundle != null) {
            pageIndex = bundle.getInt("index_position");
            mainActivity.oderFragmentTabIndex = pageIndex;
        }

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
        mainActivity.toolbar.setTitle("" + getString(R.string.voucher_details));
        ((MainActivity) getActivity()).toolbar.setLogo(R.drawable.transpent_logo);

        if (bundle != null) {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE d MMM yyyy");

            String convertedDate = "";

            try {
                System.out.println("orderdate --- >  " + bundle.getString("orderdate"));

                Date date1 = format.parse(bundle.getString("orderdate"));
                convertedDate = dateFormat.format(date1.getTime());

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date1);
                calendar.add(Calendar.DATE, Integer.parseInt(bundle.getString("expire_day")));

                if (new Date().after(calendar.getTime())) {
                    expiresAt = getString(R.string.expired);
                } else {
                    expiresAt = simpleDateFormat.format(calendar.getTime());
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

            orderDateData = convertedDate + "";
            grandTotalData = bundle.getString("itemprice") + "";
            orderId = bundle.getString("orderId");

            try {
                userPresenter.orderListDetails(orderId);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

    }

    public void redeemCouponResponse(JsonObject jsonObject) {
        Log.e("API_RESPONSE", "redeemCouponResponse: null");

        progress.setVisibility(View.GONE);
        if (jsonObject != null) {
            Log.e("API_RESPONSE", "redeemCouponResponse: " + jsonObject.toString());

            try {
                JSONObject json = new JSONObject(jsonObject.toString());
                if (json.getString("status").equals("200")) {
                    redeemCouponTv.setVisibility(View.GONE);
                    redeemCouponBtn.setVisibility(View.GONE);
                    orderExpireLinearLayout.setVisibility(View.GONE);
                    couponDetailsCardView.setVisibility(View.VISIBLE);
                    couponCodeTv.setText(couponCode);
                    redeemedAtTv.setText(new SimpleDateFormat("EEEE d MMM yyyy").format(new Date()));
                    merchantNameTv.setText(json.getJSONObject("redeem_data").optString("merchant_name"));


                    if (json.getString("payment_type").equalsIgnoreCase("partial")){
                        remaining_amount.setText(json.getString("remaining_amount"));
                        lnrRemainigAmount.setVisibility(View.VISIBLE);
                    }

                    System.out.println("remaining_amount>>>>"+json.getString("remaining_amount"));



                } else {
                    Toast.makeText(getActivity(), json.optString("message"), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.unable_to_reach_server), Toast.LENGTH_SHORT).show();
        }
    }

    public void showData(JsonObject jsonObject) {

        progress.setVisibility(View.GONE);
        if (jsonObject != null) {
            try {
                JSONObject json = new JSONObject(jsonObject.toString());
                if (json.getString("status").equals("200")) {

                    couponCode = json.optString("coupon_code");
                    redeemedAt = json.optString("user_redemption_time");
                    merchantName = json.optString("merchant_name");

                    JSONArray product_order_cod_list = json.getJSONArray("order_product");
                    if (product_order_cod_list.length() > 0) {

                        for (int i = 0; i < product_order_cod_list.length(); i++) {

                            String order_id = product_order_cod_list.getJSONObject(i).getString("order_id");
                            String product_id = product_order_cod_list.getJSONObject(i).getString("product_id");
                            String product_title = product_order_cod_list.getJSONObject(i).getString("product_title");
                            String product_image = product_order_cod_list.getJSONObject(i).getString("product_image");
                            String order_qty = product_order_cod_list.getJSONObject(i).getString("order_qty");
                            String product_currency_symbol = product_order_cod_list.getJSONObject(i).getString("product_currency_symbol");
                            String product_currency_code = product_order_cod_list.getJSONObject(i).getString("product_currency_code");
                            String product_price = product_order_cod_list.getJSONObject(i).getString("product_price");
                            String product_quantity = product_order_cod_list.getJSONObject(i).getString("product_quantity");
                            String product_available_quantity = product_order_cod_list.getJSONObject(i).getString("product_available_quantity");
                            //     expiresAt = product_order_cod_list.getJSONObject(i).optString("offer_expire_date");

                            OrderDetailsModel dealsData = new OrderDetailsModel(order_id, product_id, product_title, product_image, order_qty
                                    , product_currency_symbol, product_currency_code, product_price, product_quantity, product_available_quantity);
                            items.add(dealsData);
                        }
                        itemsCount.setText("" + items.size());
                        orderDate.setText(orderDateData);
                        grandTotal.setText(grandTotalData);

                        orderListDetailsAdapter = new OrderListDetailsAdapter(getContext(), items);
                        orderRecyclerview.setAdapter(orderListDetailsAdapter);
                        orderRecyclerview.setHasFixedSize(true);
                        orderRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                        orderRecyclerview.setNestedScrollingEnabled(false);

                    }

                    SimpleDateFormat serverDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE d MMM yyyy");

                    if (!StringUtil.isEmpty(redeemedAt)) {
                        try {
                            Date serverDate = serverDateFormat.parse(redeemedAt);
                            redeemedAt = simpleDateFormat.format(serverDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }


                    if (json.optInt("is_redeem") == 1) {
                        couponDetailsCardView.setVisibility(View.VISIBLE);
                        couponCodeTv.setText(couponCode);
                        redeemedAtTv.setText(redeemedAt);
                        merchantNameTv.setText(merchantName);
                    } else {

                        orderExpireLinearLayout.setVisibility(View.VISIBLE);

                        if (expiresAt.equalsIgnoreCase(getString(R.string.expired))) {
                            expiresAtTv.setTextColor(getResources().getColor(R.color.dark_red));
                            expiresAtTv.setText(getString(R.string.expired));
                        } else {
                            expiresAtTv.setTextColor(getResources().getColor(R.color.white));
                            expiresAtTv.setText(getString(R.string.expires_at) + " " + expiresAt);
                        }
                        redeemCouponTv.setVisibility(View.VISIBLE);
                        redeemCouponTv.setText(getString(R.string.make_sure_to_redeem_only_when_you_are_at) + " " + merchantName);

                        redeemCouponBtn.setVisibility(View.VISIBLE);
                        redeemCouponBtn.setClickable(true);
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("API_RESPONSE", "show data: null");

        }

    }


    public void applyFont(View view) {
        ApplyFont.applyBold(getActivity(), couponCodeTv);
        ApplyFont.applyFont(getActivity(), redeemedAtTv);
        ApplyFont.applyFont(getActivity(), expiresAtTv);
        ApplyFont.applyFont(getActivity(), merchantNameTv);
        ApplyFont.applyFont(getActivity(), view.findViewById(R.id.order_details_redeemed_tv));
        ApplyFont.applyFont(getActivity(), view.findViewById(R.id.order_details_merchant_name_text_tv));

        //ApplyFont.applyBold(getActivity(), view.findViewById(R.id.viewall_button_2));

    }
}
