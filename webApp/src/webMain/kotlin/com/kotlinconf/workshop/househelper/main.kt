package com.kotlinconf.workshop.househelper

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.kotlinconf.workshop.househelper.di.AppGraph
import dev.zacsweers.metro.createGraph

private val appGraph: AppGraph by lazy { createGraph<AppGraph>() }

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport {
        App(appGraph)
    }
}
