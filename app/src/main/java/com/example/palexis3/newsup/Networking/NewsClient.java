package com.example.palexis3.newsup.Networking;


import com.example.palexis3.newsup.Models.Articles;
import com.example.palexis3.newsup.Responses.NewsSourceResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsClient {

        @GET("/sources")
        Call<NewsSourceResponse> getSources();

        @GET("/articles")
        Call<List<Articles>> getArticles(
                @Query("source") String source,
                @Query("apiKey") String key
        );

}
