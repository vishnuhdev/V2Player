// HomeViewModel.kt
package com.player.v2player.presentation.homescreen.viewmodel

import android.Manifest
import android.content.ContentResolver
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.player.v2player.data.models.VideoDetails
import com.player.v2player.domain.usecase.VideoUseCase
import com.player.v2player.presentation.utils.PermissionHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val videoUseCase: VideoUseCase
) : ViewModel() {
    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state


    fun onPermissionResult(activity: ComponentActivity) {
        viewModelScope.launch {
                        // Launch the permission request
            PermissionHandler.requestPermission(
                activity = activity,
                permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_VIDEO
                else Manifest.permission.READ_EXTERNAL_STORAGE
            ) {
                _state.value = state.value.copy(
                    isGranted = it
                )
            }
        }
    }

    fun getAllVideos(contentResolver: ContentResolver) {
        var videosByFolder = mutableMapOf<String, MutableList<String>>()
        viewModelScope.launch {
            videosByFolder = videoUseCase.getVideosByFolder(contentResolver)
        }
        _state.value = state.value.copy(
            videos = VideoDetails.fromMap(videosByFolder)
        )
    }
}
