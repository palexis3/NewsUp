package com.example.palexis3.newssum.repository.news_sources

import com.example.palexis3.newssum.models.news_data.NewsDataNewsSource
import kotlinx.coroutines.flow.Flow

interface NewsSourcesRepository {
    fun getNewsSources(category: String?, language: String?, country: String?): Flow<List<NewsDataNewsSource>>
}
