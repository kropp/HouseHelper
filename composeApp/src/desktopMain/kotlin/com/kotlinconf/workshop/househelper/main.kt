package com.kotlinconf.workshop.househelper

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.jetbrains.compose.reload.DevelopmentEntryPoint

fun main() = application {
    DevelopmentEntryPoint {
        Window(
            onCloseRequest = ::exitApplication,
            title = "HouseHelper",
        ) {
            App()
        }
    }
}