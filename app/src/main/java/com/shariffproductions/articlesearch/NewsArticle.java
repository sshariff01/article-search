package com.shariffproductions.articlesearch;

public class NewsArticle {
    public String headline;
    public String imageUrl;

    public NewsArticle(String headline, String imageUrl) {
        this.headline = headline;
        this.imageUrl = setImageUrl(imageUrl);
    }

    private String setImageUrl(String imageUrl) {
        if (imageUrl == null) {
            return null;
        }
        return "http://www.nytimes.com/" + imageUrl;
    }
}
