package com.example.palexis3.newssum.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.example.palexis3.newssum.models.Article

/**
 * ArticleState is what will be used to show in the compose screens. And notice in the codebase
 * that this state will be used by the `getHeadlines()` and `getEverything()` screens
 */
data class ArticlesState(
    val articles: Async<List<Article>> = Uninitialized
) : MavericksState
