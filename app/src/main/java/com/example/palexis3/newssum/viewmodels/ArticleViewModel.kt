package com.example.palexis3.newssum.viewmodels

import com.airbnb.mvrx.MavericksViewModel
import com.example.palexis3.newssum.repository.ArticleRepository
import com.example.palexis3.newssum.state.ArticlesState
import javax.inject.Inject

class ArticleViewModel @Inject constructor(
    initialState: ArticlesState,
    private val articleRepository: ArticleRepository
) : MavericksViewModel<ArticlesState>(initialState) {

    fun getHeadlines(
        category: String?,
        keyword: String?,
        sources: String?
    ) {
        articleRepository.getHeadlines(category, keyword, sources).execute { copy(articles = it) }
    }
}
