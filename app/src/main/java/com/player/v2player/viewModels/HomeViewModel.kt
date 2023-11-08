package com.player.v2player.viewModels

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import java.io.File

class HomeViewModel : ViewModel() {

    fun getAllVideos(contentResolver: ContentResolver): Map<String, List<String>> {
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
                val videoUri = Uri.parse(videoPath)
                val parentFolder = File(videoUri.path ?: "").parent ?: "Unknown Folder"
                videosByFolder.getOrPut(parentFolder) { mutableListOf() }.add(videoUri.toString())
            }
        }
        return videosByFolder
    }

}