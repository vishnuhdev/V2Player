package com.player.v2player.presentation.homescreen.viewmodel

import com.player.v2player.data.models.VideoDetails

data class HomeState(
    val videos: List<VideoDetails>  = emptyList(),
    val isGranted: Boolean = false
)