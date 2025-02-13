package com.kotlinconf.workshop.househelper.devices

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinconf.workshop.househelper.DeviceId
import com.kotlinconf.workshop.househelper.LightDevice
import com.kotlinconf.workshop.househelper.data.HouseService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class LightDetailsViewModel(
    private val houseService: HouseService,
    deviceId: DeviceId,
) : ViewModel() {
    val light: StateFlow<LightDevice?> = houseService.getDevice(deviceId)
        .map { device -> device as? LightDevice }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = null
        )

    fun toggleLight() {
        light.value?.let { light ->
            houseService.updateDevice(light.copy(isOn = !light.isOn))
        }
    }

    fun updateBrightness(brightness: Int) {
        light.value?.let { light ->
            houseService.updateDevice(light.copy(brightness = brightness))
        }
    }

    fun updateColor(color: Color) {
        light.value?.let { light ->
            houseService.updateDevice(light.copy(color = color))
        }
    }

    fun renameLightDevice(newName: String) {
        light.value?.let { light ->
            houseService.updateDevice(light.copy(name = newName))
        }
    }
}
