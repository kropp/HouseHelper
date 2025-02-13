package com.kotlinconf.workshop.househelper.devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinconf.workshop.househelper.CameraDevice
import com.kotlinconf.workshop.househelper.DeviceId
import com.kotlinconf.workshop.househelper.data.HouseService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CameraDetailsViewModel(
    private val houseService: HouseService,
) : ViewModel() {
    fun getCamera(deviceId: DeviceId): StateFlow<CameraDevice?> = houseService.getDevice(deviceId)
        .map { it as? CameraDevice }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = null
        )

    fun toggleCamera(deviceId: DeviceId) {
        getCamera(deviceId).value?.let { camera ->
            houseService.toggle(camera)
        }
    }
}
