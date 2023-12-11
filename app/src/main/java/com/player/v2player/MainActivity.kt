package com.player.v2player

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.player.v2player.presentation.homescreen.HomeScreen
import com.player.v2player.presentation.homescreen.viewmodel.HomeViewModel
import com.player.v2player.presentation.utils.PermissionHandler
import com.player.v2player.ui.theme.V2PlayerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private val viewModel:HomeViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PermissionHandler.initialize(this)
        viewModel.onPermissionResult(this)
        setContent {
            V2PlayerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = Color.White
                ) {
                    HomeScreen(navController = NavHostController(this),viewModel)
                }
            }
        }
    }
}