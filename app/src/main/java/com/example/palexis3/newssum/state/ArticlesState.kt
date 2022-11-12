package com.example.palexis3.newssum.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.example.palexis3.newssum.models.news_api.NewsApiArticle
import com.example.palexis3.newssum.models.news_data.NewsDataArticle

/**
 * newsApiArticles is what will be used to show in the main headline screen. This state will be used
 * by the `getHeadlines()` and `getEverything()` screens
 *
 * newsDataArticles is used to fetch articles when a user goes to the news sources screen and since
 * we're using a different API, we'll need to have this state encapsulated.
 */
data class ArticlesState(
    val newsApiArticles: Async<List<NewsApiArticle>> = Uninitialized,
    val newsDataArticles: Async<List<NewsDataArticle>> = Uninitialized
) : MavericksState
