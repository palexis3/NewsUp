package com.example.palexis3.newssum.repository.source

import com.example.palexis3.newssum.models.Source
import kotlinx.coroutines.flow.Flow

interface SourceRepository {
    fun getSources(category: String?, language: String?, country: String?): Flow<List<Source>>
}
