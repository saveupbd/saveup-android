package com.saveup.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saveup.activity.MainActivity;
import com.saveup.adapter.DealsListAdapter;
import com.saveup.android.R;
import com.saveup.model.DealsListData;
import com.saveup.presenter.UserPresenter;
import com.saveup.services.OnLoadMoreListener;
import com.saveup.utils.ApplyFont;
import com.saveup.utils.SimpleDividerItemDecoration;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.facebook.login.widget.ProfilePictureView.TAG;

/**
 * Created by AAPBD on 19/4/17.
 */

public class DealsListFragment extends Fragment {


    private RecyclerView productList;
    private View mProgressView;
    private View maintopLay, noDataLay;
    private ArrayList<DealsListData> productDataArrayList = new ArrayList<>();
    String title = "", sortby = "", discount = "";
    private GridLayoutManager gridlayoutManager;
    private UserPresenter userPresenter;
    private Integer current_page = 1;
    private DealsListAdapter homeCatergoryAdap;
    private boolean isFilter = false;
    private LinearLayout loadingLay;
    private RelativeLayout deals_search_layout;
    private EditText deals_searchView;
    private ImageView search_icon;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dealslistfragment, container, false);

        gridlayoutManager = new GridLayoutManager(getActivity(), 2);

        userPresenter = new UserPresenter(DealsListFragment.this);

        maintopLay = view.findViewById(R.id.mainLay);
        mProgressView = view.findViewById(R.id.progressBar);
        noDataLay = view.findViewById(R.id.noDataLay);


        deals_search_layout = view.findViewById(R.id.deals_search_layout);
        deals_searchView = view.findViewById(R.id.deals_searchView);
        search_icon = view.findViewById(R.id.search_icon);
        loadingLay = view.findViewById(R.id.loadingLay);
        loadingLay.setVisibility(View.INVISIBLE);
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


    public void searchViewVisible() {

        if (deals_search_layout.getVisibility() == View.GONE)
            deals_search_layout.setVisibility(View.VISIBLE);
        else
            deals_search_layout.setVisibility(View.GONE);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).setTag = "dealslist";
        try {
            ((MainActivity) getActivity()).searchIcon.setVisible(true);


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

            showProgress(true);
           // mainActivity.toolbar.setTitle("" + getResources().getString(R.string.deals));
            mainActivity.productList = "" + getResources().getString(R.string.deals);
            ((MainActivity) getActivity()).toolbar.setLogo(R.drawable.transpent_logo);
            Bundle bundle = getArguments();
            if (bundle != null) {

                title = bundle.getString("title");
                userPresenter.dealsListPage("" + current_page, title);
            } else {
                userPresenter.dealsListPage("" + current_page, title);
            }


            search_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgress(true);
                    isFilter = false;
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(deals_searchView.getWindowToken(), 0);
                    noDataLay.setVisibility(View.GONE);
                    current_page = 1;
                    userPresenter.dealsListPage("" + current_page, deals_searchView.getText().toString());
                }
            });

            deals_searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    System.out.println("actionId -- > " +actionId);
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        noDataLay.setVisibility(View.GONE);
                        current_page = 1;
                        isFilter = false;
                        showProgress(true);
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(deals_searchView.getWindowToken(), 0);
                        userPresenter.dealsListPage("" + current_page, deals_searchView.getText().toString());
                        return true;
                    }
                    return false;
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }

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

    public void productListData(JsonObject jsonObject) {

        try {

            if (jsonObject != null) {
                Log.d(TAG, "productListData: " + current_page);


                if (!isFilter) {
                    productDataArrayList.clear();
                    showProgress(false);
                } else {
                    loadingLay.setVisibility(View.INVISIBLE);
                }


                JSONObject json = new JSONObject(jsonObject.toString());
                if (json.getString("status").equals("200")) {
                    maintopLay.setVisibility(View.VISIBLE);
                    noDataLay.setVisibility(View.GONE);

                    JSONArray product_review = json.getJSONArray("deal_list");
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
//                        HashSet<DealsListData> hashSet = new HashSet<DealsListData>();
//                        hashSet.addAll(productDataArrayList);
//                        productDataArrayList.clear();
//                        productDataArrayList.addAll(hashSet);
                        System.out.println("most_popular_product " + productDataArrayList.size());

                        if (!isFilter) {

                            homeCatergoryAdap = new DealsListAdapter(getActivity(), productDataArrayList, productList);
                            productList.setAdapter(homeCatergoryAdap);
                        } else {

                            homeCatergoryAdap.notifyDataSetChanged();
                            homeCatergoryAdap.setLoaded();

                        }


                        homeCatergoryAdap.setOnLoadMoreListener(new OnLoadMoreListener() {
                            @Override
                            public void onLoadMore() {
                                Log.e("haint", "Load More");
                                isFilter = true;
                                current_page++;
                                loadingLay.setVisibility(View.VISIBLE);
                                TranslateAnimation animation = new TranslateAnimation(0f, 0f, loadingLay.getHeight(), 0f);
                                animation.setDuration(300);
                                loadingLay.startAnimation(animation);
                                //Remove loading item
                                userPresenter.dealsListPage("" + current_page, title);
                            }
                        });


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
                                    fragmentTransaction.add(R.id.fragment_container, fragment2, "deallist");
                                    fragment2.setArguments(bundle);
                                    fragmentTransaction.addToBackStack("");
                                    fragmentTransaction.commit();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    } else {

                        if (!isFilter) {
                            productList.setVisibility(View.GONE);
                            maintopLay.setVisibility(View.GONE);
                            noDataLay.setVisibility(View.VISIBLE);


                        } else {

                            Snackbar.make(maintopLay, "" + json.getString("message"), Snackbar.LENGTH_SHORT).show();
                        }


                    }
                } else {

                    if (!isFilter) {
                        maintopLay.setVisibility(View.GONE);
                        noDataLay.setVisibility(View.VISIBLE);
                    } else {

                        Snackbar.make(maintopLay, "" + json.getString("message"), Snackbar.LENGTH_SHORT).show();
                    }


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
