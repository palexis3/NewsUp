package com.example.palexis3.newssum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.airbnb.mvrx.compose.mavericksViewModel
import com.example.palexis3.newssum.navigation.Screen
import com.example.palexis3.newssum.navigation.bottomNavItems
import com.example.palexis3.newssum.navigation.navigateSingleTopTo
import com.example.palexis3.newssum.navigation.navigateToWebView
import com.example.palexis3.newssum.theme.AppTheme
import com.example.palexis3.newssum.viewmodels.ArticleViewModel
import com.example.palexis3.newssum.viewmodels.NewsSourcesViewModel
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ShowNewsApp() {
    /**
     * Creating shared viewmodels to set single article and source objects because
     * the NewsApi doesn't support fetching objects via an id.
     */
    val articleViewModel: ArticleViewModel = mavericksViewModel()
    val newsSourcesViewModel: NewsSourcesViewModel = mavericksViewModel()

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val bottomBarVisible = rememberSaveable { mutableStateOf(true) }
    when (navBackStackEntry?.destination?.route) {
        Screen.WebView.routeWithArgs -> {
            bottomBarVisible.value = false
        }
        Screen.ArticleDetails.route -> {
            bottomBarVisible.value = false
        }
        Screen.NewsSourceDetails.route -> {
            bottomBarVisible.value = false
        }
        else -> {
            bottomBarVisible.value = true
        }
    }

    Scaffold(
        bottomBar = {
            if (bottomBarVisible.value) {
                NavigationBar {
                    val currentDestination = navBackStackEntry?.destination
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigateSingleTopTo(screen.route)
                            },
                            label = { Text(stringResource(id = screen.title)) },
                            icon = {}
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Headlines.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Screen.Headlines.route) {
                HeadlineScreen(
                    articleViewModel = articleViewModel,
                    goToArticleDetailsScreen = {
                        navController.navigateSingleTopTo(Screen.ArticleDetails.route)
                    }
                )
            }

            composable(route = Screen.ArticleDetails.route) {
                ArticleDetailsScreen(
                    articleViewModel = articleViewModel,
                    closeScreen = {
                        navController.popBackStack()
                    },
                    goToWebView = { url ->
                        navController.navigateToWebView(url)
                    }
                )
            }

            composable(route = Screen.NewsSources.route) {
                NewsSourcesScreen(
                    newsSourcesViewModel = newsSourcesViewModel,
                    goToNewsSourcesDetailsScreen = {
                        navController.navigateSingleTopTo(Screen.NewsSourceDetails.route)
                    }
                )
            }

            composable(
                route = Screen.WebView.routeWithArgs,
                arguments = Screen.WebView.arguments
            ) { navBackStackEntry ->
                val webUrl = navBackStackEntry.arguments?.getString(Screen.WebView.webUrlArg)
                if (webUrl != null) {
                    WebViewScreen(
                        url = webUrl,
                        closeScreen = { navController.popBackStack() }
                    )
                }
            }

            composable(route = Screen.NewsSourceDetails.route) {
                NewsSourceDetailsScreen(
                    closeScreen = { navController.popBackStack() },
                    newsSourcesViewModel = newsSourcesViewModel,
                    articleViewModel = articleViewModel,
                    goToArticleDetailsScreen = { navController.navigateSingleTopTo(Screen.ArticleDetails.route) },
                    goToWebView = { url -> navController.navigateToWebView(url) }
                )
            }
        }
    }
}
