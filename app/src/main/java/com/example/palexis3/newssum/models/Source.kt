package com.example.palexis3.newssum.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Source(
    val id: String?,
    val name: String?,
    val description: String?,
    val url: String?,
    val category: String?,
    val language: String?,
    val country: String?
)

data class SourcesResponse(
    val status: String,
    val sources: List<Source>
)
