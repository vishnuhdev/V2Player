package com.player.v2player.presentation.filescreen

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.player.v2player.R
import com.player.v2player.data.constants.AppConstants
import com.player.v2player.presentation.navigation.Routes
import com.player.v2player.ui.theme.Purple40
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


                Row(
                    modifier = Modifier.clickable(onClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.apply {
                            set(AppConstants.VideoUri, video)
                        }
                        navController.navigate(Routes.VideoScreen.route) {
                            popUpTo(Routes.VideoScreen.route) {
                                inclusive = true
                            }
                        }
                    }),
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    val file = File(video)
                    val thumbnailBitmap= getVideoThumbnail(LocalContext.current.applicationContext,file.path)

                    if (thumbnailBitmap != null) {
                        Image(
                            painter = BitmapPainter(thumbnailBitmap.asImageBitmap()) ,
                            contentDescription = "Thumbnail",
                            modifier = Modifier
                                .width(140.dp)
                                .height(75.dp)
                                .clip(shape = RoundedCornerShape(10.dp))
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = file.name,
                        color = Purple40,
                        fontFamily = FontFamily(
                            Font(R.font.raleway_bold)
                        ),
                    )
                }
            }
        }
    }
}
private fun getVideoThumbnail(context: Context,videoUri: String): Bitmap? {
    val retriever = MediaMetadataRetriever()

    try {
        retriever.setDataSource(context, Uri.parse(videoUri))
        return retriever.frameAtTime
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        retriever.release()
    }

    return null
}