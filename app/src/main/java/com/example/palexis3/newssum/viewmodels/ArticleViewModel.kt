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
    private val articleRepository: ArticleRepository,
    @Assisted initialState: ArticlesState
) : MavericksViewModel<ArticlesState>(initialState) {

    fun getHeadlines(
        category: String? = "business",
        keyword: String? = null,
        sources: String? = null,
        country: String? = "us"
    ) {
        articleRepository
            .getHeadlines(category, keyword, sources, country)
            .execute { copy(articles = it) }
    }

    fun getEverything(keyword: String? = null, sortBy: String? = null) {
        articleRepository
            .getEverything(keyword, sortBy)
            .execute { copy(articles = it) }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<ArticleViewModel, ArticlesState> {
        override fun create(state: ArticlesState): ArticleViewModel
    }

    companion object : MavericksViewModelFactory<ArticleViewModel, ArticlesState> by hiltMavericksViewModelFactory()
}
