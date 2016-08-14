package com.shariffproductions.articlesearch;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    private EditText searchFilterEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActivityLayoutOnClickListener();
        initNewsArticlesGridView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void initNewsArticlesGridView() {
        newsArticles = new ArrayList<>();
        newsArticleAdapter = new NewsArticleAdapter(this, newsArticles);

        RecyclerView recyclerGridView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerGridView.setAdapter(newsArticleAdapter);
        recyclerGridView.setLayoutManager(new GridLayoutManager(this, 4));
    }

    private void initActivityLayoutOnClickListener() {
        LinearLayout activityLayout = (LinearLayout) findViewById(R.id.activity_main);
        searchFilterEditText = (EditText) findViewById(R.id.search_filter);
        searchFilterEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_UP)) {
                    searchForNewsArticles(v);
                    return true;
                }
                return false;
            }
        });
        activityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() != R.id.search_filter) {
                    exitInputSearchFilterMode();
                }
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
                JSONArray multimedia = articleJson.getJSONArray("multimedia");
                String imageUrl = (multimedia.length() > 0) ? multimedia.getJSONObject(0).getString("url") : null;
                newsArticle = new NewsArticle(headline, imageUrl);
                newsArticleList.add(newsArticle);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return newsArticleList;
    }

    public void searchForNewsArticles(View view) {
        HttpClient httpClient = HttpClient.getClient();
        httpClient.getNewsArticles(searchFilterEditText.getText().toString(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                ArrayList<NewsArticle> fetchedNewsArticles = parseNewsArticleDetailsFrom(response);
                newsArticles.addAll(fetchedNewsArticles);
                newsArticleAdapter.notifyDataSetChanged();
                exitInputSearchFilterMode();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                exitInputSearchFilterMode();
                Toast.makeText(MainActivity.this, "Failed to load news article data", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void exitInputSearchFilterMode() {
        searchFilterEditText.clearFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.advanced_settings:
                openAdvancedSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openAdvancedSettings() {
        Intent intent = new Intent(MainActivity.this, AdvancedSettingsActivity.class);
        startActivity(intent);
    }
}
