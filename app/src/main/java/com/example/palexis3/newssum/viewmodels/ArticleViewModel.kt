package com.example.palexis3.newssum.viewmodels

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.example.palexis3.newssum.repository.article.ArticleRepository
import com.example.palexis3.newssum.state.ArticlesState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ArticleViewModel @AssistedInject constructor(
    @Assisted initialState: ArticlesState,
    private val articleRepository: ArticleRepository
) : MavericksViewModel<ArticlesState>(initialState) {

    fun getHeadlines(category: String? = null, keyword: String? = null, sources: String? = null) {
        articleRepository
            .getHeadlines(category, keyword, sources)
            .execute { copy(articles = it) }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<ArticleViewModel, ArticlesState> {
        override fun create(state: ArticlesState): ArticleViewModel
    }

    companion object : MavericksViewModelFactory<ArticleViewModel, ArticlesState> by hiltMavericksViewModelFactory()
}
