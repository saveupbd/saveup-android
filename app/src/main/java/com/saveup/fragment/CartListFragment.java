package com.saveup.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.saveup.activity.MainActivity;
import com.saveup.adapter.CartListItemAdater;
import com.saveup.android.R;
import com.saveup.model.CartData;
import com.saveup.presenter.UserPresenter;
import com.saveup.utils.SessionSave;
import com.saveup.views.RippleView;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author AAPBD on 16,April,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.adapter
 */


public class CartListFragment extends Fragment {

    private View progressBar, cartItemLay, noDataLay, nestedScrollView, subprogressBarLay;
    private RecyclerView cartList;
    private TextView no_of_items, shipping_charges, total_charges, tax_charges;
    private ArrayList<CartData> cartDataArrayList = new ArrayList<>();
    private CartListItemAdater homeCatergoryAdap;
    private String total_shopping_amount = "", total_tax_amount = "",checkPartial;
    private String total_amount = "";
    private RippleView button_continue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.cartlistfragment, container, false);

        progressBar = view.findViewById(R.id.mainprogressBarLay);
        cartItemLay = view.findViewById(R.id.cartItemLay);
        noDataLay = view.findViewById(R.id.noDataLay);
        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        subprogressBarLay = view.findViewById(R.id.subprogressBarLay);
        no_of_items = view.findViewById(R.id.no_of_items);
        shipping_charges = view.findViewById(R.id.shipping_charges);
        tax_charges = view.findViewById(R.id.tax_charges);
        total_charges = view.findViewById(R.id.total_charges);
        button_continue = view.findViewById(R.id.button_continue);
        button_continue.setVisibility(View.GONE);
        cartList = view.findViewById(R.id.cartList);
        cartList.setHasFixedSize(true);
        cartList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        cartList.setNestedScrollingEnabled(false);
        System.out.println("onBackPressed  " + "onCreateView" + "onCreateView");
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

        backViewVisible();

        try {
            showProgress(true);
            UserPresenter userPresenter = new UserPresenter(CartListFragment.this);
            userPresenter.cartListPage(SessionSave.getSession("user_id", getActivity()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity) getActivity()).mLinearView3.setVisibility(View.GONE);
                Bundle bundle = new Bundle();
                bundle.putString("itemcount", SessionSave.getSession("cartCount", getActivity()));
                bundle.putString("itemprice", total_amount);
                bundle.putString("shipprice", total_shopping_amount);
                bundle.putString("taxprice", total_tax_amount);
                bundle.putString("urltype", "cart");
                bundle.putString("urltype", "cart");


                System.out.println("itemcount " + SessionSave.getSession("cartCount", getActivity()) + " " + total_amount + " " + total_shopping_amount);
                PaymentContinueFragment fragment2 = new PaymentContinueFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, fragment2);
                fragment2.setArguments(bundle);
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();

            }
        });


    }

    public void backViewVisible() {
        ((MainActivity) getActivity()).searchIcon.setVisible(false);
        final MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.search_layout.setVisibility(View.GONE);
        mainActivity.menu_Name.setVisible(false);
        mainActivity.toggle.setDrawerIndicatorEnabled(false);
        mainActivity.toolbar.setVisibility(View.VISIBLE);
        mainActivity.disableDrawer(true);
       /* if (mainActivity.cartItem != null) {
            mainActivity.cartItem.setVisible(false);
            mainActivity.cartIconempty.setVisible(false);
        }*/
        mainActivity.toolbar.setTitle("");
        mainActivity.toolbar.setLogo(R.drawable.transpent_logo);  //R.drawable.transpent_logo
        mainActivity.counterBackgroundLay.setVisibility(View.GONE);
        mainActivity.mLinearView3.setVisibility(View.VISIBLE);
        final Drawable upArrow = getResources().getDrawable(R.drawable.back_icon);
        mainActivity.toggle.setHomeAsUpIndicator(upArrow);
        mainActivity.toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.removeFragment();
            }
        });
    }

    public void cartListData(JsonObject mJsonObject) {

        try {

            if (mJsonObject != null) {


                showProgress(false);

                JSONObject json = new JSONObject(mJsonObject.toString());
                if (json.getString("status").equals("200")) {
                    cartItemLay.setVisibility(View.VISIBLE);
                    noDataLay.setVisibility(View.GONE);
                    button_continue.setVisibility(View.VISIBLE);
                    ((MainActivity) getActivity()).counterBackgroundLay.setVisibility(View.VISIBLE);
                    nestedScrollView.setVisibility(View.VISIBLE);

                    JSONArray product_review = json.getJSONArray("cart_details");

                    SessionSave.saveSession("cartCount", "" + json.getJSONArray("cart_details").length(), getActivity());
                    ((MainActivity) getActivity()).coutTxt.setText(SessionSave.getSession("cartCount", getActivity()));
                    //     ((MainActivity) getActivity()).setBadgeCount(getActivity(), SessionSave.getSession("cartCount", getActivity()));
                    no_of_items.setText("" + SessionSave.getSession("cartCount", getActivity()));
                    total_shopping_amount = json.getString("cart_grand_shipping");
                    total_tax_amount = json.getString("cart_grand_tax");
                    total_amount = json.getString("cart_grand_total");
                    tax_charges.setText("" + SessionSave.getSession("currency_symbol", getActivity()) + " " + json.getString("cart_grand_tax"));
                    shipping_charges.setText("" + SessionSave.getSession("currency_symbol", getActivity()) + " " + json.getString("cart_grand_shipping"));
                    total_charges.setText("" + SessionSave.getSession("currency_symbol", getActivity()) + " " + json.getString("cart_grand_total"));

                SessionSave.saveSession("partial",json.getString("total_commission"),getContext());

                    System.out.println("partial_current>>>"+json.getString("total_commission"));

//                    checkPartial = json.getString("partial_payment");
//
//                    System.out.println("checkign_partial"+checkPartial);
//
//                    Log.d("checkign_partial",checkPartial+"");

                    if (product_review.length() > 0) {
                        for (int i = 0; i < product_review.length(); i++) {
                            String cart_id = product_review.getJSONObject(i).getString("cart_id");
                            String cart_product_id = product_review.getJSONObject(i).getString("cart_product_id");
                            String cart_user_id = product_review.getJSONObject(i).getString("cart_user_id");
                            String cart_title = product_review.getJSONObject(i).getString("cart_title");
                            String cart_quantity = product_review.getJSONObject(i).getString("cart_quantity");
                            String cart_image = product_review.getJSONObject(i).getString("cart_image");
                            String cart_price = product_review.getJSONObject(i).getString("cart_price");
                            String cart_total = product_review.getJSONObject(i).getString("cart_total");
                            String product_purchase_qty = product_review.getJSONObject(i).getString("product_purchase_qty");
                            String product_available_qty = product_review.getJSONObject(i).getString("product_available_qty");
                            String cart_shipping_price = product_review.getJSONObject(i).getString("cart_shipping_price");
                            String cart_tax_price = product_review.getJSONObject(i).getString("cart_tax_price");
                            cart_shipping_price = "" + (Double.valueOf(cart_shipping_price) / Integer.valueOf(cart_quantity));

                            JSONArray cart_size_details = product_review.getJSONObject(i).getJSONArray("cart_size_details");
                            String product_size_id = "", product_color_id = "";
                            if (cart_size_details.length() > 0) {

                                product_size_id = cart_size_details.getJSONObject(0).getString("product_size_id");
                            }
                            JSONArray cart_color_details = product_review.getJSONObject(i).getJSONArray("cart_color_details");
                            if (cart_color_details.length() > 0) {

                                product_color_id = cart_color_details.getJSONObject(0).getString("product_color_id");
                            }


                            CartData dealsData = new CartData(cart_id, cart_product_id, cart_user_id, cart_title, cart_quantity, cart_image, cart_price, cart_total,
                                    product_purchase_qty, product_available_qty, cart_shipping_price, product_size_id, product_color_id, cart_tax_price);
                            cartDataArrayList.add(dealsData);
                        }

                        System.out.println("most_popular_product " + cartDataArrayList.size());
                        homeCatergoryAdap = new CartListItemAdater(getActivity(), cartDataArrayList, CartListFragment.this);
                        cartList.setAdapter(homeCatergoryAdap);
                        cartList.setVisibility(View.VISIBLE);


                    } else {
                        SessionSave.saveSession("cartCount", "", getActivity());
                        // ((MainActivity) getActivity()).setBadgeCount(getActivity(), SessionSave.getSession("cartCount", getActivity()));
                        cartList.setVisibility(View.GONE);
                        cartItemLay.setVisibility(View.GONE);
                        nestedScrollView.setVisibility(View.GONE);
                        noDataLay.setVisibility(View.VISIBLE);
                        button_continue.setVisibility(View.GONE);

                    }
                } else {
                    SessionSave.saveSession("cartCount", "", getActivity());
                    cartItemLay.setVisibility(View.GONE);
                    nestedScrollView.setVisibility(View.GONE);
                    noDataLay.setVisibility(View.VISIBLE);
                    button_continue.setVisibility(View.GONE);

                }

            } else {
                button_continue.setVisibility(View.GONE);
                nestedScrollView.setVisibility(View.GONE);
                cartItemLay.setVisibility(View.GONE);
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
    public void showProgress(final boolean show) {
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
                    cartItemLay.setVisibility(show ? View.GONE : View.VISIBLE);
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
            nestedScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void viewGone() {

        try {
            if (((MainActivity) getActivity()).mLinearView3 != null) {
                ((MainActivity) getActivity()).mLinearView3.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }


    public void removeCartData(JsonObject mJsonObject, int position) {
        try {


            if (mJsonObject != null) {


                Log.d("23.0.3", "productListData: " + position);


                JSONObject json = new JSONObject(mJsonObject.toString());
                if (json.getString("status").equals("200")) {


                    if (cartDataArrayList.size() > 1) {

                        SessionSave.saveSession("partial",json.getString("total_commission"),getContext());
                        System.out.println("partial_update>>>"+json.getString("total_commission"));


                        if (!(SessionSave.getSession("cartCount", getActivity()).equals(""))) {

                            int count = Integer.parseInt(SessionSave.getSession("cartCount", getActivity())) - 1;

                            SessionSave.saveSession("cartCount", "" + count, getActivity());
                            // ((MainActivity) getActivity()).setBadgeCount(getActivity(), SessionSave.getSession("cartCount", getActivity()));
                            no_of_items.setText("" + SessionSave.getSession("cartCount", getActivity()));
                            ((MainActivity) getActivity()).coutTxt.setText(SessionSave.getSession("cartCount", getActivity()));

                        }
                        if (!(total_shopping_amount.equals("")) && !(cartDataArrayList.get(position).getCart_shipping_price().equals(""))) {

                            double shopping_amount = Double.valueOf(total_shopping_amount) - Double.valueOf(cartDataArrayList.get(position).getCart_shipping_price());

                            shipping_charges.setText("" + SessionSave.getSession("currency_symbol", getActivity()) + " " + shopping_amount);

                        }

                        if (!(total_tax_amount.equals("")) && !(cartDataArrayList.get(position).getCart_tax_price().equals(""))) {

                            double tax_price = Double.valueOf(total_tax_amount) - Double.valueOf(cartDataArrayList.get(position).getCart_tax_price());

                            tax_charges.setText("" + SessionSave.getSession("currency_symbol", getActivity()) + " " + tax_price);

                        }


                        if (!(total_amount.equals("")) && !(cartDataArrayList.get(position).getCart_total().equals(""))) {

                            double amount = Double.valueOf(total_amount) - Double.valueOf(cartDataArrayList.get(position).getCart_total());

                            total_charges.setText("" + SessionSave.getSession("currency_symbol", getActivity()) + " " + amount);

                        }

                        cartDataArrayList.remove(position);
                        cartList.removeViewAt(position);
                        homeCatergoryAdap.notifyDataSetChanged();
//                    homeCatergoryAdap.notifyItemRemoved(position);
//                    homeCatergoryAdap.notifyItemRangeChanged(position, cartDataArrayList.size());
                        Snackbar.make(cartItemLay, "" + json.getString("message"), Snackbar.LENGTH_SHORT).show();
                    } else {
                        SessionSave.saveSession("cartCount", "", getActivity());
                        //((MainActivity) getActivity()).setBadgeCount(getActivity(), SessionSave.getSession("cartCount", getActivity()));
                        no_of_items.setText("" + SessionSave.getSession("cartCount", getActivity()));
                        ((MainActivity) getActivity()).coutTxt.setText("0");
                        cartItemLay.setVisibility(View.GONE);
                        nestedScrollView.setVisibility(View.GONE);
                        noDataLay.setVisibility(View.VISIBLE);
                        button_continue.setVisibility(View.GONE);
                    }

                } else {
                    Snackbar.make(cartItemLay, "" + json.getString("message"), Snackbar.LENGTH_SHORT).show();
                }
            } else {

                nestedScrollView.setVisibility(View.GONE);
                cartItemLay.setVisibility(View.GONE);
                ((MainActivity) getActivity()).showSnackBar(nestedScrollView, "" + "Sorry unable to reach server!!! ");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void showRemoveCartProgress(boolean show) {
        subprogressBarLay.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void udpateCartData(JsonObject mJsonObject, int position) {
        try {

            showRemoveCartProgress(false);
            if (mJsonObject != null) {


                JSONObject json = new JSONObject(mJsonObject.toString());
                if (json.getString("status").equals("200")) {

                    SessionSave.saveSession("partial",json.getString("total_commission"),getContext());
                    System.out.println("partial_update>>>"+json.getString("total_commission"));

                    if (homeCatergoryAdap != null) {
                        JSONArray product_review = json.getJSONArray("cart_details");
                        String cart_id = product_review.getJSONObject(0).getString("cart_id");
                        String cart_product_id = product_review.getJSONObject(0).getString("cart_product_id");
                        String cart_user_id = product_review.getJSONObject(0).getString("cart_user_id");
                        String cart_title = product_review.getJSONObject(0).getString("cart_title");
                        String cart_quantity = product_review.getJSONObject(0).getString("cart_quantity");
                        String cart_image = product_review.getJSONObject(0).getString("cart_image");
                        String cart_price = product_review.getJSONObject(0).getString("cart_price");
                        String cart_total = product_review.getJSONObject(0).getString("cart_total");
                        String cart_tax_price = product_review.getJSONObject(0).getString("cart_tax_price");
                        String cart_shipping_price = product_review.getJSONObject(0).getString("cart_shipping_price");
                        System.out.println("total_amount " + total_amount);
                        System.out.println("total_amount 1" + cartDataArrayList.get(position).getCart_total());
                        System.out.println("total_amount 2" + cart_total);
                        Double amount = Double.valueOf(total_amount) - Double.valueOf(cartDataArrayList.get(position).getCart_total());
                        Double sub_amount = Double.valueOf(amount) + Double.valueOf(cart_total);
                        String total_amount = String.valueOf(sub_amount);

                        updateTotalAmount(total_amount);


                        if (!(total_tax_amount.equals("")) && !(cart_tax_price.equals(""))) {

                            Double tax_amount = Double.valueOf(total_tax_amount) - Double.valueOf(cartDataArrayList.get(position).getCart_tax_price());
                            Double sub_tax_amount = Double.valueOf(tax_amount) + Double.valueOf(cart_tax_price);
                            total_tax_amount = String.valueOf(sub_tax_amount);
                            tax_charges.setText("" + SessionSave.getSession("currency_symbol", getActivity()) + " " + total_tax_amount);

                        }

                        if (!(total_shopping_amount.equals("")) && !(cart_shipping_price.equals(""))) {

                            Double shopping_amount = Double.valueOf(total_shopping_amount) - (Double.valueOf(cartDataArrayList.get(position).getCart_shipping_price()) * Double.valueOf(cartDataArrayList.get(position).getCart_quantity()));
                            Double sub_shopping_amount = Double.valueOf(shopping_amount) + Double.valueOf(cart_shipping_price);
                            total_shopping_amount = String.valueOf(sub_shopping_amount);
                            shipping_charges.setText("" + SessionSave.getSession("currency_symbol", getActivity()) + " " + total_shopping_amount);

                        }
                        CartData dealsData = new CartData(cart_id, cart_product_id, cart_user_id, cart_title, cart_quantity, cart_image, cart_price, cart_total,
                                cartDataArrayList.get(position).getProduct_purchase_qty(), cartDataArrayList.get(position).getProduct_available_qty(),
                                cartDataArrayList.get(position).getCart_shipping_price(), cartDataArrayList.get(position).getProduct_size_id(), cartDataArrayList.get(position).getProduct_color_id(), cart_tax_price);
                        cartDataArrayList.set(position, dealsData);

                        homeCatergoryAdap.notifyItemChanged(position);

                        Snackbar.make(nestedScrollView, "" + json.getString("message"), Snackbar.LENGTH_SHORT).show();

                    }

                } else {
                    Snackbar.make(nestedScrollView, "" + json.getString("message"), Snackbar.LENGTH_SHORT).show();
                }
            } else {

                nestedScrollView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                ((MainActivity) getActivity()).showSnackBar(nestedScrollView, "" + "Sorry unable to reach server!!! ");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updateTotalAmount(String amount) {
        total_amount = amount;
        total_charges.setText("" + SessionSave.getSession("currency_symbol", getActivity()) + " " + amount);

    }

    @Override
    public void onStart() {
        super.onStart();

        System.out.println("onBackPressed  " + "onStart" + "onStart");

    }

    @Override
    public void onPause() {
        super.onPause();

        System.out.println("onBackPressed  " + "onPause" + "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();

        System.out.println("onBackPressed  " + "onResume" + "onResume");
    }

}
