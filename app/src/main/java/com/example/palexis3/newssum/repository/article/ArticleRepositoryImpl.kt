package com.example.palexis3.newssum.repository.article

import com.example.palexis3.newssum.models.Article
import com.example.palexis3.newssum.networking.NewsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi
) : ArticleRepository {

    override fun getArticles(
        category: String?,
        keyword: String?,
        sources: String?,
        country: String?
    ): Flow<List<Article>> =
        flow {
            val response = newsApi.getArticles(category, keyword, sources, country)
            val items = if (response.status == "ok") {
                response.articles
            } else {
                listOf()
            }
            emit(items)
        }

    override fun getEverything(
        keyword: String?,
        sortBy: String?
    ): Flow<List<Article>> =
        flow {
            val response = newsApi.getEverything(keyword, sortBy)
            val items = if (response.status == "ok") {
                response.articles
            } else {
                listOf()
            }
            emit(items)
        }
}
