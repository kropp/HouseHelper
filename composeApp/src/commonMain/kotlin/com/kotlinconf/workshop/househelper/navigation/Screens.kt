package com.kotlinconf.workshop.househelper.navigation

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
data class Light(val name: String)