package com.player.v2player.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.player.v2player.views.FileScreen
import com.player.v2player.views.HomeScreen
import com.player.v2player.views.VideoPlayerScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Home.route) {
        composable(Routes.Home.route) {
            HomeScreen(navController)
        }
        composable(Routes.VideoScreen.route) {
            VideoPlayerScreen(navController)
        }
        composable(Routes.FileScreen.route) {
            FileScreen(navController = navController)
        }
    }
}

