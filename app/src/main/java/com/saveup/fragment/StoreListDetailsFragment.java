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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saveup.activity.MainActivity;
import com.saveup.adapter.BranchListAdapter;
import com.saveup.adapter.ReviewCommentsAdapter;
import com.saveup.android.R;
import com.saveup.model.MostPopularData;
import com.saveup.model.ReviewData;
import com.saveup.model.StoreData;
import com.saveup.utils.ApplyFont;
import com.saveup.utils.SessionSave;
import com.saveup.utils.SimpleDividerItemDecoration;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.saveup.android.R.id.storeList;
import static com.facebook.login.widget.ProfilePictureView.TAG;

/**
 * @author AAPBD on 03,May,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.fragment
 */


public class StoreListDetailsFragment extends Fragment {


    private ImageView storeImage;
    private RecyclerView reviewList, mBranchList;
    private TextView storeName, storeAddress, storeNumber;
    private View mProgressView;
    private View nestedScrollView, branchLay;
    private ArrayList<StoreData> storeDataArrayList = new ArrayList<>();
    private BranchListAdapter homeCatergoryAdap;
    private ArrayList<MostPopularData> branchArrayList = new ArrayList<>();
    private ArrayList<ReviewData> reviewDatasList = new ArrayList<>();
    private LinearLayout product_review_card;
    ReviewCommentsAdapter reviewCommentsAdapter;
    private String store_img, storeID, storeN;
    private Button product_writeReview, product_main_writeReview;
    private LinearLayoutManager linearLayoutManager;

    // newInstance constructor for creating fragment with arguments
    public static StoreListDetailsFragment newInstance(String result) {
        System.out.println("Home Screeen 3" + result);
        Bundle bundle = new Bundle();
        bundle.putString("result", result);
        StoreListDetailsFragment allFeedFragment = new StoreListDetailsFragment();
        allFeedFragment.setArguments(bundle);
        return allFeedFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.storelistdetailsfragment, container, false);
        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        mProgressView = view.findViewById(R.id.progressBar);

        storeImage = view.findViewById(R.id.storeImage);
        storeName = view.findViewById(R.id.storeName);
        storeAddress = view.findViewById(R.id.storeAddress);
        storeNumber = view.findViewById(R.id.storeNumber);
        product_writeReview = view.findViewById(R.id.product_writeReview);
        product_main_writeReview = view.findViewById(R.id.product_main_writeReview);
        branchLay = view.findViewById(R.id.branchLay);
        product_review_card = view.findViewById(R.id.product_review_array);

        reviewList = view.findViewById(R.id.reviewList);
        reviewList.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        reviewList.setLayoutManager(linearLayoutManager);
        reviewList.setNestedScrollingEnabled(false);
        reviewList.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));


        mBranchList = view.findViewById(storeList);
        mBranchList.setHasFixedSize(true);
        mBranchList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mBranchList.setNestedScrollingEnabled(false);


        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        applyFont(view);
        return view;
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
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            nestedScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).searchIcon.setVisible(false);

        try {
            product_writeReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (storeID != null) {
                        Bundle bundle = new Bundle();
                        bundle.putString("store_img", store_img);
                        bundle.putString("store_name", storeN);
                        bundle.putString("store_id", storeID);

                        StoreReviewFragment fragment2 = new StoreReviewFragment();
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

                    if (storeID != null) {
                        Bundle bundle = new Bundle();
                        bundle.putString("store_img", store_img);
                        bundle.putString("store_name", storeN);
                        bundle.putString("store_id", storeID);

                        StoreReviewFragment fragment2 = new StoreReviewFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.add(R.id.fragment_container, fragment2, "review");
                        fragment2.setArguments(bundle);
                        fragmentTransaction.addToBackStack("");
                        fragmentTransaction.commit();
                    }
                }
            });

            if (SessionSave.getSession("storedetails", getActivity()) != null) {
                storeBranchUpdate(SessionSave.getSession("storedetails", getActivity()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void applyFont(View view) {

        ApplyFont.applyBold(getActivity(), view.findViewById(R.id.storeName));
        ApplyFont.applyLightFont(getActivity(), view.findViewById(R.id.storeAddress));
        ApplyFont.applyLightFont(getActivity(), view.findViewById(R.id.storeNumber));
        ApplyFont.applyBold(getActivity(), view.findViewById(R.id.textView2));

    }

    public void reviewListUpdate(JSONArray store_review) {

        try {
            if (store_review.length() > 0) {

                for (int i = 0; i < store_review.length(); i++) {


                    String review_title = store_review.getJSONObject(i).getString("review_title");
                    String review_comments = store_review.getJSONObject(i).getString("review_comments");
                    String ratings = store_review.getJSONObject(i).getString("ratings");
                    String review_date = store_review.getJSONObject(i).getString("review_date");
                    String user_id = store_review.getJSONObject(i).getString("user_id");
                    String user_name = store_review.getJSONObject(i).getString("user_name");
                    String user_img = store_review.getJSONObject(i).getString("user_img");
                    ReviewData dealsData = new ReviewData(review_title, review_comments, ratings, review_date, user_id, user_name, user_img);
                    reviewDatasList.add(dealsData);
                }


            }

            if (reviewCommentsAdapter != null) {

                linearLayoutManager.setReverseLayout(true);
                linearLayoutManager.setStackFromEnd(true);
                reviewCommentsAdapter.notifyDataSetChanged();
            } else {
                reviewCommentsAdapter = new ReviewCommentsAdapter(getActivity(), reviewDatasList);
                reviewList.setAdapter(reviewCommentsAdapter);
                reviewList.setVisibility(View.VISIBLE);
                product_review_card.setVisibility(View.VISIBLE);

                product_main_writeReview.setVisibility(View.GONE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void storeBranchUpdate(String result) {

        try {

            showProgress(false);

            if (SessionSave.getSession("storedetails", getActivity()) != null && !(SessionSave.getSession("storedetails", getActivity()).equals(""))) {
                SessionSave.saveSession("storedetails", "" + result, getActivity());
                JSONObject json = new JSONObject(SessionSave.getSession("storedetails", getActivity()));

                if (json.getString("status").equals("200")) {

                    JSONArray jsonArray = json.getJSONArray("store_details");

                    if (jsonArray.length() > 0) {
                        Glide.with(getActivity())
                                .load(jsonArray.getJSONObject(0).getString("store_img"))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .error(R.drawable.noimage)
                                .into(storeImage);

                        store_img = jsonArray.getJSONObject(0).getString("store_img");
                        storeN = jsonArray.getJSONObject(0).getString("store_name");
                        storeID = jsonArray.getJSONObject(0).getString("store_id");
                        storeName.setText("" + jsonArray.getJSONObject(0).getString("store_name"));

                        storeAddress.setText(Html.fromHtml(jsonArray.getJSONObject(0).optString("description")));

                        //save up doesn't require address and number
//                        storeAddress.setText("" + jsonArray.getJSONObject(0).getString("store_address1") + ", " +
//                                jsonArray.getJSONObject(0).getString("store_address2") + ", " +
//                                jsonArray.getJSONObject(0).getString("store_city_name") + ", " +
//                                jsonArray.getJSONObject(0).getString("store_country_name") + " - " +
//                                jsonArray.getJSONObject(0).getString("store_zipcode"));
//                        storeNumber.setText(getResources().getString(R.string.hint_number)+" : " + jsonArray.getJSONObject(0).getString("store_phone"));

                    }

                    JSONArray branch_list = json.getJSONArray("branch_list");

                    System.out.println("storeDataArrayList  branch_list " + branch_list.length());
                    if (branch_list.length() > 0) {
                        branchLay.setVisibility(View.VISIBLE);

                        storeDataArrayList.clear();
                        for (int i = 0; i < branch_list.length(); i++) {

                            String store_id = branch_list.getJSONObject(i).getString("store_id");
                            String store_name = branch_list.getJSONObject(i).getString("store_name");
                            String store_img = branch_list.getJSONObject(i).getString("store_img");
                            String store_status = branch_list.getJSONObject(i).getString("store_status");
                            String product_count = branch_list.getJSONObject(i).getString("product_count");
                            String deal_count = branch_list.getJSONObject(i).getString("deal_count");

                            StoreData dealsData = new StoreData(store_id, store_name, store_img, store_status, product_count, deal_count);
                            storeDataArrayList.add(dealsData);
                        }
                        System.out.println("most_popular_product " + storeDataArrayList.size());
                        mBranchList.setVisibility(View.VISIBLE);
                        homeCatergoryAdap = new BranchListAdapter(getActivity(), storeDataArrayList);
                        mBranchList.setAdapter(homeCatergoryAdap);

//                        homeCatergoryAdap.setOnItemClickListener(new BranchListAdapter.MyClickListener() {
//                            @Override
//                            public void onItemClick(int position, View v) {
//
//                                try {
//                                    showProgress(true);
//                                    UserPresenter userPresenter = new UserPresenter(StoreListDetailsFragment.this);
//                                    userPresenter.shopdetailsBranchPage(storeDataArrayList.get(position).getStore_id());
//
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        });

                    } else {
                        branchLay.setVisibility(View.GONE);
                    }

                    JSONArray store_review = json.getJSONArray("store_review");
                    if (store_review.length() > 0) {
                        reviewDatasList.clear();
                        for (int i = 0; i < store_review.length(); i++) {


                            String review_title = store_review.getJSONObject(i).getString("review_title");
                            String review_comments = store_review.getJSONObject(i).getString("review_comments");
                            String ratings = store_review.getJSONObject(i).getString("ratings");
                            String review_date = store_review.getJSONObject(i).getString("review_date");
                            String user_id = store_review.getJSONObject(i).getString("user_id");
                            String user_name = store_review.getJSONObject(i).getString("user_name");
                            String user_img = store_review.getJSONObject(i).getString("user_img");
                            ReviewData dealsData = new ReviewData(review_title, review_comments, ratings, review_date, user_id, user_name, user_img);
                            reviewDatasList.add(dealsData);
                        }


                        System.out.println("most_popular_product " + reviewDatasList.size());
                        reviewCommentsAdapter = new ReviewCommentsAdapter(getActivity(), reviewDatasList);
                        reviewList.setAdapter(reviewCommentsAdapter);
                        reviewList.setVisibility(View.VISIBLE);
                        product_review_card.setVisibility(View.VISIBLE);
                        product_main_writeReview.setVisibility(View.GONE);


                    } else {
                        reviewList.setVisibility(View.GONE);
                        product_review_card.setVisibility(View.GONE);
                        product_main_writeReview.setVisibility(View.VISIBLE);
                    }

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void storeDetailsUpdate(JsonObject jsonObject) {
        try {

            if (jsonObject != null) {
                Log.d(TAG, "homePageData : " + jsonObject.toString());
                storeBranchUpdate(jsonObject.toString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
