package com.player.v2player.viewModels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VideoScreenViewModel : ViewModel() {

    private val _context = MutableStateFlow<Context?>(null)
    val context: StateFlow<Context?> = _context.asStateFlow()

    private val _playWhenReady = MutableStateFlow(true)
    val playWhenReady: StateFlow<Boolean> = _playWhenReady.asStateFlow()

    private val _videoUri = MutableStateFlow<String?>(null)
    val videoUri: StateFlow<String?> = _videoUri.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _showControls = MutableStateFlow(true)
    val showControls: StateFlow<Boolean> = _showControls.asStateFlow()

    private val _isVideoFinished = MutableStateFlow(false)
    val isVideoFinished: StateFlow<Boolean> = _isVideoFinished.asStateFlow()

    private val _isLandScapeOrientation = MutableStateFlow(false)
    val isLandScapeOrientation: StateFlow<Boolean> = _isLandScapeOrientation.asStateFlow()

    private val _videoDuration = MutableStateFlow(0L)
    val videoDuration: StateFlow<Long> = _videoDuration.asStateFlow()

    private var _videoPosition = MutableStateFlow(0L)
    var videoPosition: StateFlow<Long> = _videoPosition.asStateFlow()

    private val _seekToPosition = MutableStateFlow(0L)
    val seekToPosition: StateFlow<Long> = _seekToPosition.asStateFlow()

    private val _sliderValue = MutableStateFlow(0f)
    val sliderValue: StateFlow<Float> = _sliderValue.asStateFlow()

    private val _exoPlayer = MutableStateFlow<ExoPlayer?>(null)
    val exoPlayer: StateFlow<ExoPlayer?> = _exoPlayer.asStateFlow()


    fun init(context: Context, videoUri: String) {
        _context.value = context
        _videoUri.value = videoUri
        initializePlayer()
        updateSliderValue()
    }

    private fun initializePlayer() {
        val exoPlayer = ExoPlayer.Builder(_context.value!!).build().apply {
            setMediaItem(MediaItem.fromUri(Uri.parse(_videoUri.value)))
            repeatMode = ExoPlayer.REPEAT_MODE_OFF
            playWhenReady = _playWhenReady.value
            prepare()
            play()
        }

        _exoPlayer.value = exoPlayer

        viewModelScope.launch {
            while (true) {
                // Update player status, such as video duration, position, playback state, etc.
                // For instance:
                val position = exoPlayer.currentPosition
                val maxPosition = exoPlayer.duration
                // Calculate slider progress, update duration, position, etc.
                delay(1000)
            }
        }
    }

    private fun updateSliderValue() {
        viewModelScope.launch {
            while (true) {
                val exoPlayer = _exoPlayer.value ?: return@launch

                val position = exoPlayer.currentPosition
                val maxPosition = exoPlayer.duration
                val progress = (position * 1000f / maxPosition).coerceIn(0f, 1000f)
                _sliderValue.value = progress

                delay(1000)
            }
        }
    }



    fun toggleShowControls() {
        _showControls.value = !_showControls.value
    }

}