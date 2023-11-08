package com.player.v2player.views

import android.content.ContentResolver
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.player.v2player.R
import com.player.v2player.constants.AppConstants
import com.player.v2player.navigation.Routes
import com.player.v2player.viewModels.HomeViewModel

@Composable
fun HomeScreen(navController: NavHostController) {

    val viewModel = HomeViewModel()
    val contentResolver: ContentResolver = LocalContext.current.contentResolver
    val videos = viewModel.getAllVideos(contentResolver)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Text(
            text = AppConstants.AppName,
            fontSize = 16.sp,
            color = Color.Black,
            fontFamily = FontFamily(Font(R.font.raleway_bold_italic))
        )
        LazyColumn(Modifier.fillMaxSize()) {
            videos.forEach { (folder, videos) ->
                item {
                    TextButton(onClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.apply {
                            set(AppConstants.BulkUris, videos)
                        }
                        navController.navigate(Routes.FileScreen.route) {
                            popUpTo(Routes.FileScreen.route) {
                                inclusive = true
                            }
                        }
                    }) {
                        Text(text = "Folder: $folder", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }

                }
            }
        }


    }
}







