package com.player.v2player.domain.repository

import android.content.ContentResolver
import kotlinx.coroutines.flow.Flow

interface VideoRepository {
    fun getVideosByFolder(contentResolver: ContentResolver): MutableMap<String, MutableList<String>>
}
