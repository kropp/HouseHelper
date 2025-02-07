package com.kotlinconf.workshop.househelper

import househelper.composeapp.generated.resources.Res
import househelper.composeapp.generated.resources.humidity
import househelper.composeapp.generated.resources.lightbulb
import househelper.composeapp.generated.resources.switch
import househelper.composeapp.generated.resources.thermostat
import househelper.composeapp.generated.resources.camera
import org.jetbrains.compose.resources.DrawableResource


data class Room(
    val id: String,
    val name: String,
)

interface Toggleable {
    val isOn: Boolean
}

sealed interface Device {
    val name: String
    val iconResource: DrawableResource
    val roomId: String
}

data class LightDevice(
    override val name: String,
    override val iconResource: DrawableResource = Res.drawable.lightbulb,
    override val isOn: Boolean = false,
    override val roomId: String
) : Device, Toggleable

data class SwitchDevice(
    override val name: String,
    override val iconResource: DrawableResource = Res.drawable.switch,
    override val isOn: Boolean = false,
    override val roomId: String
) : Device, Toggleable

data class HumidityDevice(
    override val name: String,
    override val iconResource: DrawableResource = Res.drawable.humidity,
    override val roomId: String
) : Device

data class ThermostatDevice(
    override val name: String,
    override val iconResource: DrawableResource = Res.drawable.thermostat,
    override val roomId: String
) : Device

data class CameraDevice(
    override val name: String,
    override val iconResource: DrawableResource = Res.drawable.camera,
    override val isOn: Boolean = false,
    override val roomId: String
) : Device, Toggleable
