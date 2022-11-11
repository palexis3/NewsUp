package com.example.palexis3.newssum.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewsSource(
    val id: String?,
    val name: String?,
    val description: String?,
    val url: String?,
    val category: String?,
    val language: String?,
    val country: String?
)

data class NewsSourcesResponse(
    val status: String,
    val sources: List<NewsSource>
)
