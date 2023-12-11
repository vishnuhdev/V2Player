package com.player.v2player.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.player.v2player.presentation.filescreen.FileScreen
import com.player.v2player.presentation.homescreen.HomeScreen
import com.player.v2player.presentation.videoscreen.VideoPlayerScreen

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
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

