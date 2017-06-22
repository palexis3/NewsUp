package com.example.palexis3.newssum.Responses;


import com.example.palexis3.newssum.Models.Articles;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class NewsArticleResponse {

    @SerializedName("articles")
    private List<Articles> articles;

    public static final String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    // public constructor is necessary for collections
    public NewsArticleResponse() { articles = new ArrayList<>(); }

    public List<Articles> getArticles() {
        return articles;
    }

    public static NewsArticleResponse parseJson(String response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat(ISO_FORMAT);
        Gson gson = gsonBuilder.create();
        NewsArticleResponse articleResponse = gson.fromJson(response, NewsArticleResponse.class);
        return articleResponse;
    }
}
