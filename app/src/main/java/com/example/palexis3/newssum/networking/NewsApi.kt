package com.example.palexis3.newssum.networking

import com.example.palexis3.newssum.models.HeadlinesResponse
import com.example.palexis3.newssum.models.SourcesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("/v2/everything")
    suspend fun getEverything(
        @Query("q") keyword: String?,
        @Query("sortBy") sortBy: String?
    ): HeadlinesResponse

    @GET("/v2/top-headlines/sources")
    suspend fun getSources(
        @Query("category") category: String?,
        @Query("language") language: String?,
        @Query("country") country: String?
    ): SourcesResponse

    @GET("/v2/top-headlines")
    suspend fun getHeadlines(
        @Query("category") category: String?,
        @Query("sources") sources: String?,
        @Query("q") keyword: String?,
        @Query("country") country: String?
    ): HeadlinesResponse
}
