package com.example.palexis3.newssum.repository.article

import com.example.palexis3.newssum.models.news_api.NewsApiArticle
import com.example.palexis3.newssum.models.news_data.NewsDataArticle
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {

    fun getNewsApiArticles(
        category: String?,
        keyword: String?,
        sources: String?,
        country: String?
    ): Flow<List<NewsApiArticle>>

    fun getNewsDataArticles(
        country: String?,
        category: String?,
        language: String?,
        domain: String?
    ): Flow<List<NewsDataArticle>>

    fun getEverything(keyword: String?, sortBy: String?): Flow<List<NewsApiArticle>>
}
