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
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private static final int UPDATE_ADVANCED_SETTINGS = 1;
    private final static String SAVED_INSTANCE_KEY_NEWS_ARTICLES_LIST = "newsArticlesList";
    private NewsArticleAdapter newsArticleAdapter;
    private ArrayList<NewsArticle> newsArticles;
    private EditText searchFilterEditText;
    private String beginDate;
    private String sortOrder;
    private List<String> newsDeskValues = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActivityLayoutOnClickListener();
        initSearchFilter();
        initNewsArticlesGridView(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList(SAVED_INSTANCE_KEY_NEWS_ARTICLES_LIST, newsArticles);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UPDATE_ADVANCED_SETTINGS) {
            if (resultCode != RESULT_OK) return;
            beginDate = parseBeginDate(data);
            sortOrder = data.getStringExtra("sortOrder");
            saveNewsDeskValues(data);
        }
    }

    private String parseBeginDate(Intent data) {
        String[] beginDateComponents = data.getStringExtra("beginDate").split("/");
        String day = beginDateComponents[0];
        String month = beginDateComponents[1];
        String year = beginDateComponents[2];
        return year + month + day;
    }

    private void saveNewsDeskValues(Intent data) {
        parseNewsDeskVaue(data, "Arts");
        parseNewsDeskVaue(data, "Fashion & Style");
        parseNewsDeskVaue(data, "Sports");
    }

    private void parseNewsDeskVaue(Intent data, String newsDeskValue) {
        if (data.getBooleanExtra(newsDeskValue, false)) {
            newsDeskValues.add(newsDeskValue);
        }
    }

    private void initActivityLayoutOnClickListener() {
        LinearLayout activityLayout = (LinearLayout) findViewById(R.id.activity_main);
        activityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() != R.id.et_search) {
                    exitInputMode();
                }
            }
        });
    }

    private void initSearchFilter() {
        searchFilterEditText = (EditText) findViewById(R.id.et_search);
        searchFilterEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_UP)) {
                    exitInputMode();
                    return true;
                }
                return false;
            }
        });
    }

    private void initNewsArticlesGridView(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            newsArticles = new ArrayList<>();
        } else {
            newsArticles = savedInstanceState.getParcelableArrayList(SAVED_INSTANCE_KEY_NEWS_ARTICLES_LIST);
        }
        newsArticleAdapter = new NewsArticleAdapter(this, newsArticles);

        RecyclerView recyclerGridView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerGridView.setAdapter(newsArticleAdapter);
        recyclerGridView.setLayoutManager(new GridLayoutManager(this, 4));
    }

    public void searchForNewsArticles(View view) {
        HttpClient httpClient = HttpClient.getClient();
        httpClient.getNewsArticles(
                searchFilterEditText.getText().toString(),
                beginDate,
                sortOrder,
                newsDeskValues,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        ArrayList<NewsArticle> fetchedNewsArticles = parseNewsArticleDetailsFrom(response);
                        newsArticles.clear();
                        newsArticles.addAll(fetchedNewsArticles);
                        newsArticleAdapter.notifyDataSetChanged();
                        searchFilterEditText.setText("");
                        exitInputMode();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        searchFilterEditText.setText("");
                        exitInputMode();
                        Toast.makeText(MainActivity.this, "Failed to load news article data", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private void exitInputMode() {
        searchFilterEditText.clearFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
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
        startActivityForResult(intent, UPDATE_ADVANCED_SETTINGS);
    }
}
