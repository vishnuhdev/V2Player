package com.player.v2player.data.repository

import android.content.ContentResolver
import android.provider.MediaStore
import com.player.v2player.domain.repository.VideoRepository
import java.io.File

class VideoRepositoryImpl() : VideoRepository {

    override fun getVideosByFolder(contentResolver: ContentResolver): MutableMap<String, MutableList<String>> {
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
        return videosByFolder;
    }
}