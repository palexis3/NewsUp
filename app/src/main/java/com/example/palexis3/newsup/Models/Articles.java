package com.example.palexis3.newsup.Models;


import org.parceler.Parcel;

@Parcel
public class Articles {

    String source;
    String author;
    String title;
    String description;
    String url;
    String urlToImage;
    String publishedAt;

    // empty constructor used for parceler library
    public Articles(){};

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getPublishedAt() { return publishedAt; }

    public String getSource() {
        return source;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

}
