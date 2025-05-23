package com.kotlinconf.workshop.househelper.navigation

import com.kotlinconf.workshop.househelper.DeviceId
import kotlinx.serialization.Serializable

// Start screens
@Serializable
data object OnboardingWelcome

@Serializable
data object OnboardingAbout

@Serializable
data object OnboardingDone

// Main screens
@Serializable
data object Dashboard

@Serializable
data class LightDetails(val deviceId: DeviceId)

@Serializable
data class CameraDetails(val deviceId: DeviceId)

// TODO Task 9: add new navigation class
