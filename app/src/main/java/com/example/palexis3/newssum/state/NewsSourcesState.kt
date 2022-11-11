package com.example.palexis3.newssum.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.example.palexis3.newssum.models.NewsSource

data class NewsSourcesState(
    val newsSources: Async<List<NewsSource>> = Uninitialized
) : MavericksState
