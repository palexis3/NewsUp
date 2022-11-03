package com.example.palexis3.newssum.repository

import com.example.palexis3.newssum.models.Article
import com.example.palexis3.newssum.networking.NewsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// TODO: Bind ArticleRepositoryImpl to ArticleRepository interface in Hilt module
class ArticleRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi
) : ArticleRepository {

    override fun getHeadlines(
        category: String?,
        keyword: String?,
        sources: String?
    ): Flow<List<Article>> =
        newsApi.getHeadlines(category, keyword, sources)
            .map { response ->
                if (response.status == "ok") {
                    response.articles
                } else {
                    listOf()
                }
            }

    override fun getEverything(
        keyword: String?,
        sortBy: String?
    ): Flow<List<Article>> =
        newsApi.getEverything(keyword, sortBy)
            .map { response ->
                if (response.status == "ok") {
                    response.articles
                } else {
                    listOf()
                }
            }
}
