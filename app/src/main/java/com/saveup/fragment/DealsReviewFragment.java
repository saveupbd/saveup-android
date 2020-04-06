package com.saveup.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.saveup.activity.MainActivity;
import com.saveup.android.R;
import com.saveup.presenter.UserPresenter;
import com.saveup.utils.NetworkStatus;
import com.saveup.utils.SessionSave;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author AAPBD on 4,May,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.adapter
 */


public class DealsReviewFragment extends Fragment {

    private View progressBar, processView, topmainLay, commentsmainLay;
    private EditText productreviewEdt, producttitleEdt;
    private RatingBar ratingBar1, ratingBar2;
    private ImageView productImage;
    private TextView prodcutname, submitComments;
    private String productId;
    private String ratingCount;
    private String reviewType;
    public interface dealsReview{
        void dealsReviewUpdate(JSONArray array);
    }
    dealsReview mCallback;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (dealsReview) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TextClicked");
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.writereviewlayout, container, false);


        progressBar = view.findViewById(R.id.progressBar);
        processView = view.findViewById(R.id.processView);
        topmainLay = view.findViewById(R.id.topmainLay);
        topmainLay.setVisibility(View.VISIBLE);
        commentsmainLay = view.findViewById(R.id.commentsmainLay);
        commentsmainLay.setVisibility(View.GONE);
        productreviewEdt = view.findViewById(R.id.productreviewEdt);
        producttitleEdt = view.findViewById(R.id.producttitleEdt);
        prodcutname = view.findViewById(R.id.prodcutname);
        submitComments = view.findViewById(R.id.submitComments);
        ratingBar1 = view.findViewById(R.id.ratingBar1);
        ratingBar2 = view.findViewById(R.id.ratingBar2);
        LayerDrawable stars = (LayerDrawable) ratingBar1.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#ff2353"), PorterDuff.Mode.SRC_ATOP);
        productImage = view.findViewById(R.id.productImage);

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

        try {
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

            Bundle bundle = getArguments();

            if (bundle != null) {
                Glide.with(getActivity())
                        .load(bundle.getString("store_img"))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.noimage)
                        .into(productImage);

                prodcutname.setText("" + bundle.getString("store_name"));
                productId = bundle.getString("store_id");
            }

            ratingBar1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {


                    System.out.println("rating " + rating + " fromUser " + fromUser);
                    if (rating > 0) {
                        ratingCount = "" + rating;
                        ratingBar2.setRating(rating);
                        SlideToAbove();
                    }
                }
            });


            productreviewEdt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {


                    if (s.length() > 0) {
                        submitComments.setTextColor(getResources().getColor(R.color.colorAccent));

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            submitComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    producttitleEdt.setError(null);
                    productreviewEdt.setError(null);
                    boolean cancel = false;
                    View focusView = null;


                    if (producttitleEdt.getText().toString().trim().isEmpty()) {
//                    ShowToast(LoginActivity.this, ""+ getResources().getString(R.string.please_enter_email));
                        producttitleEdt.setError("" + "Please enter review title");
                        focusView = producttitleEdt;
                        cancel = true;

                    } else if (productreviewEdt.getText().toString().trim().isEmpty()) {
//                    ShowToast(LoginActivity.this, ""+ getResources().getString(R.string.please_enter_password));
                        productreviewEdt.setError("" + "Please enter comments");
                        focusView = productreviewEdt;
                        cancel = true;
                    }/*else {

                    showProgress(true, loginForm, progressBar);
                    UserPresenter userPresenter = new UserPresenter(LoginActivity.this);
                    userPresenter.userLoginAPI( edtEmail.getText().toString(),edtPassword.getText().toString());

                }*/

                    if (cancel) {
                        // There was an error; don't attempt login and focus the first
                        // form field with an error.
                        focusView.requestFocus();
                    } else {

                        if (NetworkStatus.isOnline(getActivity())) {
                            showProgress(true);
                            UserPresenter userPresenter = new UserPresenter(DealsReviewFragment.this);
                            userPresenter.dealscommentsPage(SessionSave.getSession("user_id", getActivity()), productId, producttitleEdt.getText().toString().trim(), productreviewEdt.getText().toString().trim(), ratingCount);


                        } else {
                            ((MainActivity) getActivity()).ShowToast(getActivity(), "" + getResources().getString(R.string.internet_connection));

                        }

                    }


                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void SlideToAbove() {
        topmainLay.animate()
                .translationY(0)
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        topmainLay.setVisibility(View.GONE);
                        commentsmainLay.setVisibility(View.VISIBLE);
                    }
                });

    }

    public void reviewComments(JsonObject mJsonObject) {

        try {

            if (mJsonObject != null) {


                JSONObject json = new JSONObject(mJsonObject.toString());
                if (json.getString("status").equals("200")) {


                    final JSONArray product_review = json.getJSONArray("deal_review");

                    ((MainActivity) getActivity()).showSnackBar(processView,""+json.getString("message") );
                    getActivity().onBackPressed();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

//                            ProductDetailsFragment fragment = new ProductDetailsFragment();
                            mCallback.dealsReviewUpdate(product_review);
                        }
                    },1000);
                }else {
                    showProgress(false);
                    ((MainActivity) getActivity()).showSnackBar(processView,""+json.getString("message") );
                }
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

            processView.setVisibility(show ? View.GONE : View.VISIBLE);
            processView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    processView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            processView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
