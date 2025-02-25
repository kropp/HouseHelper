package com.kotlinconf.workshop.househelper

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.delay
import platform.AVFoundation.*
import platform.CoreMedia.CMTimeGetSeconds
import platform.Foundation.NSURL
import platform.UIKit.UIColor
import platform.UIKit.UIView

@Composable
actual fun VideoPlayer(
    url: String,
    modifier: Modifier,
    videoPlayerState: VideoPlayerState,
) {
    val uiView = remember {
        UIView().apply {
            backgroundColor = UIColor.blackColor
        }
    }
    val avPlayerLayer = remember { AVPlayerLayer() }
    val mediaPlayer = remember {
        uiView.layer.addSublayer(avPlayerLayer)
        AVPlayer(NSURL.URLWithString(url)!!).apply {
            avPlayerLayer.player = this
        }
    }
    mediaPlayer.volume = 0.0f
    LaunchedEffect(Unit) {
        @OptIn(ExperimentalForeignApi::class)
        avPlayerLayer.setFrame(uiView.frame)
    }
    val player = remember(mediaPlayer) { mediaPlayer.toControllableVideoPlayer() }
    videoPlayerState.controlledPlayer = player
    AvView(uiView, mediaPlayer, avPlayerLayer)
}

fun AVPlayer.toControllableVideoPlayer(): ControllableVideoPlayer {
    val mediaPlayer = this
    return object : ControllableVideoPlayer {
        override fun play() {
            mediaPlayer.play()
        }

        override fun stop() {
            mediaPlayer.pause()
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
@Composable
private fun AvView(
    uiView: UIView,
    mediaPlayer: AVPlayer,
    avPlayerLayer: AVPlayerLayer
) {
    Box(Modifier.fillMaxSize()) {
        var inflated by remember { mutableStateOf(false) }
        LaunchedEffect(inflated) {
            if (inflated) {
                avPlayerLayer.frame = uiView.frame
                mediaPlayer.play()
            }
        }
        UIKitView(
            factory = {
                uiView
            },
            update = {
                inflated = true
            },
            onRelease = {
                mediaPlayer.pause()
                mediaPlayer.cancelPendingPrerolls()
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
