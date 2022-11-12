package com.example.palexis3.newssum.models.news_api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewsApiNewsSource(
    val id: String?,
    val name: String?,
    val description: String?,
    val url: String?,
    val category: String?,
    val language: String?,
    val country: String?
)

data class NewsApiNewsSourcesResponse(
    val status: String,
    val sources: List<NewsApiNewsSource>
)
