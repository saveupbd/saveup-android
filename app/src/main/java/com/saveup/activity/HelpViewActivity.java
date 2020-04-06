package com.saveup.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.aapbd.appbajarlib.view.WebViewFormatter;
import com.saveup.android.R;

public class HelpViewActivity extends AppCompatActivity {

    String URL="https://saveupbd.com/faq";

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_view);

        webView=findViewById(R.id.helpwebview);


        WebViewFormatter.formatWebViewWithClient(webView, true,
        true);

        webView.loadUrl(URL);

    }
}
