package com.example.palexis3.newssum.models.news_data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewsDataArticle(
    val title: String?,
    val link: String?,
    val source_id: String?,
    val image_url: String?,
    val description: String?,
    val content: String?,
    val pubDate: String?
)

data class NewsDataArticleResponse(
    val status: String,
    val totalResults: Int,
    val results: List<NewsDataArticle>
)
