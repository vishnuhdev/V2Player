package com.player.v2player.presentation.videoscreen.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    val player: ExoPlayer,
    val context: Context,
) : ViewModel() {

    private val _playWhenReady = MutableStateFlow(true)
    val playWhenReady: StateFlow<Boolean> = _playWhenReady.asStateFlow()

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


    fun playVideo(uri: Uri){
        player.apply {
            setMediaItem(MediaItem.fromUri(Uri.parse(uri.toString())))
            repeatMode = ExoPlayer.REPEAT_MODE_OFF
            playWhenReady = _playWhenReady.value
            prepare()
            play()
        }
        updateSliderValue()

    }

    private fun updateSliderValue() {
        viewModelScope.launch {
            while (true) {
                val position = player.currentPosition
                val maxPosition = player.duration
                val progress = (position * 1000f / maxPosition).coerceIn(0f, 1000f)
                _sliderValue.value = progress

                delay(1000)
            }
        }
    }



    fun toggleShowControls() {
        _showControls.value = !_showControls.value
    }

    fun setSliderValue(float : Float) {
        _sliderValue.value = float
    }

    fun setVideoPosition() {
        _videoPosition.value = player.currentPosition
    }

    fun setVideoDuration() {
        _videoDuration.value = player.duration
    }

    fun setIsVideoFinished(boolean: Boolean) {
        _isVideoFinished.value = boolean
    }

    fun toggleIsPlaying() {
        _isPlaying.value = !_isPlaying.value
    }

    fun stopPlayer(){
        player.stop()
    }

    fun onFastForward() {
        player.seekTo(player.currentPosition + 10000)
    }

    fun onRewind() {
        player.seekTo(player.currentPosition - 10000)
    }

    fun onSliderValueChange(float: Float){
        val seekPosition = (float * player.duration / 1000).toLong()
        _seekToPosition.value = seekPosition.coerceIn(0, player.duration)
        _sliderValue.value = float
    }

    fun onPlayPauseToggle() {
        if (_isVideoFinished.value) {
            _isVideoFinished.value = false
            player.seekTo(0)
            player.play()
            _isPlaying.value = !_isPlaying.value
        } else {
            _isPlaying.value = !_isPlaying.value
            if (_isPlaying.value) {
                player.pause()
            } else {
                player.play()
            }
        }
    }

    fun setIsLandScape(bool: Boolean) {
        _isLandScapeOrientation.value = bool
    }

}