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
import kotlinx.coroutines.launch

class LightDetailsViewModel(
    private val houseService: HouseService,
    private val deviceId: DeviceId,
) : ViewModel() {
    val light: StateFlow<LightDevice?> = houseService.getDevice(deviceId)
        .map { device -> device as? LightDevice }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun toggleLight() {
        viewModelScope.launch {
            houseService.toggle(deviceId = deviceId)
        }
    }

    fun updateBrightness(brightness: Int) {
        viewModelScope.launch {
            houseService.setBrightness(deviceId, brightness)
        }
    }

    fun updateColor(color: Color) {
        viewModelScope.launch {
            houseService.setColor(deviceId, color)
        }
    }
}
