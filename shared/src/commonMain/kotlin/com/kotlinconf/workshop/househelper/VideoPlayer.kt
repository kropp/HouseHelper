package com.kotlinconf.workshop.househelper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

interface ControllableVideoPlayer {
    fun play()
    fun stop()
}

class VideoPlayerState {
    val isPlaying = mutableStateOf(true)

    var controlledPlayer: ControllableVideoPlayer? = null

    fun play() {
        controlledPlayer?.play()
        isPlaying.value = true
    }

    fun stop() {
        controlledPlayer?.stop()
        isPlaying.value = false
    }
}

@Composable
fun rememberVideoPlayerState() = remember { VideoPlayerState() }

@Composable
expect fun VideoPlayer(
    url: String,
    modifier: Modifier,
    videoPlayerState: VideoPlayerState,
)