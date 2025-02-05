package com.kotlinconf.workshop.househelper

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.media.MediaView


@Composable
actual fun VideoPlayer(
    url: String,
    modifier: Modifier,
    videoPlayerState: VideoPlayerState,
) {
    SwingPanel(
        modifier = modifier,
        factory = {
            val jfxPanel = JFXPanel()

            val stackPane = StackPane()
            val scene = Scene(stackPane)

            val mediaView = MediaView().apply {
                mediaPlayer = MediaPlayer(Media(url))
                mediaPlayer.isAutoPlay = true
                mediaPlayer.volume = 0.0
                isPreserveRatio = true
            }

            videoPlayerState.controlledPlayer = object : ControllableVideoPlayer {
                override fun play() {
                    Platform.runLater {
                        mediaView.mediaPlayer?.play()
                    }
                }

                override fun stop() {
                    Platform.runLater {
                        mediaView.mediaPlayer?.pause()
                    }
                }
            }

            stackPane.children.add(mediaView)
            StackPane.setAlignment(mediaView, Pos.CENTER)

            jfxPanel.scene = scene

            jfxPanel
        }
    )
}
