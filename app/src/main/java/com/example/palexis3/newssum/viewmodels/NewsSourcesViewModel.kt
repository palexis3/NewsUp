package com.example.palexis3.newssum.viewmodels

import androidx.compose.runtime.mutableStateOf
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.example.palexis3.newssum.models.NewsSource
import com.example.palexis3.newssum.repository.source.NewsSourcesRepository
import com.example.palexis3.newssum.state.NewsSourcesState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class NewsSourcesViewModel @AssistedInject constructor(
    private val sourceRepository: NewsSourcesRepository,
    @Assisted initialState: NewsSourcesState
) : MavericksViewModel<NewsSourcesState>(initialState) {

    var currentNewsSource = mutableStateOf<NewsSource?>(null)
        private set

    fun getNewsSources(
        category: String? = null,
        language: String? = "en",
        country: String? = "us"
    ) {
        sourceRepository
            .getNewsSources(category, language, country)
            .execute { copy(newsSources = it) }
    }

    fun setCurrentNewsSource(newsSource: NewsSource) {
        this.currentNewsSource.value = newsSource
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<NewsSourcesViewModel, NewsSourcesState> {
        override fun create(state: NewsSourcesState): NewsSourcesViewModel
    }

    companion object : MavericksViewModelFactory<NewsSourcesViewModel, NewsSourcesState> by hiltMavericksViewModelFactory()
}
