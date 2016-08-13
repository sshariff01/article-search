package com.shariffproductions.articlesearch;

public class NewsArticle {
    public String title;
    public String imageUrl;

    public NewsArticle(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = "http://www.nytimes.com/" + imageUrl;
    }
}
