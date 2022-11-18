package com.example.palexis3.newssum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.airbnb.mvrx.compose.mavericksViewModel
import com.example.palexis3.newssum.composable.HeadlineScreen
import com.example.palexis3.newssum.composable.SearchView
import com.example.palexis3.newssum.composable.WebViewScreen
import com.example.palexis3.newssum.composable.articles.NewsApiArticleDetailsScreen
import com.example.palexis3.newssum.composable.articles.NewsDataArticleDetailsScreen
import com.example.palexis3.newssum.composable.news_sources.NewsSourceDetailsScreen
import com.example.palexis3.newssum.composable.news_sources.NewsSourcesScreen
import com.example.palexis3.newssum.composable.preferences.PreferencesScreen
import com.example.palexis3.newssum.navigation.Screen
import com.example.palexis3.newssum.navigation.navigateSingleTopTo
import com.example.palexis3.newssum.navigation.navigateToWebView
import com.example.palexis3.newssum.theme.AppTheme
import com.example.palexis3.newssum.viewmodels.ArticleViewModel
import com.example.palexis3.newssum.viewmodels.NewsSourcesViewModel
import com.example.palexis3.newssum.viewmodels.PreferencesViewModel
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
    val preferencesViewModel: PreferencesViewModel = hiltViewModel()

    val myAppState = rememberMyAppState()

    Scaffold(
        bottomBar = {
            if (myAppState.shouldShowBottomBar) {
                BottomBar(
                    bottomNavScreens = myAppState.bottomNavScreens,
                    currentRoute = myAppState.currentRoute!!,
                    navigateToScreen = myAppState::navigateToScreen
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = myAppState.navController,
            startDestination = Screen.Headlines.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Screen.Headlines.route) {
                HeadlineScreen(
                    articleViewModel = articleViewModel,
                    preferencesViewModel = preferencesViewModel,
                    goToNewsApiArticleDetailsScreen = {
                        myAppState.navigateToScreen(Screen.NewsApiArticleDetails.route)
                    },
                    goToSearchView = {
                        myAppState.navigateToScreen(Screen.SearchView.route)
                    }
                )
            }

            composable(route = Screen.NewsApiArticleDetails.route) {
                NewsApiArticleDetailsScreen(
                    articleViewModel = articleViewModel,
                    closeScreen = {
                        myAppState.popBackStack()
                    },
                    goToWebView = { url ->
                        myAppState.navigateToWebView(url)
                    }
                )
            }

            composable(route = Screen.NewsDataArticleDetails.route) {
                NewsDataArticleDetailsScreen(
                    articleViewModel = articleViewModel,
                    closeScreen = {
                        myAppState.popBackStack()
                    },
                    goToWebView = { url ->
                        myAppState.navigateToWebView(url)
                    }
                )
            }

            composable(route = Screen.NewsSources.route) {
                NewsSourcesScreen(
                    newsSourcesViewModel = newsSourcesViewModel,
                    preferencesViewModel = preferencesViewModel,
                    goToNewsSourcesDetailsScreen = {
                        myAppState.navigateToScreen(Screen.NewsSourceDetails.route)
                    },
                    goToSearchView = {
                        myAppState.navigateToScreen(Screen.SearchView.route)
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
                        closeScreen = { myAppState.popBackStack() }
                    )
                }
            }

            composable(route = Screen.NewsSourceDetails.route) {
                NewsSourceDetailsScreen(
                    closeScreen = { myAppState.popBackStack() },
                    newsSourcesViewModel = newsSourcesViewModel,
                    preferencesViewModel = preferencesViewModel,
                    articleViewModel = articleViewModel,
                    goToNewsDataArticleDetailsScreen = { myAppState.navigateToScreen(Screen.NewsDataArticleDetails.route) },
                    goToWebView = { url -> myAppState.navigateToWebView(url) }
                )
            }

            composable(route = Screen.SearchView.route) {
                SearchView(
                    articleViewModel = articleViewModel,
                    preferencesViewModel = preferencesViewModel,
                    goToNewsApiArticleDetailsScreen = {
                        myAppState.navigateToScreen(Screen.NewsApiArticleDetails.route)
                    },
                    closeScreen = { myAppState.popBackStack() }
                )
            }

            composable(route = Screen.Preferences.route) {
                PreferencesScreen()
            }
        }
    }
}

@Composable
fun rememberMyAppState(
    navController: NavHostController = rememberNavController()
) = remember(navController) { AppState(navController) }

class AppState(val navController: NavHostController) {

    val bottomNavScreens = listOf(
        Screen.Headlines,
        Screen.NewsSources,
        Screen.Preferences
    )

    private val bottomNavRoutes = bottomNavScreens.map { it.route }

    val currentRoute: String?
        get() = navController.currentDestination?.route

    val shouldShowBottomBar: Boolean
        @Composable get() =
            navController.currentBackStackEntryAsState().value?.destination?.route in bottomNavRoutes

    fun popBackStack() {
        navController.popBackStack()
    }

    fun navigateToScreen(route: String) {
        if (currentRoute != route) {
            navController.navigateSingleTopTo(route)
        }
    }

    fun navigateToWebView(url: String) = navController.navigateToWebView(url)
}

@Composable
fun BottomBar(
    bottomNavScreens: List<Screen>,
    currentRoute: String,
    navigateToScreen: (String) -> Unit,
) {
    NavigationBar {
        bottomNavScreens.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = { navigateToScreen(screen.route) },
                label = { Text(stringResource(id = screen.title)) },
                icon = {}
            )
        }
    }
}
