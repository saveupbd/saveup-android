package com.saveup.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.saveup.android.R;
import com.facebook.FacebookSdk;
import com.saveup.utils.SessionSave;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import butterknife.ButterKnife;

import static android.content.pm.PackageManager.GET_SIGNATURES;

/**
 * @author AAPBD on 05,March,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.activity
 */


public abstract class BaseActivity extends AppCompatActivity {

    private final String TAG = BaseActivity.class.getSimpleName();
    public  Bundle savedInstance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        savedInstance = savedInstanceState;
        setView();
        printHashKey();

    }

    public void printHashKey() {
        try {
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

    private void setView() {
        try {
            int view = setLayout();
            if (view != 0) {
            //    setLocale();
                setContentView(view);
                initialize();
            } else {
                Log.d(TAG, "setView: Unable to set background_facebook Why Set view is null or 0");
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        bindViews();
    }

    protected void bindViews() {
        ButterKnife.bind(this);
    }

    public abstract int setLayout();

    public abstract void initialize();
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show, final View mainView, final View mProgressView) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mainView.setVisibility(show ? View.GONE : View.VISIBLE);
            mainView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mainView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mainView.setVisibility(show ? View.GONE : View.VISIBLE);
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

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume  " + "Main Activity");
    }


    public void successAlertView(Context contex, String message) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(contex, R.style.hidetitle);

        builder.setCancelable(false)
                .setMessage("" + message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void setLocale() {
        if (SessionSave.getSession("Lang", BaseActivity.this).equals("")) {
            SessionSave.saveSession("Lang", "en", BaseActivity.this);
            SessionSave.saveSession("Lang_Country", "en_GB", BaseActivity.this);
        }
        System.out.println("Lang" + SessionSave.getSession("Lang", BaseActivity.this));
        System.out.println("Lang_Country" + SessionSave.getSession("Lang_Country", BaseActivity.this));
        Configuration config = new Configuration();
        String langcountry = SessionSave.getSession("Lang_Country", BaseActivity.this);
        String arry[] = langcountry.split("_");
        config.locale = new Locale(arry[0], arry[1]);
        Locale.setDefault(new Locale(arry[0], arry[1]));
        BaseActivity.this.getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }
}
