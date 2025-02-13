package com.kotlinconf.workshop.househelper

import androidx.compose.ui.graphics.Color


object DeviceConstants {
    object Light {
        const val MIN_BRIGHTNESS = 0
        const val MAX_BRIGHTNESS = 100

        val PREDEFINED_COLORS = listOf(
            Color(255, 245, 235),  // Soft White
            Color(255, 182, 203),  // Pastel Pink
            Color(210, 170, 255),  // Pastel Lavender
            Color(135, 206, 235),  // Pastel Blue
            Color(144, 238, 144),  // Mint Green
            Color(255, 250, 160),  // Soft Yellow
            Color(255, 186, 128),  // Peach
            Color(240, 128, 128),  // Light Coral
        )

        val DEFAULT_COLOR = PREDEFINED_COLORS[0]
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
