package com.example.palexis3.newssum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.airbnb.mvrx.compose.mavericksViewModel
import com.example.palexis3.newssum.composable.ArticleDetailsScreen
import com.example.palexis3.newssum.composable.HomeScreen
import com.example.palexis3.newssum.navigation.ArticleDetailsScreen
import com.example.palexis3.newssum.navigation.HomeScreen
import com.example.palexis3.newssum.navigation.navigateSingleTopTo
import com.example.palexis3.newssum.theme.AppTheme
import com.example.palexis3.newssum.viewmodels.ArticleViewModel
import com.example.palexis3.newssum.viewmodels.SourceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                ShowNewsApp()
            }
        }
    }
}

@Preview
@Composable
fun ShowNewsApp() {
    /**
     * Creating shared viewmodels to set single article and source objects because
     * the NewsApi doesn't support fetching objects via an id.
     */
    val articleViewModel: ArticleViewModel = mavericksViewModel()
    val sourceViewModel: SourceViewModel = mavericksViewModel()

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeScreen.route
    ) {
        composable(route = HomeScreen.route) {
            HomeScreen(
                articleViewModel = articleViewModel,
                sourceViewModel = sourceViewModel,
                goToArticleDetailsScreen = {
                    navController.navigateSingleTopTo(ArticleDetailsScreen.route)
                }
            )
        }

        composable(route = ArticleDetailsScreen.route) {
            ArticleDetailsScreen(
                articleViewModel = articleViewModel,
                closeScreen = {
                    navController.popBackStack()
                }
            )
        }
    }
}
