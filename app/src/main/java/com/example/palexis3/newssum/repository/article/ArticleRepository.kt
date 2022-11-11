package com.example.palexis3.newssum.repository.article

import com.example.palexis3.newssum.models.news_api.NewsApiArticle
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
    fun getArticles(
        category: String?,
        keyword: String?,
        sources: String?,
        country: String?
    ): Flow<List<NewsApiArticle>>

    fun getEverything(keyword: String?, sortBy: String?): Flow<List<NewsApiArticle>>
}
