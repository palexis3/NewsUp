package com.example.palexis3.newssum.navigation

sealed class ScreenDestinations(val route: String) {
    object Headlines : ScreenDestinations("Headlines")
    object ArticleDetails : ScreenDestinations("ArticleDetails")
    object NewsSources : ScreenDestinations("NewsSources")
}
