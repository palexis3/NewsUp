package com.example.palexis3.newssum.viewmodels

import com.airbnb.mvrx.MavericksViewModel
import com.example.palexis3.newssum.repository.ArticleRepository
import com.example.palexis3.newssum.state.ArticlesState
import javax.inject.Inject

// TODO: Add MaverickViewModelFactory with initialState passed in via AssitedInject
class ArticleViewModel @Inject constructor(
    initialState: ArticlesState,
    private val articleRepository: ArticleRepository
) : MavericksViewModel<ArticlesState>(initialState) {

    fun getHeadlines(category: String? = null, keyword: String? = null, sources: String? = null) {
        articleRepository
            .getHeadlines(category, keyword, sources)
            .execute { copy(articles = it) }
    }
}
