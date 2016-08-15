package com.shariffproductions.articlesearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class NewsArticleWebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_article_web);
        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(formatWebUrlForMobile(getIntent().getStringExtra("webUrl")));
    }

    private String formatWebUrlForMobile(String webUrl) {
        return "http://mobile." + webUrl.substring(webUrl.indexOf("nytimes.com"));
    }
}
