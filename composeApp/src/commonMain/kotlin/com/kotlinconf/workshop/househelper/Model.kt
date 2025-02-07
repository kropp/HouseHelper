package com.kotlinconf.workshop.househelper

import househelper.composeapp.generated.resources.Res
import househelper.composeapp.generated.resources.humidity
import househelper.composeapp.generated.resources.lightbulb
import househelper.composeapp.generated.resources.switch
import househelper.composeapp.generated.resources.thermostat
import org.jetbrains.compose.resources.DrawableResource

data class Room(
    val name: String,
    val appliances: List<Appliance>
)

interface Toggleable {
    val isOn: Boolean
}

sealed interface Appliance {
    val name: String
    val iconResource: DrawableResource
}

data class LightAppliance(
    override val name: String,
    override val iconResource: DrawableResource = Res.drawable.lightbulb,
    override val isOn: Boolean = false
) : Appliance, Toggleable

data class SwitchAppliance(
    override val name: String,
    override val iconResource: DrawableResource = Res.drawable.switch,
    override val isOn: Boolean = false
) : Appliance, Toggleable

data class HumidityAppliance(
    override val name: String,
    override val iconResource: DrawableResource = Res.drawable.humidity
) : Appliance

data class ThermostatAppliance(
    override val name: String,
    override val iconResource: DrawableResource = Res.drawable.thermostat
) : Appliance
