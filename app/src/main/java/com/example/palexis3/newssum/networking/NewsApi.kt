package com.example.palexis3.newssum.networking

import com.example.palexis3.newssum.models.news_api.NewsApiArticlesResponse
import com.example.palexis3.newssum.models.news_api.NewsApiNewsSourcesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("/v2/everything")
    suspend fun getEverything(
        @Query("q") keyword: String?,
        @Query("sortBy") sortBy: String?
    ): NewsApiArticlesResponse

    @GET("/v2/top-headlines/sources")
    suspend fun getNewsSources(
        @Query("category") category: String?,
        @Query("language") language: String?,
        @Query("country") country: String?
    ): NewsApiNewsSourcesResponse

    @GET("/v2/top-headlines")
    suspend fun getArticles(
        @Query("category") category: String?,
        @Query("sources") sources: String?,
        @Query("q") keyword: String?,
        @Query("country") country: String?
    ): NewsApiArticlesResponse
}
