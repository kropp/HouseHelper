package com.kotlinconf.workshop.househelper

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        SwingPanel(
            modifier = Modifier.fillMaxHeight().aspectRatio((16.0/9.0).toFloat()),
            factory = {
                val jfxPanel = JFXPanel()

                Platform.runLater {
                    val stackPane = StackPane()
                    val scene = Scene(stackPane)

                    val mediaView = MediaView().apply {
                        mediaPlayer = MediaPlayer(Media(url))
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
                                mediaView.mediaPlayer?.stop()
                            }
                        }
                    }

                    Platform.runLater {
                        mediaView.mediaPlayer?.play()
                    }

                    mediaView.fitWidth = 480.0

                    stackPane.children.add(mediaView)
                    StackPane.setAlignment(mediaView, Pos.CENTER)

                    jfxPanel.scene = scene
                }

                jfxPanel
            }
        )
    }
}
