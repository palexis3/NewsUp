package com.example.palexis3.newssum.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.example.palexis3.newssum.models.Source

data class SourcesState(
    val sources: Async<List<Source>> = Uninitialized
) : MavericksState
