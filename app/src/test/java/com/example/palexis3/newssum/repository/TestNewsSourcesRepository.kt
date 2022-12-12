package com.example.palexis3.newssum.repository

import com.example.palexis3.newssum.models.news_data.NewsDataNewsSource
import com.example.palexis3.newssum.repository.news_sources.NewsSourcesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class TestNewsSourcesRepository : NewsSourcesRepository {

    private val newsSourcesFlow: MutableSharedFlow<List<NewsDataNewsSource>> = MutableSharedFlow(replay = 1)

    override fun getNewsSources(
        category: String?,
        language: String?,
        country: String?
    ): Flow<List<NewsDataNewsSource>> = newsSourcesFlow

    fun sendNewsSources(list: List<NewsDataNewsSource>) {
        newsSourcesFlow.tryEmit(list)
    }
}
