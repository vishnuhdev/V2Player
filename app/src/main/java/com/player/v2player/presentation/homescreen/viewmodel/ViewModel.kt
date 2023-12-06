package com.player.v2player.presentation.homescreen.viewmodel

import android.content.ContentResolver
import android.provider.MediaStore
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.player.v2player.data.models.VideoDetails
import java.io.File

class ViewModel : ViewModel() {

    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    fun dismissDialog() {
        visiblePermissionDialogQueue.removeFirst()
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if (!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
    }

    fun getAllVideos(contentResolver: ContentResolver): List<VideoDetails> {
        val videosByFolder = mutableMapOf<String, MutableList<String>>()
        val projection = arrayOf(MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA)

        val cursor = contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        cursor?.use {
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            while (it.moveToNext()) {
                val videoPath = it.getString(dataColumn)
                val videoFile = File(videoPath)
                val parentFolder = videoFile.parentFile?.name ?: "Unknown Folder"
                videosByFolder.getOrPut(parentFolder) { mutableListOf() }.add(videoFile.toString())
            }
        }

        return VideoDetails.fromMap(videosByFolder)
    }


}