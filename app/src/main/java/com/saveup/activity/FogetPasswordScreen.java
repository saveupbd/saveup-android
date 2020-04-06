package com.saveup.activity;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.aapbd.appbajarlib.notification.AlertMessage;
import com.saveup.android.R;
import com.google.gson.JsonObject;
import com.saveup.presenter.UserPresenter;
import com.saveup.utils.NetworkStatus;
import com.saveup.views.RippleView;

import butterknife.BindView;

/**
 * @author AAPBD on 05,March,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.activity
 */


public class FogetPasswordScreen extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.button_sendpassword)
    RippleView button_sendpassword;

    @BindView(R.id.edtEmail)
    EditText edtEmail;

    @BindView(R.id.newpasswordview)
    EditText newpasswordview;

    @BindView(R.id.confirmpasswordview)
    EditText confirmpasswordview;


    View passwordForm, progressBar;

    @Override
    public int setLayout() {
        return R.layout.forgotpassword;
    }

    @Override
    public void initialize() {

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.forgotpassword));


        passwordForm = findViewById(R.id.passwordForm);
        progressBar = findViewById(R.id.progressBar);
        button_sendpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                edtEmail.setError(null);
                boolean cancel = false;
                View focusView = null;


                if (edtEmail.getText().toString().trim().isEmpty()) {
//                    ShowToast(FogetPasswordScreen.this, ""+ getResources().getString(R.string.please_enter_email));
                    edtEmail.setError("" + getResources().getString(R.string.please_enter_email));
                    focusView = edtEmail;
                    cancel = true;
                }
//                else if (newpasswordview.getText().toString().trim().isEmpty()) {
//                    newpasswordview.setError("" + getResources().getString(R.string.please_enter_new_password));
//                    focusView = newpasswordview;
//                    cancel = true;
//                } else if (confirmpasswordview.getText().toString().trim().isEmpty()) {
//                    confirmpasswordview.setError("" + getResources().getString(R.string.please_enter_new_password));
//                    focusView = confirmpasswordview;
//                    cancel = true;
//                }
//                else if (!newpasswordview.getText().toString().trim().equals(confirmpasswordview.getText().toString().trim())) {
//                    confirmpasswordview.setError("" + getResources().getString(R.string.please_enter_new_password));
//                    focusView = confirmpasswordview;
//                    cancel = true;
//                }
                else if (!isValidEmail(edtEmail.getText().toString().trim())) {
//                    ShowToast(FogetPasswordScreen.this, ""+ getResources().getString(R.string.please_enter_email_invaild));
                    edtEmail.setError("" + getResources().getString(R.string.please_enter_email_invaild));
                    focusView = edtEmail;
                    cancel = true;
                }
// else {
//
//                    showProgress(true, passwordForm, progressBar);
//                    UserPresenter userPresenter = new UserPresenter(FogetPasswordScreen.this);
//                    userPresenter.userforgotPasswordAPI(edtEmail.getText().toString());
//
//                }
                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {

                    if (NetworkStatus.isOnline(FogetPasswordScreen.this)) {

                        hideKeyboard(FogetPasswordScreen.this);
                        //showProgress(true, passwordForm, progressBar);
                        UserPresenter userPresenter = new UserPresenter(FogetPasswordScreen.this);
                        userPresenter.userresetPasswordAPI(edtEmail.getText().toString());

                        new AlertDialog.Builder(FogetPasswordScreen.this)
                                .setTitle("Reset Password")
                                .setMessage("We have sent you an email, Please check your email to reset your password!")

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Continue with delete operation
                                        finish();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                      //  AlertMessage.showMessage(FogetPasswordScreen.this, "Reset Password", "We have sent you an email, Please check your email to reset your password!");

                    } else {
                    //    ShowToast(FogetPasswordScreen.this, "" + getResources().getString(R.string.internet_connection));
                        Snackbar.make(passwordForm, "" + getString(R.string.internet_connection), Snackbar.LENGTH_LONG).show();
                    }

                }


            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public void passwordResult(JsonObject mJsonObject) {
//        Log.d("Result", "onNext: " + mJsonObject);
//
//
//        if (mJsonObject != null) {
//            showProgress(false, passwordForm, progressBar);
//            try {
//                JSONObject json = new JSONObject(mJsonObject.toString());
//                ShowToast(FogetPasswordScreen.this, "" + json.getString("message"));
//
////                if (json.getString("status").equals("200")) {
////                    ShowToast(FogetPasswordScreen.this, "" + json.getString("message"));
//////                    Intent i = new Intent(FogetPasswordScreen.this, LoginActivity.class);
//////                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//////                    startActivity(i);
//////                    finish();
////
////                } else {
////                    ShowToast(FogetPasswordScreen.this, "" + json.getString("message"));
////
////                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        } else {
//
//            passwordForm.setVisibility(View.GONE);
//            progressBar.setVisibility(View.GONE);
//            Snackbar.make(passwordForm, "" + getString(R.string.unable_to_reach_server), Snackbar.LENGTH_LONG).show();
//        }


    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
