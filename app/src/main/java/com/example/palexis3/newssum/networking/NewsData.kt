package com.example.palexis3.newssum.networking

import com.example.palexis3.newssum.models.news_data.NewsDataArticleResponse
import com.example.palexis3.newssum.models.news_data.NewsDataNewsSourcesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsData {

    @GET("news")
    suspend fun getArticles(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("language") language: String,
        @Query("domain") domain: String
    ): NewsDataArticleResponse

    @GET("sources")
    suspend fun getNewsSources(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("language") language: String
    ): NewsDataNewsSourcesResponse
}
