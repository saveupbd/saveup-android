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
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.saveup.activity.MainActivity;
import com.saveup.adapter.DealsListAdapter;
import com.saveup.android.R;
import com.saveup.model.DealsListData;
import com.saveup.utils.ApplyFont;
import com.saveup.utils.SessionSave;
import com.saveup.utils.SimpleDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.facebook.login.widget.ProfilePictureView.TAG;

/**
 * @author AAPBD on 03,May,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.fragment
 */


public class StoreDealsListFragment extends Fragment {


    private RecyclerView productList;
    private View mProgressView;
    private View maintopLay, noDataLay;
    private ArrayList<DealsListData> productDataArrayList = new ArrayList<>();
    String title = "", sortby = "", discount = "";
    private GridLayoutManager gridlayoutManager;
    private Integer current_page = 1;
    private DealsListAdapter homeCatergoryAdap;
    private boolean isFilter = false;
    private TextView noDatatext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dealslistfragment, container, false);

        gridlayoutManager = new GridLayoutManager(getActivity(), 2);


        maintopLay = view.findViewById(R.id.mainLay);
        mProgressView = view.findViewById(R.id.progressBar);
        noDataLay = view.findViewById(R.id.noDataLay);
        noDatatext = view.findViewById(R.id.noDatatext);
        productList = view.findViewById(R.id.productList);
//        productList.setHasFixedSize(true);
        productList.setLayoutManager(gridlayoutManager);
        productList.setNestedScrollingEnabled(false);
        productList.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
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
        productListData();
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getActivity().getResources().getInteger(android.R.integer.config_shortAnimTime);

            maintopLay.setVisibility(show ? View.GONE : View.VISIBLE);
            maintopLay.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    maintopLay.setVisibility(show ? View.GONE : View.VISIBLE);
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
            maintopLay.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void productListData() {

        try {

            if (SessionSave.getSession("storedetails", getActivity()) != null) {
                Log.d(TAG, "productListData: " + current_page);


                JSONObject json = new JSONObject(SessionSave.getSession("storedetails", getActivity()));
                if (json.getString("status").equals("200")) {
                    maintopLay.setVisibility(View.VISIBLE);
                    noDataLay.setVisibility(View.GONE);

                    JSONArray product_review = json.getJSONArray("deal_list_by_shop");
                    if (product_review.length() > 0) {

                        for (int i = 0; i < product_review.length(); i++) {

                            String deal_id = product_review.getJSONObject(i).getString("deal_id");
                            String deal_title = product_review.getJSONObject(i).getString("deal_title");
                            String deal_original_price = product_review.getJSONObject(i).getString("deal_original_price");
                            String deal_discount_price = product_review.getJSONObject(i).getString("deal_discount_price");
                            String deal_discount_percentage = product_review.getJSONObject(i).getString("deal_discount_percentage");
                            String deal_saving_price = product_review.getJSONObject(i).getString("deal_saving_price");
                            String deal_currency_code = product_review.getJSONObject(i).getString("deal_currency_code");
                            String deal_currency_symbol = product_review.getJSONObject(i).getString("deal_currency_symbol");


                            String deal_start_date = product_review.getJSONObject(i).getString("deal_start_date");
                            String deal_end_date = product_review.getJSONObject(i).getString("deal_end_date");
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date1 = format.parse(deal_start_date);
                            Date date2 = format.parse(deal_end_date);


                            String deal_shop_id = product_review.getJSONObject(i).getString("deal_shop_id");
                            String deal_expiry_date = product_review.getJSONObject(i).getString("deal_expiry_date");
                            String deal_image = product_review.getJSONObject(i).getString("deal_image");
                            String deal_activeorexpire_status = product_review.getJSONObject(i).getString("deal_activeorexpire_status");
                            String deal_status = product_review.getJSONObject(i).getString("deal_status");


                            DealsListData dealsData = new DealsListData(deal_id, deal_title, deal_original_price, deal_discount_price, deal_discount_percentage, deal_saving_price, deal_currency_code, deal_currency_symbol,
                                    deal_start_date, deal_end_date, deal_expiry_date, deal_shop_id, deal_image, deal_activeorexpire_status, deal_status, date1.getTime(), date2.getTime());
                            productDataArrayList.add(dealsData);
                        }


                        System.out.println("most_popular_product " + productDataArrayList.size());

                        productList.setVisibility(View.VISIBLE);
//
                        homeCatergoryAdap = new DealsListAdapter(getActivity(), productDataArrayList, productList);
                        productList.setAdapter(homeCatergoryAdap);


                        homeCatergoryAdap.setOnItemClickListener(new DealsListAdapter.MyClickListener() {
                            @Override
                            public void onItemClick(int position, View v) {
                                ((MainActivity) getActivity()).isTouchFromHome = false;
                                ((MainActivity) getActivity()).titleTxt = title;
                                try {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("deal_id", productDataArrayList.get(position).getDeal_id());
                                    bundle.putString("title", productDataArrayList.get(position).getDeal_title());
                                    DealsListDetailsFragment fragment2 = new DealsListDetailsFragment();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.add(R.id.fragment_container, fragment2, "storedealslist");
                                    fragment2.setArguments(bundle);
                                    fragmentTransaction.addToBackStack("");
                                    fragmentTransaction.commit();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    } else {

                        productList.setVisibility(View.GONE);
                        maintopLay.setVisibility(View.GONE);
                        noDataLay.setVisibility(View.VISIBLE);
                        noDatatext.setText("No Deal available ");


                    }
                } else {

                    maintopLay.setVisibility(View.GONE);
                    noDataLay.setVisibility(View.VISIBLE);
                    noDatatext.setText("No Deal available ");

                }
            } else {

                maintopLay.setVisibility(View.GONE);
                mProgressView.setVisibility(View.GONE);
                ((MainActivity) getActivity()).showSnackBar(maintopLay, "" + "Sorry unable to reach server!!! ");
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

//    private Integer getCurrentPage() {
//        return current_page;
//    }
//
//    private void setCurrentPage(Integer current_page) {
//        this.current_page = current_page;
//    }

    public void applyFont(View view) {

        ApplyFont.applyFont(getActivity(), view.findViewById(R.id.button_sort));
        ApplyFont.applyFont(getActivity(), view.findViewById(R.id.button_filter));

    }


}

