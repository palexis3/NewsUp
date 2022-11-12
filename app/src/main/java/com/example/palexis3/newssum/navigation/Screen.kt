package com.example.palexis3.newssum.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.palexis3.newssum.R

sealed class Screen {
    abstract val route: String
    abstract val title: Int

    object Headlines : Screen() {
        override val route: String = "Headlines"
        override val title: Int = R.string.headlines
    }

    object NewsApiArticleDetails : Screen() {
        override val route: String = "NewsApiArticleDetails"
        override val title: Int = R.string.news_api_article_details
    }

    object NewsDataArticleDetails : Screen() {
        override val route: String = "NewsDataArticleDetails"
        override val title: Int = R.string.news_data_article_details
    }

    object NewsSources : Screen() {
        override val route: String = "NewsSources"
        override val title: Int = R.string.news_sources
    }

    object WebView : Screen() {
        override val route: String = "WebView"
        override val title: Int = R.string.webview
        const val webUrlArg = "web_url"
        val routeWithArgs = "$route/{$webUrlArg}"
        val arguments = listOf(
            navArgument(webUrlArg) { type = NavType.StringType }
        )
    }

    object NewsSourceDetails : Screen() {
        override val route: String = "NewsSourceDetails"
        override val title: Int = R.string.news_sources_details
    }
}

val bottomNavItems = listOf(
    Screen.Headlines,
    Screen.NewsSources
)
