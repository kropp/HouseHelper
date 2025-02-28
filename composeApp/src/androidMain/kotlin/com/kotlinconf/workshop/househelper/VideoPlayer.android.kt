package com.kotlinconf.workshop.househelper

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
actual fun VideoPlayer(
    url: String,
    modifier: Modifier,
    videoPlayerState: VideoPlayerState,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            VideoView(context).apply {
                val videoView = this
                videoView.setVideoURI(Uri.parse(url))
                videoPlayerState.controlledPlayer = object : ControllableVideoPlayer {
                    override fun play() {
                        videoView.start()
                    }

                    override fun stop() {
                        videoView.pause()
                    }
                }
            }
        },
        update = { view -> },
    )
}
