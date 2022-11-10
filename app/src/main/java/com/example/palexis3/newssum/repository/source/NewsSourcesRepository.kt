package com.example.palexis3.newssum.repository.source

import com.example.palexis3.newssum.models.NewsSource
import kotlinx.coroutines.flow.Flow

interface NewsSourcesRepository {
    fun getNewsSources(category: String?, language: String?, country: String?): Flow<List<NewsSource>>
}
