package com.example.palexis3.newssum.repository.source

import com.example.palexis3.newssum.models.Source
import com.example.palexis3.newssum.networking.NewsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SourceRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi
) : SourceRepository {

    override fun getSources(
        category: String?,
        language: String?
    ): Flow<List<Source>> =
        newsApi
            .getSources(category, language)
            .map { response ->
                if (response.status == "ok") {
                    response.items
                } else {
                    listOf()
                }
            }
}
