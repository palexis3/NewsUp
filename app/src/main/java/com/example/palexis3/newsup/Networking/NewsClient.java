package com.example.palexis3.newsup.Networking;


import com.example.palexis3.newsup.Responses.NewsArticleResponse;
import com.example.palexis3.newsup.Responses.NewsSourceResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsClient {

        @GET("/v1/sources")
        Call<NewsSourceResponse> getSources();

        @GET("/v1/articles")
        Call<NewsArticleResponse> getArticles(
                @Query("source") String source,
                @Query("apiKey") String key
        );

}
