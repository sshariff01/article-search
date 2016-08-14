package com.shariffproductions.articlesearch;

public class NewsArticle {
    public String headline;
    public String imageUrl;

    public NewsArticle(String headline, String imageUrl) {
        this.headline = headline;
        this.imageUrl = "http://www.nytimes.com/" + imageUrl;
    }
}
