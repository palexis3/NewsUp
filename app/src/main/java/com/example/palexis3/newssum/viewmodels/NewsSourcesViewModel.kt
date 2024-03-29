package com.example.palexis3.newssum.viewmodels

import androidx.compose.runtime.mutableStateOf
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.example.palexis3.newssum.models.news_data.NewsDataNewsSource
import com.example.palexis3.newssum.repository.news_sources.NewsSourcesRepository
import com.example.palexis3.newssum.state.NewsSourcesState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class NewsSourcesViewModel @AssistedInject constructor(
    private val newsSourcesRepository: NewsSourcesRepository,
    @Assisted initialState: NewsSourcesState
) : MavericksViewModel<NewsSourcesState>(initialState) {

    var currentNewsDataNewsSource = mutableStateOf<NewsDataNewsSource?>(null)
        private set

    fun getNewsSources(
        category: String? = null,
        language: String? = null,
        country: String? = null
    ) {
        newsSourcesRepository
            .getNewsSources(category, language, country)
            .execute { copy(newsDataNewsSources = it) }
    }

    fun setCurrentNewsSource(newsDataNewsSource: NewsDataNewsSource) {
        this.currentNewsDataNewsSource.value = newsDataNewsSource
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<NewsSourcesViewModel, NewsSourcesState> {
        override fun create(state: NewsSourcesState): NewsSourcesViewModel
    }

    companion object : MavericksViewModelFactory<NewsSourcesViewModel, NewsSourcesState> by hiltMavericksViewModelFactory()
}
