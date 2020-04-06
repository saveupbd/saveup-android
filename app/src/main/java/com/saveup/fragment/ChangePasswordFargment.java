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
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.saveup.activity.MainActivity;
import com.saveup.android.R;
import com.saveup.presenter.UserPresenter;
import com.saveup.utils.ApplyFont;
import com.saveup.utils.NetworkStatus;
import com.saveup.utils.SessionSave;
import com.saveup.views.RippleView;
import com.google.gson.JsonObject;

import org.json.JSONObject;

/**
 * Created by AAPBD on 25/4/17.
 */

public class ChangePasswordFargment extends Fragment {
    private TextView new_show_hide, confirm_show_hide;

    private EditText edtConfirmPassword, edtNewPassword;
    private View progressBar, mainTopLay;
    private RippleView button_changepassword;
    private boolean isnewshowhide = false, isconfirmshowhide = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.changepasswordfargment, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        mainTopLay = view.findViewById(R.id.mainTopLay);
        new_show_hide = view.findViewById(R.id.new_show_hide);
        confirm_show_hide = view.findViewById(R.id.confirm_show_hide);
        edtConfirmPassword = view.findViewById(R.id.edtConfirmPassword);
        edtNewPassword = view.findViewById(R.id.edtNewPassword);
        button_changepassword = view.findViewById(R.id.button_changepassword);


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
        try {
            ((MainActivity) getActivity()).searchIcon.setVisible(false);
            final MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.search_layout.setVisibility(View.GONE);
            mainActivity.menu_Name.setVisible(false);
            mainActivity.toggle.setDrawerIndicatorEnabled(false);
            mainActivity.disableDrawer(true);
            mainActivity.toolbar.setTitle("" + getResources().getString(R.string.changepassword));
            mainActivity.toolbar.setLogo(R.drawable.transpent_logo);
            final Drawable upArrow = getResources().getDrawable(R.drawable.back_icon);
            mainActivity.toggle.setHomeAsUpIndicator(upArrow);
            mainActivity.cartIconempty.setVisible(false);
            mainActivity.toolbar.setVisibility(View.VISIBLE);
         //   mainActivity.cartItem.setVisible(false);
            mainActivity.toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mainActivity.onBackPressed();
                }
            });


            new_show_hide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (!(edtNewPassword.getText().toString().trim().equals(""))) {
                        if (isnewshowhide) {
                            isnewshowhide = false;
                            edtNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            new_show_hide.setText("" + getResources().getString(R.string.show));
                        } else {
                            edtNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            isnewshowhide = true;
                            new_show_hide.setText("" + getResources().getString(R.string.hide));

                        }
                    }
                }
            });


            confirm_show_hide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!(edtConfirmPassword.getText().toString().trim().equals(""))) {
                        if (isconfirmshowhide) {
                            isconfirmshowhide = false;
                            edtConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            confirm_show_hide.setText("" +getResources().getString(R.string.show));
                        } else {
                            edtConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            isconfirmshowhide = true;
                            confirm_show_hide.setText("" +getResources().getString(R.string.hide));

                        }
                    }
                }
            });

            button_changepassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Reset errors.
                    edtNewPassword.setError(null);
                    edtConfirmPassword.setError(null);
                    boolean cancel = false;
                    View focusView = null;


                    if (edtNewPassword.getText().toString().trim().isEmpty()) {
                        edtNewPassword.setError("" + getResources().getString(R.string.please_enter_new_password));
                        focusView = edtNewPassword;
                        cancel = true;

                    } else if (edtNewPassword.getText().toString().equals(SessionSave.getSession("old_password", getActivity()))) {
                        edtNewPassword.setError("" + getResources().getString(R.string.please_enter_passwordsame));
                        focusView = edtNewPassword;
                        cancel = true;
                    } else if (edtConfirmPassword.getText().toString().trim().isEmpty()) {
                        edtConfirmPassword.setError("" + getResources().getString(R.string.please_enter_confirm_password));
                        focusView = edtConfirmPassword;
                        cancel = true;
                    } else if (!(edtNewPassword.getText().toString().equals(edtConfirmPassword.getText().toString()))) {
                        edtNewPassword.setError("" + getResources().getString(R.string.please_enter_confirmpasswordsame));
                        focusView = edtNewPassword;
                        cancel = true;
                    }


                    if (cancel) {
                        // There was an error; don't attempt login and focus the first
                        // form field with an error.
                        focusView.requestFocus();
                    } else {

                        if (NetworkStatus.isOnline(getActivity())) {
                            showProgress(true);

                            UserPresenter userPresenter = new UserPresenter(ChangePasswordFargment.this);
                            userPresenter.resetPassword(SessionSave.getSession("user_id", getActivity()), SessionSave.getSession("old_password", getActivity()), edtNewPassword.getText().toString());


                        } else {
                            ShowToast(getActivity(), "" + getResources().getString(R.string.internet_connection));

                        }

                    }


                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This is method for show the toast
     */
    public void ShowToast(Context contex, String message) {

        Toast toast = Toast.makeText(contex, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
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


        ApplyFont.applyFont(getActivity(), view.findViewById(R.id.confirm_show_hide));
        ApplyFont.applyFont(getActivity(), view.findViewById(R.id.edtConfirmPassword));
        ApplyFont.applyFont(getActivity(), view.findViewById(R.id.new_show_hide));
        ApplyFont.applyBold(getActivity(), view.findViewById(R.id.button_changepassword));
        ApplyFont.applyFont(getActivity(), view.findViewById(R.id.new_show_hide));


    }

    public void resetPasswordData(JsonObject mJsonObject) {
        if (mJsonObject != null) {
            try {
                JSONObject json = new JSONObject(mJsonObject.toString());
                if (json.getString("status").equals("200")) {
                    showProgress(false);

                    SessionSave.saveSession("old_password", edtNewPassword.getText().toString(), getActivity());
                    Snackbar.make(mainTopLay, "" + json.getString("message"), Snackbar.LENGTH_LONG).show();


                } else {
                    showProgress(false);
                    ShowToast(getActivity(), "" + json.getString("message"));

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {

            mainTopLay.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            Snackbar.make(mainTopLay, "" + "Sorry unable to reach server!!!  ", Snackbar.LENGTH_LONG).show();
        }

    }
}
