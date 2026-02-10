package com.kotlinconf.workshop.househelper

import androidx.compose.ui.graphics.Color


data class NamedColor(val color: Color, val name: String)

object DeviceConstants {
    object Light {
        const val MIN_BRIGHTNESS = 0
        const val MAX_BRIGHTNESS = 100

        val PREDEFINED_COLORS = listOf(
            NamedColor(Color(185, 116, 58), "Amber"),
            NamedColor(Color(185, 112, 133), "Pink"),
            NamedColor(Color(140, 100, 185), "Purple"),
            NamedColor(Color(65, 136, 165), "Blue"),
            NamedColor(Color(74, 168, 74), "Light Green"),
            NamedColor(Color(34, 139, 34), "Forest Green"),
            NamedColor(Color(170, 58, 58), "Red"),
        )

        val DEFAULT_COLOR = PREDEFINED_COLORS[0].color
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
