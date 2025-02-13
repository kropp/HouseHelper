package com.kotlinconf.workshop.househelper.navigation

import com.kotlinconf.workshop.househelper.DeviceId
import kotlinx.serialization.Serializable

// Start screens
@Serializable
data object StartScreens

@Serializable
data object Welcome

@Serializable
data object Onboarding

@Serializable
data object OnboardingDone

// Main screens
@Serializable
data object Dashboard

@Serializable
data object Settings

@Serializable
data class LightDetails(val deviceId: DeviceId)

@Serializable
data class CameraDetails(val deviceId: DeviceId)

@Serializable
data class RenameDevice(val currentName: String)
