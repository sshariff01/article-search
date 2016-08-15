package com.shariffproductions.articlesearch;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsArticleAdapter extends RecyclerView.Adapter<NewsArticleAdapter.ViewHolder> {
    public List<NewsArticle> newsArticles;
    public Context context;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView headline;
        ImageView image;
        Context context;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            this.headline = (TextView) itemView.findViewById(R.id.headline);
            this.image = (ImageView) itemView.findViewById(R.id.image);
            this.context = context;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition();
            NewsArticle newsArticle = newsArticles.get(position);
            Intent intent = new Intent(this.context, NewsArticleWebActivity.class);
            intent.putExtra("webUrl", newsArticle.webUrl);
            this.context.startActivity(intent);
        }
    }

    public NewsArticleAdapter(Context context, List<NewsArticle> newsArticles) {
        this.newsArticles = newsArticles;
        this.context = context;
    }

    @Override
    public NewsArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_news_article, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ViewHolder viewHolder = new ViewHolder(context, view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NewsArticleAdapter.ViewHolder viewHolder, int position) {
        NewsArticle newsArticle = newsArticles.get(position);
        viewHolder.headline.setText(newsArticle.headline);
        if (newsArticle.imageUrl == null) {
            Picasso.with(context)
                    .load(R.drawable.default_article_icon)
                    .into(viewHolder.image);
        } else {
            Picasso.with(context)
                    .load(newsArticle.imageUrl)
                    .into(viewHolder.image);
        }
    }

    @Override
    public int getItemCount() {
        return newsArticles.size();
    }
}
