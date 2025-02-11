package com.kotlinconf.workshop.househelper

import kotlinx.serialization.Serializable

@Serializable
data class RGBColor(
    val red: Int,
    val green: Int,
    val blue: Int,
) {
    init {
        require(red in 0..255) { "Red value must be between 0 and 255" }
        require(green in 0..255) { "Green value must be between 0 and 255" }
        require(blue in 0..255) { "Blue value must be between 0 and 255" }
    }
}

object DeviceConstants {
    object Light {
        const val MIN_BRIGHTNESS = 0
        const val MAX_BRIGHTNESS = 100

        val PREDEFINED_COLORS = listOf(
            RGBColor(255, 255, 255), // White
            RGBColor(255, 255, 0),   // Yellow
            RGBColor(255, 165, 0),   // Orange
            RGBColor(255, 0, 0),     // Red
            RGBColor(0, 255, 0),     // Green
            RGBColor(0, 0, 255),     // Blue
            RGBColor(128, 0, 128),   // Purple
            RGBColor(255, 192, 203)  // Pink
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
