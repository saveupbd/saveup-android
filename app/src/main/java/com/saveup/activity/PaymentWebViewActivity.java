package com.saveup.activity;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.aapbd.appbajarlib.network.NetInfo;
import com.aapbd.appbajarlib.view.WebViewFormatter;
import com.saveup.android.R;

public class PaymentWebViewActivity extends AppCompatActivity {

    public static final String WEBVIEW_URL = "webview_url";

    //widgets
    private TextView noInternetTv;
    private WebView paymentWebView;

    private ImageView backIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_web_view);

        initUi();
    }

    private void initUi() {

        noInternetTv = findViewById(R.id.payment_no_internet_tv);
        paymentWebView = findViewById(R.id.payment_webview);
        backIv = findViewById(R.id.payment_back_iv);

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        WebViewFormatter.formatWebViewWithClient(paymentWebView, true, true);
        paymentWebView.loadUrl(getIntent().getStringExtra(WEBVIEW_URL));

        if (!NetInfo.isOnline(this)) {
            paymentWebView.setVisibility(View.GONE);
            noInternetTv.setVisibility(View.VISIBLE);
        }else {
            paymentWebView.setVisibility(View.VISIBLE);
            noInternetTv.setVisibility(View.GONE);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PaymentWebViewActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }
}
