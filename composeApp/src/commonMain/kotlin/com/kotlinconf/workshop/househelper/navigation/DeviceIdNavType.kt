package com.kotlinconf.workshop.househelper.navigation

import androidx.core.bundle.Bundle
import androidx.navigation.NavType
import com.kotlinconf.workshop.househelper.DeviceId

object DeviceIdNavType : NavType<DeviceId>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): DeviceId? =
        bundle.getString(key)?.let { DeviceId(it) }

    override fun parseValue(value: String): DeviceId =
        DeviceId(value)

    override fun put(bundle: Bundle, key: String, value: DeviceId) {
        bundle.putString(key, value.value)
    }
}
