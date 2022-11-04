package com.example.palexis3.newssum.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Source(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val language: String,
    val country: String
)

@JsonClass(generateAdapter = true)
data class SourceId(
    val id: String,
    val name: String
)

data class SourcesResponse(
    val status: String,
    val items: List<Source>
)