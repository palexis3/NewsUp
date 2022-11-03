package com.example.palexis3.newssum.repository

import com.example.palexis3.newssum.models.Article
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
    fun getHeadlines(category: String?, keyword: String?, sources: String?): Flow<List<Article>>
    fun getEverything(keyword: String?, sortBy: String?): Flow<List<Article>>
}
