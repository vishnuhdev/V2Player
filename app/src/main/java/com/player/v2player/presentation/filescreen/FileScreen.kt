package com.player.v2player.presentation.filescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavController
import com.player.v2player.R
import com.player.v2player.data.constants.AppConstants
import com.player.v2player.presentation.navigation.Routes
import java.io.File

@Composable
fun FileScreen(navController: NavController) {

    val videoUri =
        navController.previousBackStackEntry?.savedStateHandle?.get<MutableList<String>>(
            AppConstants.BulkUris
        ) ?: mutableListOf()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            itemsIndexed(items = videoUri) { _, video ->
                TextButton(onClick = {
                    navController.currentBackStackEntry?.savedStateHandle?.apply {
                        set(AppConstants.VideoUri, video)
                    }
                    navController.navigate(Routes.VideoScreen.route) {
                        popUpTo(Routes.VideoScreen.route) {
                            inclusive = true
                        }
                    }
                }) {
                    val file = File(video)
                    Text(
                        text = file.name,
                        fontFamily = FontFamily(
                            Font(R.font.raleway_bold)
                        ),
                    )
                }
            }
        }
    }
}