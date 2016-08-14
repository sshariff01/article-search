package com.shariffproductions.articlesearch;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

public class HttpClient extends AsyncHttpClient {
    private static HttpClient httpClient;

    public static HttpClient getClient() {
        if (httpClient == null) httpClient = new HttpClient();
        return httpClient;
    }

    private HttpClient() {

    }

    public void getNewsArticles(ResponseHandlerInterface responseHandler) {
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams requestParams = new RequestParams();
        requestParams.put("api-key", "cec11110d859466dbecb8568a58919f9");

        this.get(url, requestParams, responseHandler);
    }
}
