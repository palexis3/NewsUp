package com.example.palexis3.newssum.repository.source

import com.example.palexis3.newssum.models.NewsSource
import com.example.palexis3.newssum.networking.NewsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NewsSourcesRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi
) : NewsSourcesRepository {

    override fun getNewsSources(
        category: String?,
        language: String?,
        country: String?
    ): Flow<List<NewsSource>> =
        flow {
            val response = newsApi.getNewsSources(category, language, country)
            val items = if (response.status == "ok") {
                response.sources
            } else {
                listOf()
            }
            emit(items)
        }
}
