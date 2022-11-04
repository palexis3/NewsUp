package com.example.palexis3.newssum.repository.source

import com.example.palexis3.newssum.models.Source
import com.example.palexis3.newssum.networking.NewsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SourceRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi
) : SourceRepository {

    override fun getSources(
        category: String?,
        language: String?,
        country: String?
    ): Flow<List<Source>> =
        flow {
            val response = newsApi.getSources(category, language, country)
            val items = if (response.status == "ok") {
                response.items
            } else {
                listOf()
            }
            emit(items)
        }
}
