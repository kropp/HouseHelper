package com.kotlinconf.workshop.househelper.navigation

import kotlinx.serialization.Serializable

@Serializable
data class Onboarding(val step: Int)

@Serializable
data object Dashboard

@Serializable
data object Settings

@Serializable
data class Light(val name: String)