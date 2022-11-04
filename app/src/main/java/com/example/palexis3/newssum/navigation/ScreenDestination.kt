package com.example.palexis3.newssum.navigation

interface ScreenDestination {
    val route: String
}

object HomeScreen : ScreenDestination {
    override val route = "Home"
}
