package com.kotlinconf.workshop.househelper

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Custom colors
private val Blue80 = Color(0xFF90CAF9)  // Light blue
private val BlueGrey80 = Color(0xFF90A4AE)  // Blue grey
private val LightBlue80 = Color(0xFF81D4FA)  // Lighter blue

private val Blue40 = Color(0xFF1976D2)  // Darker blue
private val BlueGrey40 = Color(0xFF546E7A)  // Darker blue grey
private val LightBlue40 = Color(0xFF0288D1)  // Medium blue

// Light theme colors
val AppLightColorScheme = lightColorScheme(
    primary = Blue40,
    secondary = BlueGrey40,
    tertiary = LightBlue40,
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFFFFFFF),
    onTertiary = Color(0xFFFFFFFF),
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
)

// Dark theme colors
val AppDarkColorScheme = darkColorScheme(
    primary = Blue80,
    secondary = BlueGrey80,
    tertiary = LightBlue80,
    background = Color(0xFF1C1B1F),
    surface = Color(0xFF1C1B1F),
    onPrimary = Color(0xFF003258),
    onSecondary = Color(0xFF202C35),
    onTertiary = Color(0xFF003547),
    onBackground = Color(0xFFE6E1E5),
    onSurface = Color(0xFFE6E1E5),
)
