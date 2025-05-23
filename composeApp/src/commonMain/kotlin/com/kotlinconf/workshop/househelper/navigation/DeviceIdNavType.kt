package com.kotlinconf.workshop.househelper.navigation

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import com.kotlinconf.workshop.househelper.DeviceId

object DeviceIdNavType : NavType<DeviceId>(isNullableAllowed = false) {
    override fun get(bundle: SavedState, key: String): DeviceId =
        bundle.read { DeviceId(getString(key)) }

    override fun put(bundle: SavedState, key: String, value: DeviceId) =
        bundle.write { putString(key, value.value) }

    override fun parseValue(value: String): DeviceId = DeviceId(value)

    override fun serializeAsValue(value: DeviceId): String = value.value
}
