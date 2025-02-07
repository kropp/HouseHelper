package com.kotlinconf.workshop.househelper

import househelper.composeapp.generated.resources.Res
import househelper.composeapp.generated.resources.humidity
import househelper.composeapp.generated.resources.lightbulb
import househelper.composeapp.generated.resources.switch
import househelper.composeapp.generated.resources.thermostat
import househelper.composeapp.generated.resources.camera
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import kotlin.jvm.JvmInline


@JvmInline
@Serializable
value class RoomId(val value: String)

@JvmInline
@Serializable
value class DeviceId(val value: String)

data class Room(
    val id: RoomId,
    val name: String,
)

sealed interface Toggleable {
    val isOn: Boolean
}

sealed interface Device {
    val deviceId: DeviceId
    val name: String
    val iconResource: DrawableResource
    val roomId: RoomId
}

data class LightDevice(
    override val deviceId: DeviceId,
    override val name: String,
    override val iconResource: DrawableResource = Res.drawable.lightbulb,
    override val isOn: Boolean = false,
    override val roomId: RoomId
) : Device, Toggleable

data class SwitchDevice(
    override val deviceId: DeviceId,
    override val name: String,
    override val iconResource: DrawableResource = Res.drawable.switch,
    override val isOn: Boolean = false,
    override val roomId: RoomId
) : Device, Toggleable

data class HumidityDevice(
    override val deviceId: DeviceId,
    override val name: String,
    override val iconResource: DrawableResource = Res.drawable.humidity,
    override val roomId: RoomId
) : Device

data class ThermostatDevice(
    override val deviceId: DeviceId,
    override val name: String,
    override val iconResource: DrawableResource = Res.drawable.thermostat,
    override val roomId: RoomId
) : Device

data class CameraDevice(
    override val deviceId: DeviceId,
    override val name: String,
    override val iconResource: DrawableResource = Res.drawable.camera,
    override val isOn: Boolean = false,
    override val roomId: RoomId
) : Device, Toggleable
