package com.example.palexis3.newssum.viewmodels

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.example.palexis3.newssum.repository.source.SourceRepository
import com.example.palexis3.newssum.state.SourcesState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class SourceViewModel @AssistedInject constructor(
    @Assisted initialState: SourcesState,
    private val sourceRepository: SourceRepository
) : MavericksViewModel<SourcesState>(initialState) {

    fun getSources(category: String? = null, language: String? = null) {
        sourceRepository
            .getSources(category, language)
            .execute { copy(sources = it) }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<SourceViewModel, SourcesState> {
        override fun create(state: SourcesState): SourceViewModel
    }

    companion object : MavericksViewModelFactory<SourceViewModel, SourcesState> by hiltMavericksViewModelFactory()
}
