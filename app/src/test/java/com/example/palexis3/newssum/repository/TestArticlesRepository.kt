package com.example.palexis3.newssum.repository

import com.example.palexis3.newssum.models.news_api.NewsApiArticle
import com.example.palexis3.newssum.models.news_data.NewsDataArticle
import com.example.palexis3.newssum.repository.article.ArticleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class TestArticlesRepository : ArticleRepository {

    private val newsApiFlow: MutableSharedFlow<List<NewsApiArticle>> = MutableSharedFlow(replay = 1)
    private val newsDataFlow: MutableSharedFlow<List<NewsDataArticle>> = MutableSharedFlow(replay = 1)

    override fun getNewsApiArticles(
        category: String?,
        keyword: String?,
        sources: String?,
        country: String?,
        language: String?
    ): Flow<List<NewsApiArticle>> = newsApiFlow

    override fun getNewsDataArticles(
        country: String?,
        category: String?,
        language: String?,
        domain: String?
    ): Flow<List<NewsDataArticle>> = newsDataFlow

    override fun getEverything(
        keyword: String?,
        sortBy: String?,
        language: String?
    ): Flow<List<NewsApiArticle>> = newsApiFlow

    fun sendNewsApi(list: List<NewsApiArticle>) {
        newsApiFlow.tryEmit(list)
    }

    fun sendNewsData(list: List<NewsDataArticle>) {
        newsDataFlow.tryEmit(list)
    }
}
