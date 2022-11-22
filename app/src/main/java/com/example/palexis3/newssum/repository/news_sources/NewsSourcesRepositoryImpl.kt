package com.example.palexis3.newssum.repository.news_sources

import com.example.palexis3.newssum.models.news_data.NewsDataNewsSource
import com.example.palexis3.newssum.networking.NewsData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NewsSourcesRepositoryImpl @Inject constructor(
    private val newsData: NewsData
) : NewsSourcesRepository {

    override fun getNewsSources(
        category: String?,
        language: String?,
        country: String?
    ): Flow<List<NewsDataNewsSource>> =
        flow {
            val response = newsData.getNewsSources(country, category, language)
            val items = if (response.status == "success") {
                response.results
            } else {
                listOf()
            }
            emit(items)
        }
}
