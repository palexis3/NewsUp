package com.example.palexis3.newssum.repository.article

import com.example.palexis3.newssum.models.Article
import com.example.palexis3.newssum.networking.NewsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

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
