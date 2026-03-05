package com.kotlinconf.workshop.househelper

import androidx.compose.ui.window.ComposeUIViewController
import com.kotlinconf.workshop.househelper.di.AppGraph
import dev.zacsweers.metro.createGraph

private val appGraph: AppGraph by lazy { createGraph<AppGraph>() }

fun MainViewController() = ComposeUIViewController { App(appGraph) }
