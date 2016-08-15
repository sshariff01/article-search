package com.shariffproductions.articlesearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class NewsArticleWebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView webView = new WebView(this);
        setContentView(webView);
        webView.loadUrl(formatWebUrlForMobile(getIntent().getStringExtra("webUrl")));
        finish();
    }

    private String formatWebUrlForMobile(String webUrl) {
        return "http://mobile." + webUrl.substring(webUrl.indexOf("nytimes.com"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
