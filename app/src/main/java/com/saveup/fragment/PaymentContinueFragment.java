package com.saveup.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.aapbd.appbajarlib.display.KeyBoardManager;
import com.google.gson.JsonObject;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.saveup.activity.MainActivity;
import com.saveup.activity.PaymentCompletedScreen;
import com.saveup.activity.PaymentWebViewActivity;
import com.saveup.adapter.CountryCityBaseAdapter;
import com.saveup.adapter.CoutryCityAdapter;
import com.saveup.android.BuildConfig;
import com.saveup.android.R;
import com.saveup.model.CityListData;
import com.saveup.model.CountryListData;
import com.saveup.presenter.UserPresenter;
import com.saveup.utils.ApplyFont;
import com.saveup.utils.NetworkStatus;
import com.saveup.utils.SessionSave;
import com.saveup.utils.StringUtil;
import com.saveup.views.RippleView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PaymentContinueFragment extends Fragment {

    private LinearLayout paypalLay, cashonLay, couponAmountLay,lnrPaymentOption;
    private RadioButton paypalradio, cashonradio, yesradio, noradio, fullRadio, partialRadio;
    private TextView no_of_items_txt, itemsPrice, shipping_charges, total_charges, tax_charges;
    private RippleView button_payment;
    private boolean ispaymenType;
    private String ship_id, ship_name, ship_email, ship_phone, ship_address1, ship_address2, ship_country_id, ship_city_id, ship_state, ship_postalcode;
    private View progressBar;
    private NestedScrollView mainTopLay;
    private LinearLayout shippingAddressLay, shippingAddressEdtLay;
    private CardView card_ship_address;
    private EditText edtName, edtNumber, edtEmail, edtbuildingName, edtLocalityName, edtPincode, edtState;
    private Spinner spin_country, spin_city;
    ArrayList<CountryListData> mCountryList = new ArrayList<>();
    ArrayList<CityListData> mCityList;
    ArrayList<CityListData> mEmptyList = new ArrayList<>();
    public String urlType;
    private String colorId, sizeId, ProductId, quanty, totalPrice;
    private String transaction_id, token_id, payer_email, payer_id, payer_name;
    private final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;
    // note that these credentials will differ between live & sandbox environments.
    private String CONFIG_CLIENT_ID = "AfiSFrZ9FxvKNGLpSPs-r4d0cD7_NpMKl3sjmhzTYgn5x-wlFX4Behtgbvizu5TWTrF8MfcN1k64Cn6r";
    private final int REQUEST_CODE_PAYMENT = 1;
    private int defaultCountry = 0, defaultCity = 0;
    String defaultTextForCountrySpinner = "Country";

    private TextView promoCodeStatusTv, itemOriginalPrice;
    private EditText addCouponCodeEt, edtPartialPayment;
    private Button applyCouponCodeBtn;
    private String couponCode;
    private String paymentOption = StringUtil.FULL;

    private double productTotalPrice;

    String partial ;


    private PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID).merchantName("Example Merchant")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.paymentcontinuefragment, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        mainTopLay = view.findViewById(R.id.mainLay);
        paypalLay = view.findViewById(R.id.paypalLay);
        cashonLay = view.findViewById(R.id.cashonLay);
        shippingAddressLay = view.findViewById(R.id.shippingAddressLay);
        shippingAddressEdtLay = view.findViewById(R.id.shippingAddressEdtLay);
        card_ship_address = view.findViewById(R.id.card_ship_address);

        spin_country = view.findViewById(R.id.spin_country);
        spin_city = view.findViewById(R.id.spin_city);
        lnrPaymentOption = view.findViewById(R.id.lnrPaymentOption);

        edtName = view.findViewById(R.id.edtshipName);
        edtNumber = view.findViewById(R.id.edtshipNumber);
        edtEmail = view.findViewById(R.id.edtshipEmail);
        edtbuildingName = view.findViewById(R.id.edtbuildingName);
        edtLocalityName = view.findViewById(R.id.edtLocalityName);
        edtPincode = view.findViewById(R.id.edtPincode);
        edtState = view.findViewById(R.id.edtState);

        paypalradio = view.findViewById(R.id.paypalradio);
        cashonradio = view.findViewById(R.id.cashonradio);
        noradio = view.findViewById(R.id.noRadio);
        yesradio = view.findViewById(R.id.yesRadio);
        fullRadio = view.findViewById(R.id.fullRadio);
        no_of_items_txt = view.findViewById(R.id.no_of_items_txt);
        itemsPrice = view.findViewById(R.id.itemsPrice);
        shipping_charges = view.findViewById(R.id.shipping_charges);
        total_charges = view.findViewById(R.id.total_charges);
        tax_charges = view.findViewById(R.id.tax_charges);
        button_payment = view.findViewById(R.id.button_payment);
        partialRadio = view.findViewById(R.id.partialRadio);

        addCouponCodeEt = view.findViewById(R.id.payment_continue_coupon_code_et);
        promoCodeStatusTv = view.findViewById(R.id.payment_continue_promo_code_status_tv);
        applyCouponCodeBtn = view.findViewById(R.id.payment_continue_apply_coupon_code_btn);
        itemOriginalPrice = view.findViewById(R.id.itemsOriginalPrice);
        couponAmountLay = view.findViewById(R.id.payment_coupon_amount_linear_layout);
        edtPartialPayment = view.findViewById(R.id.edtPartialPayment);


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
        mainActivity.toolbar.setTitle("" + getResources().getString(R.string.payment));
        ((MainActivity) getActivity()).toolbar.setLogo(R.drawable.transpent_logo);
        Bundle bundle = getArguments();
        ispaymenType = false;
        cashonradio.setChecked(true);
        paypalradio.setChecked(false);
        fullRadio.setChecked(true);
        partialRadio.setChecked(false);

        edtPartialPayment.setEnabled(false);

        //.....................partial Payment...............................//


        partial = SessionSave.getSession("partial",getActivity());


        Log.d("payment_continue",partial);




        if (SessionSave.getSession("partial_payment", getActivity()) != null) {

            Log.e("Paymentmode", "Of merchant is "+SessionSave.getSession("partial_payment", getActivity()));
//
//                                    <option value="1">Full Payment</option>
//                                    <option value="2">Partil Payment</option>
//                                    <option value="3">Both</option>

            if (SessionSave.getSession("partial_payment", getActivity()).equalsIgnoreCase("1")) {  // full
                fullRadio.setChecked(true);
                lnrPaymentOption.setVisibility(View.GONE);
                edtPartialPayment.setText(SessionSave.getSession("currency_symbol", getActivity()) + partial);
            }else if (SessionSave.getSession("partial_payment", getActivity()).equalsIgnoreCase("2")){ // only partial


                /*
                changed by biplob
                 */
                lnrPaymentOption.setVisibility(View.VISIBLE);
                fullRadio.setChecked(false);
                partialRadio.setChecked(true);

                /*
                hide full radio
                 */

                fullRadio.setVisibility(View.GONE);

                button_payment.setText(getResources().getString(R.string.pay) + " " + SessionSave.getSession("currency_symbol", getActivity()) + " " + partial);



            }else if (SessionSave.getSession("partial_payment", getActivity()).equalsIgnoreCase("3")){  // both
                lnrPaymentOption.setVisibility(View.VISIBLE);
            }
        }

        if (!SessionSave.getSession("client_id", getActivity()).equals("")) {

            CONFIG_CLIENT_ID = SessionSave.getSession("client_id", getActivity());
        }
        paypalLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ispaymenType = true;
                cashonradio.setChecked(false);
                paypalradio.setChecked(true);

            }
        });

        cashonLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ispaymenType = false;
                cashonradio.setChecked(true);
                paypalradio.setChecked(false);

            }
        });

        cashonradio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ispaymenType = false;
                cashonradio.setChecked(true);
                paypalradio.setChecked(false);
            }
        });

        paypalradio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ispaymenType = true;
                cashonradio.setChecked(false);
                paypalradio.setChecked(true);

            }
        });
        yesradio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noradio.setChecked(false);
                yesradio.setChecked(true);
                defaultCountry++;
                isProfileAddress(true);
                shippingAddressEdtLay.setVisibility(View.VISIBLE);
                mainTopLay.post(new Runnable() {
                    @Override
                    public void run() {
                        mainTopLay.scrollTo(0, shippingAddressLay.getBottom());
                    }
                });
            }
        });

        fullRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                paymentOption = getString(R.string.full_payment);
                partialRadio.setChecked(false);
                edtPartialPayment.setVisibility(View.GONE);
                button_payment.setText(getResources().getString(R.string.pay) + " " + SessionSave.getSession("currency_symbol", getActivity()) + " " + totalPrice);

            }
        });


        partialRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentOption = getString(R.string.patial_payment);
                edtPartialPayment.setVisibility(View.VISIBLE);
                fullRadio.setChecked(false);
                edtPartialPayment.setText(SessionSave.getSession("currency_symbol", getActivity()) + partial);
                button_payment.setText(getResources().getString(R.string.pay) + " "+ " " + edtPartialPayment.getText());


            }
        });

        noradio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noradio.setChecked(true);
                yesradio.setChecked(false);
                defaultCountry = 0;

                isProfileAddress(false);
                shippingAddressEdtLay.setVisibility(View.VISIBLE);
                mainTopLay.post(new Runnable() {
                    @Override
                    public void run() {
                        mainTopLay.scrollTo(0, shippingAddressLay.getBottom());
                    }
                });
            }
        });

        //edit text text watcher
        addCouponCodeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() > 0) {
                    applyCouponCodeBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    applyCouponCodeBtn.setEnabled(true);
                } else {
                    applyCouponCodeBtn.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    applyCouponCodeBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //apply promo code btn
        applyCouponCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String couponCode = addCouponCodeEt.getText().toString();
                if (!couponCode.isEmpty()) {

                    showProgress(true);
                    KeyBoardManager.closeKeyBoard(getActivity(), applyCouponCodeBtn);
                    //call api
                    UserPresenter userPresenter = new UserPresenter(PaymentContinueFragment.this);
                    userPresenter.applyCouponCode(couponCode, SessionSave.getSession("user_id", getActivity()));
                }
            }
        });

        spin_country.getBackground().setColorFilter(Color.parseColor("#ff2353"), PorterDuff.Mode.SRC_ATOP);
        spin_city.getBackground().setColorFilter(Color.parseColor("#ff2353"), PorterDuff.Mode.SRC_ATOP);

        if (bundle != null) {
            //setCountryCode();
            urlType = bundle.getString("urltype");
            if (urlType.equals("cart")) {

                if (bundle.getString("itemcount").equals("1")) {
                    no_of_items_txt.setText(getResources().getString(R.string.price) + " ( " + bundle.getString("itemcount") + " " + getResources().getString(R.string.items) + " ) ");
                } else {
                    no_of_items_txt.setText(getResources().getString(R.string.price) + " ( " + bundle.getString("itemcount") + " " + getResources().getString(R.string.items) + " ) ");
                }



                totalPrice = bundle.getString("itemprice");
                itemsPrice.setText("" + SessionSave.getSession("currency_symbol", getActivity()) + " " + (Double.valueOf(bundle.getString("itemprice")) - (Double.valueOf(bundle.getString("shipprice")) + Double.valueOf(bundle.getString("taxprice")))));
                shipping_charges.setText("" + SessionSave.getSession("currency_symbol", getActivity()) + " " + Double.valueOf(bundle.getString("shipprice")));
                total_charges.setText("" + SessionSave.getSession("currency_symbol", getActivity()) + " " + Double.valueOf(bundle.getString("itemprice")));

                if (partialRadio.isChecked()) {
                    button_payment.setText(getResources().getString(R.string.pay) + " " + SessionSave.getSession("currency_symbol", getActivity()) + " " + partial);
                } else {
                    button_payment.setText(getResources().getString(R.string.pay) + " " + SessionSave.getSession("currency_symbol", getActivity()) + " " + Double.valueOf(bundle.getString("itemprice")));

                }
                tax_charges.setText("" + SessionSave.getSession("currency_symbol", getActivity()) + " " + Double.valueOf(bundle.getString("taxprice")));
            } else if (urlType.equals("product")) {
                System.out.println("itemcount " + bundle.getString("itemcount") + " " + bundle.getString("itemprice") + " " + bundle.getString("shipprice"));
                if (bundle.getString("itemcount").equals("1")) {
                    no_of_items_txt.setText(getResources().getString(R.string.price) + " ( " + bundle.getString("itemcount") + " " + getResources().getString(R.string.items) + " ) ");
                } else {
                    no_of_items_txt.setText(getResources().getString(R.string.price) + " ( " + bundle.getString("itemcount") + " " + getResources().getString(R.string.items) + " ) ");
                }
                colorId = bundle.getString("colorId");
                sizeId = bundle.getString("sizeId");
                ProductId = bundle.getString("product_id");
                quanty = bundle.getString("itemcount");
                totalPrice = bundle.getString("itemprice");

//                totalPrice = ""+( Double.valueOf(bundle.getString("itemprice"))-Double.valueOf(bundle.getString("taxprice")));
                tax_charges.setText("" + SessionSave.getSession("currency_symbol", getActivity()) + " " + Double.valueOf(bundle.getString("taxprice")));
                itemsPrice.setText("" + SessionSave.getSession("currency_symbol", getActivity()) + " " + (Double.valueOf(bundle.getString("itemprice")) - (Double.valueOf(bundle.getString("shipprice")) + Double.valueOf(bundle.getString("taxprice")))));
                shipping_charges.setText("" + SessionSave.getSession("currency_symbol", getActivity()) + " " + Double.valueOf(bundle.getString("shipprice")));
                total_charges.setText("" + SessionSave.getSession("currency_symbol", getActivity()) + " " + Double.valueOf(bundle.getString("itemprice")));

                if (partialRadio.isChecked()) {
                    button_payment.setText(getResources().getString(R.string.pay) + " " + SessionSave.getSession("currency_symbol", getActivity()) + " " + edtPartialPayment.getText());
                } else {
                    button_payment.setText(getResources().getString(R.string.pay) + " " + SessionSave.getSession("currency_symbol", getActivity()) + " " + Double.valueOf(bundle.getString("itemprice")));
                }

            } else if (urlType.equals("deal")) {
                System.out.println("itemcount " + bundle.getString("itemcount") + " " + bundle.getString("itemprice") + " " + bundle.getString("shipprice"));
                if (bundle.getString("itemcount").equals("1")) {
                    no_of_items_txt.setText(getResources().getString(R.string.price) + " ( " + bundle.getString("itemcount") + " " + getResources().getString(R.string.items) + " ) ");
                } else {
                    no_of_items_txt.setText(getResources().getString(R.string.price) + " ( " + bundle.getString("itemcount") + " " + getResources().getString(R.string.items) + " ) ");
                }
                tax_charges.setText("" + SessionSave.getSession("currency_symbol", getActivity()) + " " + Double.valueOf(bundle.getString("taxprice")));
                itemsPrice.setText("" + SessionSave.getSession("currency_symbol", getActivity()) + " " + (Double.valueOf(bundle.getString("itemprice")) - (Double.valueOf(bundle.getString("shipprice")) + Double.valueOf(bundle.getString("taxprice")))));
                shipping_charges.setText("" + SessionSave.getSession("currency_symbol", getActivity()) + " " + Double.valueOf(bundle.getString("shipprice")));
                total_charges.setText("" + SessionSave.getSession("currency_symbol", getActivity()) + " " + Double.valueOf(bundle.getString("itemprice")));

                if (partialRadio.isChecked()) {
                    button_payment.setText(getResources().getString(R.string.pay) + " " + SessionSave.getSession("currency_symbol", getActivity()) + " " + edtPartialPayment.getText());
                } else {
                    button_payment.setText(getResources().getString(R.string.pay) + " " + SessionSave.getSession("currency_symbol", getActivity()) + " " + Double.valueOf(bundle.getString("itemprice")));
                }

                colorId = bundle.getString("colorId");
                sizeId = bundle.getString("sizeId");
                ProductId = bundle.getString("product_id");
                quanty = bundle.getString("itemcount");
                totalPrice = bundle.getString("itemprice");


            }

        }

        button_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (yesradio.isChecked() || noradio.isChecked()) {
                    ship_name = edtName.getText().toString();
                    ship_email = edtEmail.getText().toString();
                    ship_phone = edtNumber.getText().toString();
                    ship_address1 = edtbuildingName.getText().toString();
                    ship_address2 = edtLocalityName.getText().toString();
                    ship_state = edtState.getText().toString();
                    ship_postalcode = edtPincode.getText().toString();
                    edtEmail.setError(null);
                    edtName.setError(null);
                    edtNumber.setError(null);
                    edtbuildingName.setError(null);
                    edtLocalityName.setError(null);
                    edtState.setError(null);
                    edtPincode.setError(null);
                    boolean cancel = false;
                    View focusView = null;

                    if (edtName.getText().toString().trim().isEmpty()) {
                        edtName.setError("" + getResources().getString(R.string.please_enter_username));
                        focusView = edtName;
                        cancel = true;
                    } else if (edtEmail.getText().toString().trim().isEmpty()) {
                        edtEmail.setError("" + getResources().getString(R.string.please_enter_email));
                        focusView = edtEmail;
                        cancel = true;
                    } else if (!isValidEmail(edtEmail.getText().toString().trim())) {
                        edtEmail.setError("" + getResources().getString(R.string.please_enter_email_invaild));
                        focusView = edtEmail;
                        cancel = true;
                    } else if (edtNumber.getText().toString().trim().isEmpty()) {
                        edtNumber.setError("" + getResources().getString(R.string.please_enter_phone));
                        focusView = edtNumber;
                        cancel = true;
                    }
//                else if (edtbuildingName.getText().toString().trim().isEmpty()) {
//                    edtbuildingName.setError("" + getResources().getString(R.string.please_enter_building));
//                    focusView = edtbuildingName;
//                    cancel = true;
//                }
                    else if (edtLocalityName.getText().toString().trim().isEmpty()) {
                        edtLocalityName.setError("" + getResources().getString(R.string.please_enter_street));
                        focusView = edtLocalityName;
                        cancel = true;
                    }
//                    else if (ship_country_id.equals("")) {
//                        cancel = true;
//                        ((MainActivity) getActivity()).ShowToast(getActivity(), "No Country found ");
//                    } else if (ship_city_id.equals("")) {
//                        cancel = true;
//                        ((MainActivity) getActivity()).ShowToast(getActivity(), "No city found ");
//                    }
                    else if (edtState.getText().toString().trim().isEmpty()) {
                        edtState.setError("" + getResources().getString(R.string.please_enter_state));
                        focusView = edtState;
                        cancel = true;
                    } else if (edtPincode.getText().toString().trim().isEmpty()) {
                        edtPincode.setError("" + getResources().getString(R.string.please_enter_pincode));
                        focusView = edtPincode;
                        cancel = true;
                    }
//

                    if (cancel) {
                        focusView.requestFocus();
                    } else {
                        if (NetworkStatus.isOnline(getActivity())) {

                            /*
                                loading webview
                             */


                            String fullPayment = BuildConfig.BASE_URL + "/app-pay?user_id=" + SessionSave.getSession("user_id", getActivity()) + "&email=" +
                                    edtEmail.getText().toString() + "&name=" + edtName.getText().toString() + "&address=" +
                                    edtLocalityName.getText().toString() + "&city=" +
                                    edtState.getText().toString() + "&province=" + edtState.getText().toString() + "&postalcode=" + edtPincode.getText().toString() + "&phone=" +
                                    edtNumber.getText().toString() + "&coupon_code=" + couponCode;

//                                for partial payment
                            String partialPayment = BuildConfig.BASE_URL + "/app-pay?user_id=" + SessionSave.getSession("user_id", getActivity()) + "&email=" +
                                    edtEmail.getText().toString() + "&name=" + edtName.getText().toString() + "&address=" +
                                    edtLocalityName.getText().toString() + "&city=" +
                                    edtState.getText().toString() + "&province=" + edtState.getText().toString() + "&postalcode=" + edtPincode.getText().toString() + "&phone=" +
                                    edtNumber.getText().toString() + "&coupon_code=" + couponCode+"&payment_type="+paymentOption+"&partial_amount="+ partial;

                            if (partialRadio.isChecked()){
// .............................partial payment
                                Intent payment_intent = new Intent(getContext(), PaymentWebViewActivity.class);
                                payment_intent.putExtra(PaymentWebViewActivity.WEBVIEW_URL, partialPayment);
                                startActivity(payment_intent);

                                System.out.println("partial_payment"+partialPayment);
                                System.out.println("partial_payment"+partial);
                            }else {
// .............................Normal Payment......................................
                                Intent payment_intent = new Intent(getContext(), PaymentWebViewActivity.class);
                                payment_intent.putExtra(PaymentWebViewActivity.WEBVIEW_URL, fullPayment);
                                startActivity(payment_intent);

                                System.out.println("partial_payment"+fullPayment);

                            }


//                            if (!ispaymenType) {
//
//                                if (urlType != null && urlType.equals("cart")) {
//                                    button_payment.setVisibility(View.GONE);
//                                    System.out.println("ship_name  ship_email" + ship_name + "  - " + ship_email + "  - " + ship_phone + "  - " + ship_address1 + "  - " + ship_address2 + "  - " + ship_country_id + "  - " + ship_city_id + "  - " + ship_state + "  - " + ship_postalcode);
//                                    showProgress(true);
//                                    UserPresenter userPresenter = new UserPresenter(PaymentContinueFragment.this);
//                                    userPresenter.paymentcodPage(SessionSave.getSession("user_id", getActivity()), ship_name, ship_email, ship_phone, ship_address1, ship_address2, ship_country_id, ship_city_id, ship_state, ship_postalcode);
//
//                                } else if (urlType != null && urlType.equals("product")) {
//                                    button_payment.setVisibility(View.GONE);
//                                    System.out.println("ship_name  ship_email" + ship_name + "  - " + ship_email + "  - " + ship_phone + "  - " + ship_address1 + "  - " + ship_address2 + "  - " + ship_country_id + "  - " + ship_city_id + "  - " + ship_state + "  - " + ship_postalcode);
//                                    showProgress(true);
//                                    UserPresenter userPresenter = new UserPresenter(PaymentContinueFragment.this);
//                                    userPresenter.paymentbuynowPage(SessionSave.getSession("user_id", getActivity()), ProductId, sizeId, colorId, quanty, ship_name, ship_email, ship_phone, ship_address1, ship_address2, ship_country_id, ship_city_id, ship_state, ship_postalcode);
//
//                                } else if (urlType != null && urlType.equals("deal")) {
//                                    button_payment.setVisibility(View.GONE);
//                                    System.out.println("ship_name  ship_email" + ship_name + "  - " + ship_email + "  - " + ship_phone + "  - " + ship_address1 + "  - " + ship_address2 + "  - " + ship_country_id + "  - " + ship_city_id + "  - " + ship_state + "  - " + ship_postalcode);
//                                    showProgress(true);
//                                    UserPresenter userPresenter = new UserPresenter(PaymentContinueFragment.this);
//                                    userPresenter.dealsbuynowPage(SessionSave.getSession("user_id", getActivity()), ProductId, quanty, ship_name, ship_email, ship_phone, ship_address1, ship_address2, ship_country_id, ship_city_id, ship_state, ship_postalcode);
//                                }
//                            } else {
//                                button_payment.setVisibility(View.GONE);
//                                showProgress(true);
//                                Intent service = new Intent(getActivity(), PayPalAccessTokenService.class);
//                                getActivity().startService(service);
//
//                                UserPresenter userPresenter = new UserPresenter(PaymentContinueFragment.this);
//                                // Based on country set in admin panel the value is fetched in json at HomeScreenFragment.class
//                                userPresenter.getCurrencyValues(SessionSave.getSession("currency_code", getActivity()), totalPrice);
//                            }
                        } else {
                            ((MainActivity) getActivity()).ShowToast(getActivity(), "" + getResources().getString(R.string.internet_connection));
                        }

                    }


                } else {
                    ((MainActivity) getActivity()).ShowToast(getActivity(), "" + getString(R.string.select_billing_address));
                }

            }
        });

    }


    private void isProfileAddress(boolean isProfile) {
        try {

            if (isProfile) {
                if (!(SessionSave.getSession("shipping_details", getActivity()).equals(""))) {
                    System.out.println("ship_name  ship_email  1" + SessionSave.getSession("shipping_details", getActivity()));
                    mCountryList.clear();
                    defaultCountry = 0;
                    JSONObject json = new JSONObject(SessionSave.getSession("shipping_details", getActivity()));
                    if (json.getString("status").equalsIgnoreCase("200")) {
                        JSONArray shipping_details = json.getJSONArray("shipping_details");
                        if (shipping_details.length() > 0) {
                            ship_name = shipping_details.getJSONObject(0).getString("ship_name").trim();
                            ship_email = shipping_details.getJSONObject(0).getString("ship_email").trim();
                            ship_phone = shipping_details.getJSONObject(0).getString("ship_phone").trim();
                            ship_address1 = shipping_details.getJSONObject(0).getString("ship_address1").trim();
                            ship_address2 = shipping_details.getJSONObject(0).getString("ship_address2").trim();
                            ship_country_id = shipping_details.getJSONObject(0).getString("ship_country_id").trim();
                            ship_city_id = shipping_details.getJSONObject(0).getString("ship_city_id").trim();
                            ship_state = shipping_details.getJSONObject(0).getString("ship_state").trim();
                            ship_postalcode = shipping_details.getJSONObject(0).getString("ship_postalcode").trim();

                            edtName.setText(ship_name);
                            edtEmail.setText(ship_email);
                            edtNumber.setText(ship_phone);
                            edtbuildingName.setText(ship_address1);
                            edtLocalityName.setText(ship_address2);
                            edtState.setText(ship_state);
                            edtPincode.setText(ship_postalcode);

                            try {
                                if (!SessionSave.getSession("countrylist", getActivity()).equals("")) {

                                    JSONObject jsonObject = new JSONObject(SessionSave.getSession("countrylist", getActivity()));


                                    JSONArray country_details = jsonObject.getJSONArray("country_details");


                                    if (country_details.length() > 0) {

                                        for (int i = 0; i < country_details.length(); i++) {

                                            defaultCity = 0;
                                            String country_id = country_details.getJSONObject(i).getString("country_id");
                                            String country_name = country_details.getJSONObject(i).getString("country_name");

                                            JSONArray city_details = country_details.getJSONObject(i).getJSONArray("city_details");
                                            mCityList = new ArrayList<>();

                                            if (city_details.length() > 0) {
                                                for (int j = 0; j < city_details.length(); j++) {
                                                    String city_id = city_details.getJSONObject(j).getString("city_id");
                                                    String city_name = city_details.getJSONObject(j).getString("city_name");
                                                    String city_country_id = city_details.getJSONObject(j).getString("city_country_id");

                                                    if (defaultCity == 0) {
                                                        defaultCity++;
                                                        CityListData cityListData = new CityListData("", "" + getResources().getString(R.string.city), "");
                                                        mCityList.add(cityListData);
                                                    }
                                                    CityListData cityListData = new CityListData(city_id, city_name, city_country_id);
                                                    mCityList.add(cityListData);
                                                }
                                            } else {
                                                CityListData cityListData = new CityListData("", "" + getResources().getString(R.string.no_city_found), "");
                                                mCityList.add(cityListData);
                                            }
                                            if (defaultCountry == 0) {
                                                defaultCountry++;
                                                CountryListData countryListData = new CountryListData("", "" + getResources().getString(R.string.country), mCityList);
                                                mCountryList.add(countryListData);
                                            }

                                            CountryListData countryListData = new CountryListData(country_id, country_name, mCityList);
                                            mCountryList.add(countryListData);

                                        }
                                    }

                                }

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            defaultCountry = 0;
                            CoutryCityAdapter adapter = new CoutryCityAdapter(getActivity(), mCountryList, defaultTextForCountrySpinner);
                            spin_country.setAdapter(adapter);


                            for (int i = 0; i < mCountryList.size(); i++) {


                                if (mCountryList.get(i).getCountry_name().equalsIgnoreCase(shipping_details.getJSONObject(0).getString("ship_country_name").trim())) {
                                    ship_country_id = mCountryList.get(i).getCountry_id();
                                    spin_country.setSelection(i);
                                    mCityList = mCountryList.get(i).getCityListDataArrayList();
                                    System.out.println("mCityList  size---- > " + mCityList.size());
                                    CountryCityBaseAdapter adapter1 = new CountryCityBaseAdapter(getActivity(), mCityList);
                                    // Set adapter to spinner
                                    spin_city.setAdapter(adapter1);
                                    break;
                                } else {
                                    ship_country_id = "";
                                    mEmptyList.clear();
                                    CityListData cityListData = new CityListData("", "City", "");
                                    mEmptyList.add(cityListData);
                                    CountryCityBaseAdapter countryCityBaseAdapter = new CountryCityBaseAdapter(getActivity(), mEmptyList);
                                    spin_city.setAdapter(countryCityBaseAdapter);
                                }
                            }


                            for (int i = 0; i < mCityList.size(); i++) {

                                System.out.println("mCityList ---- > " + mCityList.get(i).getCity_name()
                                        + " " + shipping_details.getJSONObject(0).getString("ship_city_name"));

                                if (mCityList.get(i).getCity_name().equalsIgnoreCase(shipping_details.getJSONObject(0).getString("ship_city_name").trim())) {
                                    System.out.println("mCityList ---- >  i " + i);
                                    ship_city_id = mCityList.get(i).getCity_id();
                                    spin_city.setSelection(i);
                                    break;
                                }
                            }


                            // Listener called when spinner item selected
                            spin_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                                    if (!mCountryList.get(position).getCountry_id().equalsIgnoreCase("")) {
                                        if (defaultCountry > 0) {
                                            // Create custom adapter object ( see below CustomAdapter.java )
                                            ship_country_id = mCountryList.get(position).getCountry_id();
                                            System.out.println("mCountryList --- > Final " + mCountryList.get(position).getCityListDataArrayList().size());
                                            mCityList = mCountryList.get(position).getCityListDataArrayList();
                                            CountryCityBaseAdapter adapter = new CountryCityBaseAdapter(getActivity(), mCityList);
                                            // Set adapter to spinner
                                            spin_city.setAdapter(adapter);
                                        }
                                    } else {
                                        ship_country_id = "";
                                        mEmptyList.clear();
                                        CityListData cityListData = new CityListData("", "City", "");
                                        mEmptyList.add(cityListData);
                                        CountryCityBaseAdapter countryCityBaseAdapter = new CountryCityBaseAdapter(getActivity(), mEmptyList);
                                        spin_city.setAdapter(countryCityBaseAdapter);
                                    }

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parentView) {
                                    // your code here
                                }

                            });
                            spin_country.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    defaultCountry++;
                                    return false;
                                }
                            });

                            // Listener called when spinner item selected
                            spin_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                                    System.out.println("mCountryList " + mCityList.size());


                                    if (mCityList.size() > 0)
                                        ship_city_id = mCityList.get(position).getCity_id();
                                    else if (mEmptyList.size() > 0)
                                        ship_city_id = mEmptyList.get(position).getCity_id();
                                    else
                                        ship_city_id = "";
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parentView) {
                                    // your code here
                                }

                            });

                        } else {
                            edtName.setText("");
                            edtEmail.setText("");
                            edtNumber.setText("");
                            edtbuildingName.setText("");
                            edtLocalityName.setText("");
                            edtState.setText("");
                            edtPincode.setText("");
                            setCountryCode();
                        }
                    } else {
                        edtName.setText("");
                        edtEmail.setText("");
                        edtNumber.setText("");
                        edtbuildingName.setText("");
                        edtLocalityName.setText("");
                        edtState.setText("");
                        edtPincode.setText("");
                        setCountryCode();
                    }

                } else if (!(SessionSave.getSession("user_details", getActivity()).equals(""))) {

                    JSONObject json = new JSONObject(SessionSave.getSession("user_details", getActivity()));
                    JSONObject shipping_details = json.getJSONObject("user_details");
                    ship_name = shipping_details.getString("user_name").trim();
                    ship_email = shipping_details.getString("user_email").trim();
                    ship_phone = shipping_details.getString("user_phone").trim();
                    ship_address1 = shipping_details.getString("user_address1").trim();
                    ship_address2 = shipping_details.getString("user_address2").trim();
                    ship_country_id = "";
                    ship_city_id = "";
                    ship_state = "";
                    ship_postalcode = "";

                    edtName.setText(ship_name);
                    edtEmail.setText(ship_email);
                    edtNumber.setText(ship_phone);
                    edtbuildingName.setText(ship_address1);
                    edtLocalityName.setText(ship_address2);
                    edtState.setText(ship_state);
                    edtPincode.setText(ship_postalcode);


                    // Listener called when spinner item selected
                    spin_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                            // Create custom adapter object ( see below CustomAdapter.java )
                            ship_country_id = mCountryList.get(position).getCountry_id();
                            System.out.println("mCountryList " + mCountryList.get(position).getCityListDataArrayList().size());
                            mCityList = mCountryList.get(position).getCityListDataArrayList();
                            CountryCityBaseAdapter adapter = new CountryCityBaseAdapter(getActivity(), mCityList);
                            // Set adapter to spinner
                            spin_city.setAdapter(adapter);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // your code here
                        }

                    });

                    // Listener called when spinner item selected
                    spin_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                            System.out.println("mCountryList " + mCityList.size());
                            ship_city_id = mCityList.get(position).getCity_id();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // your code here
                        }

                    });

                }
            } else {
                edtName.setText("");
                edtEmail.setText("");
                edtNumber.setText("");
                edtbuildingName.setText("");
                edtLocalityName.setText("");
                edtState.setText("");
                edtPincode.setText("");
                setCountryCode();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
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

            mainTopLay.setVisibility(show ? View.GONE : View.VISIBLE);
            mainTopLay.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mainTopLay.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mainTopLay.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void applyFont(View view) {
        ApplyFont.applyMediumFont(getActivity(), view.findViewById(R.id.mainTopLay));
        ApplyFont.applyFont(getActivity(), view.findViewById(R.id.itemsPrice));
        ApplyFont.applyFont(getActivity(), view.findViewById(R.id.no_of_items_txt));
        ApplyFont.applyFont(getActivity(), view.findViewById(R.id.shipping_charges_txt));
        ApplyFont.applyFont(getActivity(), view.findViewById(R.id.shipping_charges));
        ApplyFont.applyFont(getActivity(), view.findViewById(R.id.paypaltxt));
        ApplyFont.applyFont(getActivity(), view.findViewById(R.id.cashontxt));
        ApplyFont.applyFont(getActivity(), view.findViewById(R.id.cashontxt));
        ApplyFont.applyMediumFont(getActivity(), view.findViewById(R.id.pricingdetails));
        ApplyFont.applyBold(getActivity(), view.findViewById(R.id.total_charges_txt));
        ApplyFont.applyBold(getActivity(), view.findViewById(R.id.total_charges));
        ApplyFont.applyFont(getActivity(), view.findViewById(R.id.shippingAddressEdtLay));
    }


    private PayPalPayment getThingToBuy(String paymentIntent, String Convert_to_Currency_Code, String amount) {
        return new PayPalPayment(new BigDecimal(amount), Convert_to_Currency_Code, "" + urlType, paymentIntent);
    }

    public void paymentContinue(JsonObject mJsonObject) {
        showProgress(false);

        try {
            if (mJsonObject != null) {
                button_payment.setVisibility(View.VISIBLE);

                JSONObject json = new JSONObject(mJsonObject.toString());
                if (json.getString("status").equals("200")) {
                    Intent intent = new Intent(getActivity(), PaymentCompletedScreen.class);
                    intent.putExtra("result", mJsonObject.toString());
                    intent.putExtra("totalPrice", totalPrice);
                    getActivity().startActivity(intent);
                    getActivity().finish();

                } else {
                    ((MainActivity) getActivity()).showSnackBar(mainTopLay, "" + "Sorry unable to reach server!!! ");
                }
            } else {
                button_payment.setVisibility(View.VISIBLE);
                ((MainActivity) getActivity()).showSnackBar(mainTopLay, "" + getString(R.string.unable_to_reach_server));

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void CouponCodeResponse(JsonObject mJsonObject) {

        showProgress(false);

        try {
            if (mJsonObject != null) {

                JSONObject json = new JSONObject(mJsonObject.toString());
                if (json.getString("status").equals("200")) {

                    int discountPrice = json.getJSONObject("coupon_details").optInt("coupon_amount");
                    couponCode = json.getJSONObject("coupon_details").optString("coupon_code");

                    if (discountPrice > 0) {

                        //  itemOriginalPrice.setVisibility(View.VISIBLE);
                        couponAmountLay.setVisibility(View.VISIBLE);

                        tax_charges.setText("" + SessionSave.getSession("currency_symbol", getActivity()) + " " + discountPrice);

                       double newPrice = (Double.valueOf(totalPrice)) - discountPrice;
                        totalPrice=newPrice+"";

                        total_charges.setText("" + SessionSave.getSession("currency_symbol", getActivity()) + " " + newPrice);

                        /*
                        handle carefully. Full text wasn't updating
                         */

                        if(fullRadio.isChecked()) {
                            button_payment.setText(getResources().getString(R.string.pay) + " " + SessionSave.getSession("currency_symbol", getActivity()) + " " + totalPrice);
                        }


                    }
                }
                promoCodeStatusTv.setText(json.optString("message"));
                promoCodeStatusTv.setVisibility(View.VISIBLE);
            } else {
                // button_payment.setVisibility(View.VISIBLE);
                promoCodeStatusTv.setVisibility(View.GONE);
                ((MainActivity) getActivity()).showSnackBar(mainTopLay, "" + getString(R.string.unable_to_reach_server));

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i("DownloadLink", confirm.toJSONObject().toString());
                        Log.i("DownloadLink", confirm.getEnvironment().toString());
                        // Log.i(TAG, confirm.getPayment());
                        Log.i("DownloadLink", confirm.getPayment().toJSONObject().toString());
                        /**
                         *  TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
                         * or consent completion.
                         * See https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                         * for more details.
                         *
                         * For sample mobile backend interactions, see
                         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
                         */

                        JSONObject jsonObject = new JSONObject(confirm.toJSONObject().toString());
                        transaction_id = jsonObject.getJSONObject("response").getString("id");
//                        new DownloadLink().execute();
                        showProgress(true);


                        try {


                            token_id = SessionSave.getSession("token_id", getActivity());

                            if (token_id.equals("")) {
                                showProgress(false);
                                ((MainActivity) getActivity()).showSnackBar(mainTopLay, "" + "Sorry unable to reach server!!! ");
                            } else {
                                JSONObject json = new JSONObject(SessionSave.getSession("paypal_user_info", getActivity()));
                                payer_email = "" + ship_email;
                                payer_id = json.getString("user_id");
                                payer_name = "" + ship_name;

                                System.out.println("ship_city_id uuuuuuuu ---- > " + ship_country_id
                                        + " " + ship_city_id);
                                if (urlType != null && urlType.equals("cart") && transaction_id != null && token_id != null) {


                                    button_payment.setVisibility(View.GONE);
                                    showProgress(true);
                                    UserPresenter userPresenter = new UserPresenter(PaymentContinueFragment.this);
                                    userPresenter.paymentPaypalcodPage(SessionSave.getSession("user_id", getActivity()), ship_name, ship_email, ship_phone, ship_address1, ship_address2, ship_country_id, ship_city_id, ship_state, ship_postalcode, transaction_id, token_id, payer_email, payer_id, payer_name);

                                } else if (urlType != null && urlType.equals("product") && transaction_id != null && token_id != null) {
                                    button_payment.setVisibility(View.GONE);
                                    showProgress(true);
                                    UserPresenter userPresenter = new UserPresenter(PaymentContinueFragment.this);
                                    userPresenter.paymentPaypalbuynowPage(SessionSave.getSession("user_id", getActivity()), ProductId, sizeId, colorId, quanty, ship_name, ship_email, ship_phone, ship_address1, ship_address2, ship_country_id, ship_city_id, ship_state, ship_postalcode, transaction_id, token_id, payer_email, payer_id, payer_name);

                                } else if (urlType != null && urlType.equals("deal") && transaction_id != null && token_id != null) {
                                    button_payment.setVisibility(View.GONE);
                                    showProgress(true);
                                    UserPresenter userPresenter = new UserPresenter(PaymentContinueFragment.this);
                                    userPresenter.dealsPayPalbuynowPage(SessionSave.getSession("user_id", getActivity()), ProductId, quanty, ship_name, ship_email, ship_phone, ship_address1, ship_address2, ship_country_id, ship_city_id, ship_state, ship_postalcode, transaction_id, token_id, payer_email, payer_id, payer_name);

                                }
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } catch (Exception e) {
                        Log.e("DownloadLink", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("DownloadLink", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        "DownloadLink",
                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    public void getcurrencyValues(JsonObject jsonObject) {
        showProgress(false);
        try {
            if (jsonObject != null) {
                JSONObject json = new JSONObject(jsonObject.toString());
                if (json.getString("status").equals("200")) {

                    PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE, json.getString("Convert_to_Currency_Code"), json.getString("amount"));

                    Intent intent = new Intent(getActivity(), PaymentActivity.class);
                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
                    getActivity().startActivityForResult(intent, REQUEST_CODE_PAYMENT);

                }
            } else {
                button_payment.setVisibility(View.VISIBLE);
                ((MainActivity) getActivity()).showSnackBar(mainTopLay, "" + "Sorry unable to reach server!!! ");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    class DownloadLink extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub


            String response = "";
            try {

                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;

                try {
                    URL url = new URL("https://api.sandbox.paypal.com/v1/payments/payment/" + transaction_id);
                    // Create the request to OpenWeatherMap, and open the connection
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setRequestProperty("Authorization", "Bearer " + SessionSave.getSession("token_id", getActivity()));
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                    // Read the input stream into a String
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        // Nothing to do.
                        response = "";
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {
                        // Stream was empty.  No point in parsing.
                        response = "";
                    }
                    response = buffer.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("PlaceholderFragment", "Error ", e);
                    // If the code didn't successfully get the weather data, there's no point in attemping
                    // to parse it.
                    response = "";

                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            Log.e("PlaceholderFragment", "Error closing stream", e);
                        }
                    }
                }
//
                System.out.println("response " + response);

            } catch (Exception e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            }
            //Do Your stuff here..
            return response;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            //getaccesstokens();
            System.out.println("response " + aVoid);

            try {
                JSONObject jsonObject = new JSONObject(aVoid);
                token_id = jsonObject.getString("access_token");

                System.out.println("response token_id " + token_id);


            } catch (Exception ex) {
                ex.printStackTrace();
            }

//
        }
    }

    private void setCountryCode() {
        try {

            defaultCountry = 0;
            if (!SessionSave.getSession("countrylist", getActivity()).equals("")) {

                mCountryList.clear();
                JSONObject jsonObject = new JSONObject(SessionSave.getSession("countrylist", getActivity()));


                JSONArray country_details = jsonObject.getJSONArray("country_details");


                if (country_details.length() > 0) {

                    for (int i = 0; i < country_details.length(); i++) {
                        defaultCity = 0;

                        String country_id = country_details.getJSONObject(i).getString("country_id");
                        String country_name = country_details.getJSONObject(i).getString("country_name");

                        JSONArray city_details = country_details.getJSONObject(i).getJSONArray("city_details");
                        mCityList = new ArrayList<>();

                        if (city_details.length() > 0) {
                            for (int j = 0; j < city_details.length(); j++) {
                                String city_id = city_details.getJSONObject(j).getString("city_id");
                                String city_name = city_details.getJSONObject(j).getString("city_name");
                                String city_country_id = city_details.getJSONObject(j).getString("city_country_id");

                                if (defaultCity == 0) {
                                    defaultCity++;
                                    CityListData cityListData = new CityListData("", "" + getResources().getString(R.string.city), "");
                                    mCityList.add(cityListData);
                                }
                                CityListData cityListData = new CityListData(city_id, city_name, city_country_id);
                                mCityList.add(cityListData);
                            }
                        } else {
                            CityListData cityListData = new CityListData("", "" + getResources().getString(R.string.no_city_found), "");
                            mCityList.add(cityListData);
                        }
                        if (defaultCountry == 0) {
                            defaultCountry++;
                            CountryListData countryListData = new CountryListData("", "" + getResources().getString(R.string.country), mCityList);
                            mCountryList.add(countryListData);
                        }

                        CountryListData countryListData = new CountryListData(country_id, country_name, mCityList);
                        mCountryList.add(countryListData);

                    }


                    // Create custom adapter object ( see below CustomAdapter.java )
                    CoutryCityAdapter adapter = new CoutryCityAdapter(getActivity(), mCountryList, defaultTextForCountrySpinner);
                    spin_country.setAdapter(adapter);

                    spin_country.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            defaultCountry++;
                            return false;
                        }
                    });
                    // Listener called when spinner item selected
                    spin_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {

                            // Create custom adapter object ( see below CustomAdapter.java )

                            if (!mCountryList.get(position).getCountry_id().equalsIgnoreCase("")) {

                                ship_country_id = mCountryList.get(position).getCountry_id();
                                System.out.println("mCountryList  open " + mCountryList.get(position).getCityListDataArrayList().size());
                                mCityList = mCountryList.get(position).getCityListDataArrayList();
                                CountryCityBaseAdapter adapter = new CountryCityBaseAdapter(getActivity(), mCityList);
                                spin_city.setAdapter(adapter);
                            } else {
                                ship_country_id = "";
                                mEmptyList.clear();
                                CityListData cityListData = new CityListData("", "City", "");
                                mEmptyList.add(cityListData);
                                CountryCityBaseAdapter adapter = new CountryCityBaseAdapter(getActivity(), mEmptyList);
                                // Set adapter to spinner
                                spin_city.setAdapter(adapter);
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // your code here

                        }

                    });

                    // Listener called when spinner item selected
                    spin_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                            if (mCityList.size() > 0)
                                ship_city_id = mCityList.get(position).getCity_id();
                            else if (mEmptyList.size() > 0)
                                ship_city_id = mEmptyList.get(position).getCity_id();
                            else
                                ship_city_id = "";
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // your code here
                        }

                    });
                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
