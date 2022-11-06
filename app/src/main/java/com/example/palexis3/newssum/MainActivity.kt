package com.example.palexis3.newssum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.palexis3.newssum.composable.HomeScreen
import com.example.palexis3.newssum.navigation.HomeScreen
import com.example.palexis3.newssum.theme.AppTheme
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
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = HomeScreen.route) {
        composable(route = HomeScreen.route) {
            HomeScreen()
        }
    }
}
