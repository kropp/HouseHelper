package com.kotlinconf.workshop.househelper

import androidx.compose.ui.graphics.Color


object DeviceConstants {
    object Light {
        const val MIN_BRIGHTNESS = 0
        const val MAX_BRIGHTNESS = 100

        val PREDEFINED_COLORS = listOf(
            Color.White,
            Color(255, 255, 0),   // Yellow
            Color(255, 165, 0),   // Orange
            Color.Red,
            Color.Green,
            Color.Blue,
            Color(128, 0, 128),   // Purple
            Color(255, 192, 203)  // Pink
        )

        val DEFAULT_COLOR = PREDEFINED_COLORS[0] // White
    }

    object Humidity {
        const val MIN_HUMIDITY = 0f
        const val MAX_HUMIDITY = 100f
    }

    object Thermostat {
        const val MIN_TEMPERATURE = -50f
        const val MAX_TEMPERATURE = 50f
    }
}
