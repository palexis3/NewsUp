package com.example.palexis3.newssum.models.news_data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewsDataNewsSource(
    val id: String?,
    val name: String?,
    val url: String?,
    val category: List<String>?,
    val language: List<String>?,
    val country: List<String>?
)

data class NewsDataNewsSourcesResponse(
    val status: String,
    val results: List<NewsDataNewsSource>
)
