package com.example.palexis3.newssum.models.news_api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewsApiArticle(
    val author: String?,
    val source: SourceObj?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?
)

@JsonClass(generateAdapter = true)
data class SourceObj(
    val id: String?,
    val name: String?
)

data class NewsApiArticlesResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<NewsApiArticle>
)
