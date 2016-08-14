package com.shariffproductions.articlesearch;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import java.util.List;

public class HttpClient extends AsyncHttpClient {
    private static HttpClient httpClient;

    public static HttpClient getClient() {
        if (httpClient == null) httpClient = new HttpClient();
        return httpClient;
    }

    private HttpClient() {

    }

    public void getNewsArticles(String searchFilter,
                                String beginDate,
                                String sortOrder,
                                List<String> newsDeskValues,
                                ResponseHandlerInterface responseHandler) {
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams requestParams = new RequestParams();
        requestParams.put("api-key", "cec11110d859466dbecb8568a58919f9");
        if (isDefined(searchFilter)) {
            requestParams.put("q", searchFilter);
        }
        if (isDefined(beginDate)) {
            requestParams.put("begin_date", beginDate);
        }
        if (isDefined(sortOrder)) {
            requestParams.put("sort", sortOrder);
        }
        if (newsDeskValues.size() > 0) {
            requestParams.put("fq", buildNewsDeskValuesFilterQuery(newsDeskValues));
        }

        this.get(url, requestParams, responseHandler);
    }

    private String buildNewsDeskValuesFilterQuery(List<String> newsDeskValues) {
        StringBuilder newsDeskValuesStringBuilder = new StringBuilder();
        newsDeskValuesStringBuilder.setLength(0);
        newsDeskValuesStringBuilder.append("news_desk:(");
        for (String newsDeskValue : newsDeskValues) {
            newsDeskValuesStringBuilder.append("\"" + newsDeskValue + "\"");
            newsDeskValuesStringBuilder.append(" ");
        }
        newsDeskValuesStringBuilder.deleteCharAt(newsDeskValuesStringBuilder.length()-1);
        newsDeskValuesStringBuilder.append(")");
        return newsDeskValuesStringBuilder.toString();
    }

    private boolean isDefined(String searchFilter) {
        return searchFilter != null && !searchFilter.trim().isEmpty();
    }
}
