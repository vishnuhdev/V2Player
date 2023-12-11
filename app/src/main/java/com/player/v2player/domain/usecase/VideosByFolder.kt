package com.player.v2player.domain.usecase

import com.player.v2player.domain.repository.VideoRepository
import android.content.ContentResolver

class VideosByFolder (
    private val repository: VideoRepository
    ) {

        operator fun invoke(contentResolver: ContentResolver): MutableMap<String, MutableList<String>> {
            return repository.getVideosByFolder(contentResolver)
        }
}
