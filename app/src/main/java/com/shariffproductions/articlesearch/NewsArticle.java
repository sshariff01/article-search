package com.shariffproductions.articlesearch;

import android.os.Parcel;
import android.os.Parcelable;

public class NewsArticle implements Parcelable {
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

    protected NewsArticle(Parcel in) {
        headline = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<NewsArticle> CREATOR = new Creator<NewsArticle>() {
        @Override
        public NewsArticle createFromParcel(Parcel in) {
            return new NewsArticle(in);
        }

        @Override
        public NewsArticle[] newArray(int size) {
            return new NewsArticle[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(headline);
        dest.writeString(imageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
