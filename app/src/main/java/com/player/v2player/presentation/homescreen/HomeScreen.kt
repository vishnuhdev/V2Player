package com.player.v2player.presentation.homescreen

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.player.v2player.R
import com.player.v2player.data.constants.AppConstants
import com.player.v2player.presentation.homescreen.viewmodel.HomeViewModel
import com.player.v2player.presentation.navigation.Routes
import com.player.v2player.presentation.permisson.NoPermission

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel = hiltViewModel<HomeViewModel>()) {


    val state = viewModel.state.value
    val contentResolver: ContentResolver = LocalContext.current.contentResolver

    if (!state.isGranted) {
        NoPermission(text = "We kindly request your permission to access storage for the smooth functioning of the application. Thank you!😊", image = R.drawable.folder)
    } else {
        viewModel.getAllVideos(contentResolver)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            LazyColumn(Modifier.fillMaxSize()) {
                state.videos.forEach { (folder, fileNames) ->
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

fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}






