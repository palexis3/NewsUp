package com.example.palexis3.newssum.networking

import com.example.palexis3.newssum.models.HeadlinesResponse
import com.example.palexis3.newssum.models.SourcesResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("/v2/everything")
    fun getEverything(
        @Query("q") keyword: String?,
        @Query("sortBy") sortBy: String?
    ): Flow<HeadlinesResponse>

    @GET("/v2/top-headlines/sources")
    fun getSources(@Query("category") category: String?): Flow<SourcesResponse>

    @GET("/v2/top-headlines")
    fun getHeadlines(
        @Query("category") category: String?,
        @Query("sources") sources: String?,
        @Query("q") keyword: String?
    ): Flow<HeadlinesResponse>
}
