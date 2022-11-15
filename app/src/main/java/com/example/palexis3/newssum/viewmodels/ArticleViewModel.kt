package com.example.palexis3.newssum.viewmodels

import androidx.compose.runtime.mutableStateOf
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.example.palexis3.newssum.models.news_api.NewsApiArticle
import com.example.palexis3.newssum.models.news_data.NewsDataArticle
import com.example.palexis3.newssum.repository.article.ArticleRepository
import com.example.palexis3.newssum.state.ArticlesState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ArticleViewModel @AssistedInject constructor(
    private val articleRepository: ArticleRepository,
    @Assisted initialState: ArticlesState
) : MavericksViewModel<ArticlesState>(initialState) {

    var currentNewsApiArticle = mutableStateOf<NewsApiArticle?>(null)
        private set

    var currentNewsDataArticle = mutableStateOf<NewsDataArticle?>(null)
        private set

    fun getNewsApiArticles(
        country: String? = null,
        category: String? = null,
        keyword: String? = null,
        sources: String? = null
    ) {
        articleRepository
            .getNewsApiArticles(category, keyword, sources, country)
            .execute { copy(newsApiArticles = it) }
    }

    fun getNewsDataArticles(
        country: String? = null,
        language: String? = null,
        category: String? = null,
        domain: String? = null
    ) {
        articleRepository
            .getNewsDataArticles(country, category, language, domain)
            .execute { copy(newsDataArticles = it) }
    }

    fun search(
        keyword: String? = null,
        sortBy: String? = null,
        language: String? = null
    ) {
        articleRepository
            .getEverything(keyword, sortBy, language)
            .execute { copy(searchedArticles = it) }
    }

    fun resetSearch() {
        setState { copy(searchedArticles = Uninitialized) }
    }

    fun setCurrentNewsApiArticle(newsApiArticle: NewsApiArticle) {
        this.currentNewsApiArticle.value = newsApiArticle
    }

    fun setCurrentNewsDataArticle(newsDataArticle: NewsDataArticle) {
        this.currentNewsDataArticle.value = newsDataArticle
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<ArticleViewModel, ArticlesState> {
        override fun create(state: ArticlesState): ArticleViewModel
    }

    companion object : MavericksViewModelFactory<ArticleViewModel, ArticlesState> by hiltMavericksViewModelFactory()
}
