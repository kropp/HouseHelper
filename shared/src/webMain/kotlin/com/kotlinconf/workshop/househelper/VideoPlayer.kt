package com.kotlinconf.workshop.househelper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.HtmlElementView
import kotlinx.browser.document
import org.w3c.dom.HTMLSourceElement
import org.w3c.dom.HTMLVideoElement
import kotlin.js.ExperimentalWasmJsInterop

@OptIn(ExperimentalComposeUiApi::class, ExperimentalWasmJsInterop::class)
@Composable
actual fun VideoPlayer(
    url: String,
    modifier: Modifier,
    videoPlayerState: VideoPlayerState,
) {
    val video = remember {
        (document.createElement("video") as HTMLVideoElement).apply {
            appendChild((document.createElement("source") as HTMLSourceElement).apply {
                src = url
            })
        }
    }

    videoPlayerState.controlledPlayer = object : ControllableVideoPlayer {
        override fun play() {
            video.play()
        }

        override fun stop() {
            video.pause()
        }
    }

    HtmlElementView(
        factory = { video },
        modifier = modifier,
        update = { video ->
            (video.firstChild as? HTMLSourceElement)?.src = url
        },
        onRelease = {
            video.pause()
        },
        onReset = null
    )
}