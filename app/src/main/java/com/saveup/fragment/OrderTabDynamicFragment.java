package com.saveup.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aapbd.appbajarlib.network.NetInfo;
import com.aapbd.appbajarlib.notification.AlertMessage;
import com.google.gson.JsonObject;
import com.saveup.adapter.OrderListAdapter;
import com.saveup.android.R;
import com.saveup.db.DatabaseHandler;
import com.saveup.model.OrderData;
import com.saveup.presenter.UserPresenter;
import com.saveup.utils.ApplyFont;
import com.saveup.utils.SessionSave;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;

/**
 * Created by AAPBD on 26/5/17.
 */

public class OrderTabDynamicFragment extends Fragment {

    private View mProgressView;
    private View nestedScrollView, noDataLay;
    private RecyclerView storeList;

    private final String TAG = "SearchFragmentDynamic";
    private ArrayList<OrderData> unredeemedObjList = new ArrayList<>();
    private ArrayList<OrderData> redemeededObjList = new ArrayList<>();
    private OrderListAdapter unRedemeedAdapter;
    private OrderListAdapter redeemAdapter;
    String title = "", sortby = "", discount = "";
    private int pageCount = 1;
    private LinearLayoutManager linearLayoutManager;
    private UserPresenter userPresenter;
    private Integer current_page = 1;
    private OrderListAdapter homeCatergoryAdap;
    private boolean isFilter = false;
    private LinearLayout loadingLay;
    private LinearLayout noDatatext;

    private boolean isVisibleToUser;
    private Bundle bundle;
    private int indexPosition;
    private TextView txtRedeemAt,txtPromoCode,txtIsOnline;

    DatabaseHandler myDB;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.orderlistscreen, container, false);
        ButterKnife.bind(this, view);

        myDB = new DatabaseHandler(getContext());
        bundle = getArguments();
        System.out.println("bundle Position " + bundle.getInt("position"));

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        userPresenter = new UserPresenter(OrderTabDynamicFragment.this);
        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        mProgressView = view.findViewById(R.id.progressBar);
        noDataLay = view.findViewById(R.id.noDataLay);
        storeList = view.findViewById(R.id.storeList);
        loadingLay = view.findViewById(R.id.loadingLay);
        loadingLay.setVisibility(View.INVISIBLE);
        storeList.setHasFixedSize(true);
        storeList.setLayoutManager(linearLayoutManager);
        storeList.setNestedScrollingEnabled(false);
        applyFont(view);
        txtRedeemAt = view.findViewById(R.id.txtRedeemAt);
        txtPromoCode = view.findViewById(R.id.txtPromoCode);
        txtIsOnline = view.findViewById(R.id.txtIsOnline);

        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        indexPosition = bundle.getInt("position");


        /*
        load data from database
         */

        loadDataFromDataBase();


        return view;
    }


    public void loadDataFromDataBase() {

        unredeemedObjList = myDB.getAllOrders();
        redemeededObjList = myDB.getAllREDEEMEDOrders();

        Log.e("Unredeemed object  ", "number " + unredeemedObjList.size() + " ");
        Log.e("redeem object  ", "number " + redemeededObjList.size() + " ");

        if (indexPosition == 0) {  //  UNreademed
            unRedemeedAdapter = new OrderListAdapter(getActivity(), unredeemedObjList, OrderTabDynamicFragment.this, storeList);
            storeList.setAdapter(unRedemeedAdapter);


            if (!NetInfo.isOnline(getActivity())) {

                unRedemeedAdapter.setOnItemClickListener(new OrderListAdapter.MyClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        txtPromoCode.setVisibility(View.GONE);
                        txtRedeemAt.setVisibility(View.GONE);



                       final int currentposition=position;
                        if (!unredeemedObjList.get(position).getExpire_day().equalsIgnoreCase(getString(R.string.expired))) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(getString(R.string.redeem_coupon));
                            builder.setMessage(getString(R.string.are_you_sure_redeem_coupon));
                            builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();


                                    OrderData orderData=unredeemedObjList.get(currentposition);
                                    orderData.setOffLineRedeemTime(new Date().getTime()+"");

                                    myDB.addRedeemOrderDATA(orderData);
                                    myDB.deleteUnRedeemObject(orderData.getOrderDBID());



                                    txtPromoCode.setVisibility(View.VISIBLE);
                                    txtPromoCode.setText(unredeemedObjList.get(0).getCoupon_code());

                                    //date
                                    txtRedeemAt.setVisibility(View.VISIBLE);
                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time => " + c);
                                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                    String formattedDate = df.format(c);
                                    txtRedeemAt.setText(getString(R.string.redeem_at)+" : "+formattedDate);


                                    indexPosition=0;


                                   // refresh both list again
                                   //  */
                                    loadDataFromDataBase();



                                }
                            }).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    mProgressView.setVisibility(View.GONE);
                                }
                            });

                            builder.setCancelable(false);
                            builder.show();
                        } else {
                            AlertMessage.showMessage(getActivity(), getString(R.string.error), getString(R.string.voucher_expired));
                        }
                    }
                });


            }
        } else if (indexPosition == 1) {

            /*
            redemmed
             */
            redeemAdapter = new OrderListAdapter(getActivity(), redemeededObjList, OrderTabDynamicFragment.this, storeList);
            storeList.setAdapter(redeemAdapter);
        }


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;

        if (isVisibleToUser && getActivity() != null) {
            callOrderListAPI();
        }

    }

    public void callOrderListAPI() {

        if (NetInfo.isOnline(getContext())) {
            txtIsOnline.setVisibility(View.GONE);
            showProgress(true);
            userPresenter.orderListPage(SessionSave.getSession("user_id", getActivity()), "" + current_page);

        } else {

            /*
            handle when  internet not available
                */

            txtIsOnline.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {

            if (isVisibleToUser) {

                isVisibleToUser = false;
                callOrderListAPI();
            }

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
            noDataLay.setVisibility(View.GONE);
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            nestedScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void applyFont(View view) {

        ApplyFont.applyFont(getActivity(), view.findViewById(R.id.noDataLay));

    }

    public void orderListData(JsonObject jsonObject) {

        try {

            if (jsonObject != null) {

                Log.d(TAG, "productListData: " + current_page + "isFilter " + isFilter);

//                if (!isFilter && current_page == 1) {
//                    unredeemedObjList.clear();
//                   // showProgress(false);
//                } else {
//                    loadingLay.setVisibility(View.INVISIBLE);
//                }


                unredeemedObjList.clear();
                redemeededObjList.clear();

                myDB.deleteOrderTableInfo();
                myDB.deleteRedeemTableInfo();

                JSONObject json = new JSONObject(jsonObject.toString());
                if (json.getString("status").equals("200")) {
//                    nestedScrollView.setVisibility(View.VISIBLE);
//                    noDataLay.setVisibility(View.GONE);

                    JSONArray product_order_list = json.getJSONArray("product_order_list");
                    if (product_order_list.length() > 0) {

                        for (int i = 0; i < product_order_list.length(); i++) {

                            String order_id = product_order_list.getJSONObject(i).getString("order_id");
                            String user_id = product_order_list.getJSONObject(i).getString("user_id");
                            String shipping_name = product_order_list.getJSONObject(i).getString("shipping_name");
                            String order_total = product_order_list.getJSONObject(i).getString("order_total");
                            String order_date = product_order_list.getJSONObject(i).getString("order_date");
                            String expire_day = product_order_list.getJSONObject(i).getString("day");
                            String coupon = product_order_list.getJSONObject(i).getString("coupon_code");
                            OrderData dealsData = new OrderData(order_id, user_id, shipping_name, order_total, order_date, expire_day, coupon);

                            System.out.println("RedeemCoupon>>>" + coupon);
                            // insert to DB
                            myDB.addRedeemOrderDATA(dealsData);
                            redemeededObjList.add(dealsData);
                        }

                    }
                    JSONArray product_order_cod_list = json.getJSONArray("order_list");
                    if (product_order_cod_list.length() > 0) {

                        for (int i = 0; i < product_order_cod_list.length(); i++) {

                            String order_id = product_order_cod_list.getJSONObject(i).getString("order_id");
                            String user_id = product_order_cod_list.getJSONObject(i).getString("user_id");
                            String shipping_name = product_order_cod_list.getJSONObject(i).getString("shipping_name");
                            String order_total = product_order_cod_list.getJSONObject(i).getString("order_total");
                            String order_date = product_order_cod_list.getJSONObject(i).getString("order_date");
                            String expire_day = product_order_cod_list.getJSONObject(i).getString("day");
                            String coupon = product_order_cod_list.getJSONObject(i).getString("coupon_code");
                            OrderData dealsData = new OrderData(order_id, user_id, shipping_name, order_total, order_date, expire_day, coupon);

                            // add to DB
                            myDB.unRedeemedData(dealsData);

                            unredeemedObjList.add(dealsData);
                        }

                    }

                    Log.e("DB items are", myDB.getAllOrders().size() + "  and " + myDB.getAllOrders().size());

                    System.out.println("most_popular_product " + unredeemedObjList.size());


                    // Bundle bundle = getArguments();

                    System.out.println("bundle ---->  " + bundle.getInt("position"));
                    // int position = bundle.getInt("position");
                    Log.e("TAB_INDEX", indexPosition + "outside if order tab dynamic");

                    if (indexPosition == 0) {
                        if (unredeemedObjList.size() > 0) {
                            showProgress(false);
                            if (isFilter) {
                                unRedemeedAdapter.setLoaded();

                            }
                            storeList.getRecycledViewPool().clear();
                            unRedemeedAdapter.notifyDataSetChanged();

                            unRedemeedAdapter.setOnItemClickListener(new OrderListAdapter.MyClickListener() {
                                @Override
                                public void onItemClick(int position, View v) {

                                    try {

                                        Bundle bundle = new Bundle();
                                        bundle.putString("itemprice", unredeemedObjList.get(position).getOrder_total());
                                        bundle.putString("orderdate", unredeemedObjList.get(position).getOrder_date());
                                        bundle.putString("orderId", unredeemedObjList.get(position).getOrder_id());
                                        bundle.putString("expire_day", unredeemedObjList.get(position).getExpire_day());
                                        bundle.putInt("index_position", indexPosition);
                                        Log.e("TAB_INDEX", indexPosition + "inside order tab dynamic");

                                        OrderDetailsFragment fragment2 = new OrderDetailsFragment();
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
                            storeList.setVisibility(View.GONE);
                            nestedScrollView.setVisibility(View.GONE);
                            noDataLay.setVisibility(View.VISIBLE);
                            mProgressView.setVisibility(View.GONE);
                        }

                    } else {

                        if (redemeededObjList.size() > 0) {
                            showProgress(false);
//                            Collections.sort(redemeededObjList, new Comparator<OrderData>() {
//                                public int compare(OrderData o1, OrderData o2) {
//                                    return o2.getOrder_date().compareTo(o1.getOrder_date());
//                                }
//                            });
                            if (isFilter) {
                                redeemAdapter.setLoaded();
                            }
                            storeList.getRecycledViewPool().clear();
                            redeemAdapter.notifyDataSetChanged();


                            redeemAdapter.setOnItemClickListener(new OrderListAdapter.MyClickListener() {
                                @Override
                                public void onItemClick(int position, View v) {

                                    try {

                                        Bundle bundle = new Bundle();
                                        bundle.putString("itemprice", redemeededObjList.get(position).getOrder_total());
                                        bundle.putString("orderdate", redemeededObjList.get(position).getOrder_date());
                                        bundle.putString("orderId", redemeededObjList.get(position).getOrder_id());
                                        bundle.putString("expire_day", redemeededObjList.get(position).getExpire_day());
                                        bundle.putInt("index_position", indexPosition);
                                        Log.e("TAB_INDEX", indexPosition + "inside order tab dynamic");

                                        OrderDetailsFragment fragment2 = new OrderDetailsFragment();
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
                            storeList.setVisibility(View.GONE);
                            nestedScrollView.setVisibility(View.GONE);
                            noDataLay.setVisibility(View.VISIBLE);
                            mProgressView.setVisibility(View.GONE);
                        }

                    }

                } else {

                    storeList.setVisibility(View.GONE);
                    nestedScrollView.setVisibility(View.GONE);
                    noDataLay.setVisibility(View.VISIBLE);
                    loadingLay.setVisibility(View.INVISIBLE);
                    mProgressView.setVisibility(View.GONE);


                }
            } else {

                nestedScrollView.setVisibility(View.GONE);
                noDataLay.setVisibility(View.VISIBLE);
                loadingLay.setVisibility(View.INVISIBLE);
                Snackbar.make(nestedScrollView, "" + getString(R.string.unable_to_reach_server), Snackbar.LENGTH_SHORT).show();

            }

        } catch (
                Exception ex
        ) {
            ex.printStackTrace();

        }

    }


}
