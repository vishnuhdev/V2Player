package com.player.v2player.presentation.videoscreen

import android.app.Activity
import android.content.pm.ActivityInfo
import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController
import com.player.v2player.R
import com.player.v2player.data.constants.AppConstants
import com.player.v2player.presentation.videoscreen.components.PlayerButton
import com.player.v2player.presentation.videoscreen.viewmodel.ViewModel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun VideoPlayerScreen(navController: NavHostController) {


    val context = LocalContext.current
    val viewModel = hiltViewModel<ViewModel>()
    val videoUri = navController.previousBackStackEntry?.savedStateHandle?.get<String>(
        AppConstants.VideoUri
    )
    val uri = if (!videoUri.isNullOrEmpty()) {
        Uri.parse(videoUri)
    } else {
        Uri.parse("Video")
    }
    val fileName = uri.lastPathSegment
    var lifeCycle by remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }
    val coroutineScope = rememberCoroutineScope()
    val lifeCycleOwner = LocalLifecycleOwner.current
    val isPlaying by viewModel.isPlaying.collectAsState()
    val showControls by viewModel.showControls.collectAsState()
    val isLandScapeOrientation by viewModel.isLandScapeOrientation.collectAsState()
    val videoDuration by viewModel.videoDuration.collectAsState()
    val videoPosition by viewModel.videoPosition.collectAsState()
    val seekToPosition by viewModel.seekToPosition.collectAsState()
    val sliderValue by viewModel.sliderValue.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.playVideo(Uri.parse(videoUri))
    }

    LaunchedEffect(Unit) {
        delay(5000)
        viewModel.toggleShowControls()
    }

    DisposableEffect(Unit) {
        val updateTimeJob = coroutineScope.launch {
            while (true) {
                val position = viewModel.player.currentPosition
                val maxPosition = viewModel.player.duration
                val progress = (position * 1000f / maxPosition).coerceIn(0f, 1000f)
                viewModel.setSliderValue(progress)
                delay(1000)
            }
        }

        onDispose {
            updateTimeJob.cancel()
        }
    }

    LaunchedEffect(seekToPosition) {
        viewModel.player.seekTo(seekToPosition)
    }

    LaunchedEffect(Unit) {
        while (true) {
            viewModel.setVideoPosition()
            delay(1000)
        }
    }

    DisposableEffect(Unit) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            lifeCycle = event
        }

        val playerListener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                viewModel.setVideoPosition()
                viewModel.setVideoDuration()

                if (playbackState == Player.STATE_ENDED) {
                    viewModel.setIsVideoFinished(true)
                    viewModel.toggleShowControls()
                    viewModel.toggleIsPlaying()
                    viewModel.stopPlayer()

                    coroutineScope.launch {
                        delay(1000)
                        if (isLandScapeOrientation) {
                            (context as? Activity)?.rotateScreenToLandscape(false)
                        }
                        navController.navigateUp()
                    }

                }
            }
        }

        lifeCycleOwner.lifecycle.addObserver(lifecycleObserver)
        viewModel.player.addListener(playerListener)

        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(lifecycleObserver)
            viewModel.player.removeListener(playerListener)
        }
    }

    fun formatDuration(milliseconds: Long): String {
        val totalSeconds = milliseconds / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }

    fun onBackPressed() {
        viewModel.stopPlayer()
        coroutineScope.launch {
            delay(10)
            if (isLandScapeOrientation) {
                (context as? Activity)?.rotateScreenToLandscape(false)
            }
            navController.navigateUp()
        }
    }

    fun onRotateClicked() {
        if (!isLandScapeOrientation) {
            (context as? Activity)?.rotateScreenToLandscape(true)
            viewModel.setIsLandScape(true)
        } else {
            (context as? Activity)?.rotateScreenToLandscape(false)
            viewModel.setIsLandScape(false)
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    viewModel.toggleShowControls()
                    if (showControls) {
                        coroutineScope.launch {
                            delay(5000)
                            viewModel.toggleShowControls()
                        }
                    } else {
                        coroutineScope.coroutineContext.cancelChildren()
                    }
                }
            }, contentAlignment = Alignment.Center
    ) {
        AndroidView(modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black), update = {
            when (lifeCycle) {
                Lifecycle.Event.ON_PAUSE -> {
                    it.onPause()
                    viewModel.toggleIsPlaying()
                    it.player?.pause()
                }

                Lifecycle.Event.ON_RESUME -> {
                    it.onResume()
                }

                else -> Unit
            }
        }, factory = {
            PlayerView(it).apply {
                player = viewModel.player
                useController = false
                FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        })
        AnimatedVisibility(
            visible = showControls, enter = fadeIn(), exit = fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Transparent)
                    .wrapContentSize(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { onBackPressed() },
                        colors = ButtonDefaults.buttonColors(Color.Transparent),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.back_button),
                            contentDescription = "Back Button",
                            modifier = Modifier.size(35.dp)
                        )
                    }
                    Text(
                        text = fileName.toString(),
                        fontFamily = FontFamily(
                            Font(R.font.raleway_medium)
                        ),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 2.dp),
                        color = Color.White

                    )

                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PlayerButton(
                            onClick = { viewModel.onRewind() }, icon = R.drawable.arrow_left,
                        )
                        PlayerButton(
                            onClick = { viewModel.onPlayPauseToggle() },
                            icon = if (!isPlaying) R.drawable.round_pause else R.drawable.round_play,
                        )
                        PlayerButton(
                            onClick = { viewModel.onFastForward() }, icon = R.drawable.arrow_right,
                        )
                    }
                }
                Column(
                    modifier = Modifier
                ) {
                    Slider(
                        value = sliderValue, onValueChange = {
                            viewModel.onSliderValueChange(it)
                        }, valueRange = 0f..1000f
                    )
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally),
                    ) {
                        Row(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${formatDuration(videoPosition)} / ",
                                color = Color.White,
                                fontSize = 12.sp
                            )
                            Text(
                                text = formatDuration(videoDuration),
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = {
                                onRotateClicked()
                            },
                            colors = ButtonDefaults.buttonColors(Color.Transparent),
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.fullscreen),
                                contentDescription = "FullScreen",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

fun Activity.rotateScreenToLandscape(landScape: Boolean) {
    requestedOrientation = if (landScape) {
        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    } else {
        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}




