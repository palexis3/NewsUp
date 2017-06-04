package com.example.palexis3.newsup.Responses;


import com.example.palexis3.newsup.Models.Articles;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class NewsArticleResponse {

    public static final String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    List<Articles> articles;

    // public constructor is necessary for collections
    public NewsArticleResponse() {
        articles = new ArrayList<Articles>();
    }

    public static NewsArticleResponse parseJson(String response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat(ISO_FORMAT);
        Gson gson = gsonBuilder.create();
        NewsArticleResponse articleResponse = gson.fromJson(response, NewsArticleResponse.class);
        return articleResponse;
    }
}
