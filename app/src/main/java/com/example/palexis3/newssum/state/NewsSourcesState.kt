package com.example.palexis3.newssum.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.example.palexis3.newssum.models.news_api.NewsApiNewsSource

data class NewsSourcesState(
    val newsApiNewsSources: Async<List<NewsApiNewsSource>> = Uninitialized
) : MavericksState
