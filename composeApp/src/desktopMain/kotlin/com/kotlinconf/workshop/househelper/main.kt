package com.kotlinconf.workshop.househelper

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "HouseHelper",
        state = rememberWindowState(width = 500.dp, height = 800.dp),
    ) {
        App()
    }
}
