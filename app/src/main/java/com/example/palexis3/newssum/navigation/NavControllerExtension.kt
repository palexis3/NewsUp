package com.example.palexis3.newssum.navigation

import androidx.navigation.NavHostController
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * `navigateSingleTopTo` extension is used to ensure that for each screen we navigate to, there's
 * only a single copy that exists in the backstack to avoid confusing, circular navigation
 */
fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        launchSingleTop = true
        restoreState = true
    }

// Note: Need to encode the web url because navigation routes are already in url format
fun NavHostController.navigateToWebView(url: String) {
    val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
    this.navigateSingleTopTo("${Screen.WebView.route}/$encodedUrl")
}
