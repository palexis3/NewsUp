package com.example.palexis3.newssum.models.news_data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewsDataNewsSource(
    val id: String?,
    val name: String?,
    val url: String?,
    val category: String?,
    val language: String?,
    val country: String?
)

data class NewsDataNewsSourcesResponse(
    val status: String,
    val results: List<NewsDataNewsSource>
)
