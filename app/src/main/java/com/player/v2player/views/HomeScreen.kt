package com.player.v2player.views

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.player.v2player.R
import com.player.v2player.constants.AppConstants
import com.player.v2player.models.VideoDetails
import com.player.v2player.navigation.Routes
import com.player.v2player.viewModels.HomeViewModel

@Composable
fun HomeScreen(navController: NavHostController) {

    var videos by remember { mutableStateOf<List<VideoDetails>>(emptyList()) }
    var isPermissionNotGrand by remember {
        mutableStateOf(true)
    }

    val viewModel = HomeViewModel()
    val contentResolver: ContentResolver = LocalContext.current.contentResolver
    val dialogQueue = viewModel.visiblePermissionDialogQueue
    val permissionsToRequest = arrayOf(
        Manifest.permission.READ_MEDIA_VIDEO
    )

    val cameraPermissionResultLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                Log.d("Permission", isGranted.toString())
                if (isGranted) {
                    isPermissionNotGrand = false
                    videos = viewModel.getAllVideos(contentResolver)
                    Log.d("videosss", videos.toString())
                }
                viewModel.onPermissionResult(
                    permission = Manifest.permission.READ_EXTERNAL_STORAGE, isGranted = isGranted
                )
            })

    LaunchedEffect(key1 = Unit) {
        cameraPermissionResultLauncher.launch(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    if (isPermissionNotGrand) {
        NoPermission(text = "We kindly request your permission to access storage for the smooth functioning of the application. Thank you!😊", image = R.drawable.folder)
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            LazyColumn(Modifier.fillMaxSize()) {
                videos.forEach { (folder, fileNames) ->
                    item {
                        TextButton(onClick = {
                            navController.currentBackStackEntry?.savedStateHandle?.apply {
                                set(AppConstants.BulkUris, fileNames)
                            }
                            navController.navigate(Routes.FileScreen.route) {
                                popUpTo(Routes.FileScreen.route) {
                                    inclusive = true
                                }
                            }
                        }) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.folder),
                                    contentDescription = "Folder Icon",
                                    modifier = Modifier.size(height = 20.dp, width = 20.dp),
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = folder,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily(
                                        Font(R.font.raleway_medium)
                                    ),
                                    fontSize = 18.sp,
                                )
                            }
                        }
                    }
                }
            }
//        dialogQueue
//            .reversed()
//            .forEach { permission ->
//                PermissionDialog(
//                    permissionTextProvider = when (permission) {
//                        Manifest.permission.CAMERA -> {
//                            CameraPermissionTextProvider()
//                        }
//                        Manifest.permission.RECORD_AUDIO -> {
//                            RecordAudioPermissionTextProvider()
//                        }
//                        Manifest.permission.CALL_PHONE -> {
//                            PhoneCallPermissionTextProvider()
//                        }
//                        else -> return@forEach
//                    },
//                    isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
//                        permission
//                    ),
//                    onDismiss = viewModel::dismissDialog,
//                    onOkClick = {
//                        viewModel.dismissDialog()
//                        multiplePermissionResultLauncher.launch(
//                            arrayOf(permission)
//                        )
//                    },
//                    onGoToAppSettingsClick = ::openAppSettings
//                )
//            }


        }
    }


}


fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}







