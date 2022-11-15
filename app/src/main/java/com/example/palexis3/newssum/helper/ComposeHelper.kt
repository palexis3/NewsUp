package com.example.palexis3.newssum.helper

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Getting the width of the current screen and getting the 1/8 value
// is to ensure that the top row consisting of the title and search icon
// are equally spaced and aligned correctly
@Composable
fun EmptyScreenWidth(): Dp {
    val configuration = LocalConfiguration.current
    return configuration.screenWidthDp.dp / 8
}
