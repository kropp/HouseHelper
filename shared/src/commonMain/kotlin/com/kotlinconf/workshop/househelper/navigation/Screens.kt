package com.kotlinconf.workshop.househelper.navigation

import com.kotlinconf.workshop.househelper.DeviceId
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen

// Start screens
@Serializable
data object OnboardingWelcome : Screen

@Serializable
data object OnboardingAbout : Screen

@Serializable
data object OnboardingDone : Screen

// Main screens
@Serializable
data object Dashboard : Screen

@Serializable
data class LightDetails(val deviceId: DeviceId) : Screen

@Serializable
data class CameraDetails(val deviceId: DeviceId) : Screen

@Serializable
data class RenameDevice(val deviceId: DeviceId) : Screen
