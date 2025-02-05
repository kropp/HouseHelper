package com.kotlinconf.workshop.househelper

import androidx.compose.ui.graphics.Color
import househelper.composeapp.generated.resources.Res
import househelper.composeapp.generated.resources.camera
import househelper.composeapp.generated.resources.humidity
import househelper.composeapp.generated.resources.lightbulb
import househelper.composeapp.generated.resources.switch
import househelper.composeapp.generated.resources.thermostat
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

interface Sensor {
    var currentValue: Float
}

data class LightDevice(
    override val deviceId: DeviceId,
    override val name: String,
    override val iconResource: DrawableResource = Res.drawable.lightbulb,
    override val isOn: Boolean = false,
    override val roomId: RoomId,
    val brightness: Int = 50,
    val color: Color = DeviceConstants.Light.DEFAULT_COLOR
) : Device, Toggleable {
    init {
        require(brightness in DeviceConstants.Light.MIN_BRIGHTNESS..DeviceConstants.Light.MAX_BRIGHTNESS) { 
            "Brightness must be between ${DeviceConstants.Light.MIN_BRIGHTNESS} and ${DeviceConstants.Light.MAX_BRIGHTNESS}" 
        }
    }
}

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
    override val roomId: RoomId,
    override var currentValue: Float = 50f
) : Device, Sensor {
    init {
        require(currentValue in DeviceConstants.Humidity.MIN_HUMIDITY..DeviceConstants.Humidity.MAX_HUMIDITY) {
            "Humidity must be between ${DeviceConstants.Humidity.MIN_HUMIDITY}% and ${DeviceConstants.Humidity.MAX_HUMIDITY}%"
        }
    }
}

data class ThermostatDevice(
    override val deviceId: DeviceId,
    override val name: String,
    override val iconResource: DrawableResource = Res.drawable.thermostat,
    override val roomId: RoomId,
    override var currentValue: Float = 20f
) : Device, Sensor {
    init {
        require(currentValue in DeviceConstants.Thermostat.MIN_TEMPERATURE..DeviceConstants.Thermostat.MAX_TEMPERATURE) {
            "Temperature must be between ${DeviceConstants.Thermostat.MIN_TEMPERATURE}°C and ${DeviceConstants.Thermostat.MAX_TEMPERATURE}°C"
        }
    }
}

data class CameraDevice(
    override val deviceId: DeviceId,
    override val name: String,
    override val iconResource: DrawableResource = Res.drawable.camera,
    override val isOn: Boolean = false,
    override val roomId: RoomId
) : Device, Toggleable
