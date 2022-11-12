package com.example.palexis3.newssum.repository.source

import android.util.Log
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
            Log.d("XXX-SourcesRepository", "country: $country category: $category language: $language response: $response")
            val items = if (response.status == "success") {
                response.results
            } else {
                listOf()
            }
            emit(items)
        }
}
