package com.kotlinconf.workshop.househelper.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Light theme colors
val AppLightColorScheme = lightColorScheme(
    primary = Color(0xFF0B5DD8),
    secondary = Color(0xFF546E7A),
    tertiary = Color(0xFF5385A0),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFF000000),
    onTertiary = Color(0xFF000000),
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
)

// Dark theme colors
val AppDarkColorScheme = darkColorScheme(
    primary = Color(0xFF90CAF9),
    secondary = Color(0xFF90A4AE),
    tertiary = Color(0xFF81D4FA),
    background = Color(0xFF1C1B1F),
    surface = Color(0xFF1C1B1F),
    onPrimary = Color(0xFF003258),
    onSecondary = Color(0xFF202C35),
    onTertiary = Color(0xFF003547),
    onBackground = Color(0xFFE6E1E5),
    onSurface = Color(0xFFE6E1E5),
)
