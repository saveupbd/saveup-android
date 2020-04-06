package com.saveup.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.LinearLayout;

import com.saveup.android.R;
import com.saveup.services.CategoryIntenService;
import com.saveup.services.CountryIntenCityService;
import com.saveup.utils.SessionSave;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author AAPBD on 06,May,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.activity
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (SessionSave.getSession("lang", SplashActivity.this).equals("")) {
            SessionSave.saveSession("lang", "en", SplashActivity.this);
            SessionSave.saveSession("Lang_Country", "en_GB", SplashActivity.this);
        }

        LinearLayout mainLay = findViewById(R.id.mainLay);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
       // if (NetworkStatus.isOnline(SplashActivity.this)) {
            Intent in = new Intent(SplashActivity.this, CategoryIntenService.class);
            startService(in);

            if (SessionSave.getSession("user_id", SplashActivity.this).equals("")) {
                Intent i = new Intent(SplashActivity.this, CountryIntenCityService.class);
                startService(i);
            }
//        } else {
//
//            Snackbar snackbar = Snackbar
//                    .make(mainLay, "Network Connection Field!!!", Snackbar.LENGTH_LONG);
//
//            snackbar.show();
//            return;
//        }
        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer.
             */
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                if (SessionSave.getSession("user_id", SplashActivity.this).equals("")) {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 500);
    }


}
