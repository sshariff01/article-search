package com.shariffproductions.articlesearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsArticleAdapter extends RecyclerView.Adapter<NewsArticleAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView headline;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);

            headline = (TextView) itemView.findViewById(R.id.headline);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    public List<NewsArticle> newsArticles;
    public Context context;

    public NewsArticleAdapter(Context context, List<NewsArticle> newsArticles) {
        this.newsArticles = newsArticles;
        this.context = context;
    }

    @Override
    public NewsArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_news_article, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NewsArticleAdapter.ViewHolder viewHolder, int position) {
        NewsArticle newsArticle = newsArticles.get(position);
        viewHolder.headline.setText(newsArticle.headline);
        Picasso.with(context)
                .load(newsArticle.imageUrl)
                .into(viewHolder.image);
    }

    @Override
    public int getItemCount() {
        return newsArticles.size();
    }
}
