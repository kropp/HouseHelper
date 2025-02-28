package com.kotlinconf.workshop.househelper

import androidx.compose.ui.graphics.Color


object DeviceConstants {
    object Light {
        const val MIN_BRIGHTNESS = 0
        const val MAX_BRIGHTNESS = 100

        val PREDEFINED_COLORS = listOf(
            Color(185, 116, 58),  // Warm White
            Color(185, 112, 133),  // Pastel Pink
            Color(140, 100, 185),  // Pastel Lavender
            Color(65, 136, 165),  // Pastel Blue
            Color(74, 168, 74),  // Mint Green
            Color(34, 139, 34),  // Forest Green
            Color(170, 58, 58),  // Light Coral
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
