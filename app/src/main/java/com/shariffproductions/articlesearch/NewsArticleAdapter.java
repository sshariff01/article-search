package com.shariffproductions.articlesearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsArticleAdapter extends ArrayAdapter<NewsArticle> {
    private static class NewsArticleHolder {
        TextView title;
        ImageView image;
    }

    public NewsArticleAdapter(Context context, ArrayList<NewsArticle> newsArticleList) {
        super(context, R.layout.item_news_article, newsArticleList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewsArticle newsArticle = getItem(position);
        final NewsArticleHolder newsArticleHolder;
        if (convertView == null) {
            newsArticleHolder = new NewsArticleHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_news_article, parent, false);
            newsArticleHolder.title = (TextView) convertView.findViewById(R.id.title);
            newsArticleHolder.image = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(newsArticleHolder);
        } else {
            newsArticleHolder = (NewsArticleHolder) convertView.getTag();
        }

        setUpNewsArticleDetails(newsArticle, newsArticleHolder);
        return convertView;
    }

    private void setUpNewsArticleDetails(NewsArticle newsArticle, NewsArticleHolder newsArticleHolder) {
        newsArticleHolder.title.setText(newsArticle.title);
        Picasso.with(getContext())
                .load(newsArticle.imageUrl)
                .into(newsArticleHolder.image);
    }


}
