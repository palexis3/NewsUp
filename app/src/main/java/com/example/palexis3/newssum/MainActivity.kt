package com.example.palexis3.newssum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
    val context = LocalContext.current

    /**
     * Creating shared viewmodels to set single article and source objects because
     * the NewsApi/NewsData doesn't support fetching single objects via an id and I'll use
     * the viewModels as a state holder.
     */
    val articleViewModel: ArticleViewModel = mavericksViewModel()
    val newsSourcesViewModel: NewsSourcesViewModel = mavericksViewModel()
    val preferencesViewModel: PreferencesViewModel = hiltViewModel()

    var screenTitle by rememberSaveable { mutableStateOf("") }

    val myAppState = rememberMyAppState()

    Scaffold(
        topBar = {
            if (myAppState.isTopBarScreen) {
                TopBar(
                    title = screenTitle,
                    isNavBarScreen = myAppState.isNavBarScreen,
                    navigateToSearchScreen = { myAppState.navigateToSearchView() },
                    closeScreen = { myAppState.popBackStack() }
                )
            }
        },
        bottomBar = {
            if (myAppState.isNavBarScreen) {
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
                    screenTitle = { stringId -> screenTitle = context.getString(stringId) }
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
                    },
                    screenTitle = { title -> screenTitle = title }
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
                    },
                    screenTitle = { title -> screenTitle = title }
                )
            }

            composable(route = Screen.NewsSources.route) {
                NewsSourcesScreen(
                    newsSourcesViewModel = newsSourcesViewModel,
                    preferencesViewModel = preferencesViewModel,
                    goToNewsSourcesDetailsScreen = {
                        myAppState.navigateToScreen(Screen.NewsSourceDetails.route)
                    },
                    screenTitle = { stringId -> screenTitle = context.getString(stringId) }
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
                    goToWebView = { url -> myAppState.navigateToWebView(url) },
                    screenTitle = { title -> screenTitle = title }
                )
            }

            composable(route = Screen.SearchView.route) {
                SearchView(
                    articleViewModel = articleViewModel,
                    preferencesViewModel = preferencesViewModel,
                    goToNewsApiArticleDetailsScreen = {
                        myAppState.navigateToScreen(Screen.NewsApiArticleDetails.route)
                    },
                    closeScreen = myAppState::popBackStack
                )
            }

            composable(route = Screen.Preferences.route) {
                PreferencesScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberMyAppState(
    navController: NavHostController = rememberNavController(),
    topAppBarState: TopAppBarState = rememberTopAppBarState()
) = remember(navController, topAppBarState) {
    AppState(navController, topAppBarState)
}

class AppState @OptIn(ExperimentalMaterial3Api::class) constructor(
    val navController: NavHostController,
    val topAppBarState: TopAppBarState
) {
    val bottomNavScreens = listOf(
        Screen.Headlines,
        Screen.NewsSources,
        Screen.Preferences
    )

    private val nonTopBarRoutes = listOf(
        Screen.SearchView,
        Screen.Preferences
    ).map { it.route }

    private val bottomNavRoutes = bottomNavScreens.map { it.route }

    val currentRoute: String?
        get() = navController.currentDestination?.route

    val isNavBarScreen: Boolean
        @Composable get() =
            navController.currentBackStackEntryAsState().value?.destination?.route in bottomNavRoutes

    val isTopBarScreen: Boolean
        @Composable get() =
            navController.currentBackStackEntryAsState().value?.destination?.route !in nonTopBarRoutes

    fun popBackStack() {
        navController.popBackStack()
    }

    fun navigateToScreen(route: String) {
        if (currentRoute != route) {
            navController.navigateSingleTopTo(route)
        }
    }

    fun navigateToWebView(url: String) = navController.navigateToWebView(url)

    fun navigateToSearchView() = navigateToScreen(Screen.SearchView.route)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    isNavBarScreen: Boolean,
    navigateToSearchScreen: () -> Unit,
    closeScreen: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            // we only want to show back arrow for screens that are not on the bottom nav bar
            // since we have to navigate our way to them
            if (isNavBarScreen.not()) {
                IconButton(onClick = closeScreen) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Go Back"
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = navigateToSearchScreen) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
        }
    )
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
