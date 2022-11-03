package com.example.palexis3.newssum.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Article(
    val source: SourceId,
    val author: String,
    val title: String,
    val description: String,
    val urlToImage: String,
    val publishedAt: String,
    val content: String
)

data class HeadlinesResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)
