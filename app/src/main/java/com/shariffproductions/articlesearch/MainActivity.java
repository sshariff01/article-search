package com.shariffproductions.articlesearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private NewsArticleAdapter newsArticleAdapter;
    private ArrayList<NewsArticle> newsArticles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsArticles = new ArrayList<>();
        newsArticleAdapter = new NewsArticleAdapter(this, newsArticles);

        RecyclerView recyclerGridView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerGridView.setAdapter(newsArticleAdapter);
        recyclerGridView.setLayoutManager(new GridLayoutManager(this, 4));

        populateGridWithNewsArticles();
    }

    private void populateGridWithNewsArticles() {
        HttpClient httpClient = HttpClient.getClient();
        httpClient.getNewsArticles(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                ArrayList<NewsArticle> fetchedNewsArticles = parseNewsArticleDetailsFrom(response);
                newsArticles.addAll(fetchedNewsArticles);
                newsArticleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(MainActivity.this, "Failed to load news article data", Toast.LENGTH_LONG).show();
            }
        });
    }

    private ArrayList<NewsArticle> parseNewsArticleDetailsFrom(JSONObject response) {
        ArrayList<NewsArticle> newsArticleList = new ArrayList<>();
        try {
            NewsArticle newsArticle;
            JSONObject articleJson;
            JSONArray articlesJsonArray = response.getJSONObject("response").getJSONArray("docs");
            for(int i = 0; i < articlesJsonArray.length(); i++) {
                articleJson = articlesJsonArray.getJSONObject(i);
                String headline = articleJson.getJSONObject("headline").getString("main");
                String imageUrl = articleJson.getJSONArray("multimedia").getJSONObject(1).getString("url");
                newsArticle = new NewsArticle(headline, imageUrl);
                newsArticleList.add(newsArticle);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return newsArticleList;
    }
}
