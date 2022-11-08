package com.example.palexis3.newssum.navigation

import androidx.annotation.StringRes
import com.example.palexis3.newssum.R

sealed class Screen(val route: String, @StringRes val title: Int) {
    object Headlines : Screen("Headlines", R.string.headlines)
    object ArticleDetails : Screen("ArticleDetails", R.string.article_details)
    object NewsSources : Screen("NewsSources", R.string.news_sources)
}

val bottomNavItems = listOf(
    Screen.Headlines,
    Screen.NewsSources
)
