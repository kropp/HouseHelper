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
import kotlinx.coroutines.launch

class CameraDetailsViewModel(
    private val houseService: HouseService,
    deviceId: DeviceId,
) : ViewModel() {
    val camera: StateFlow<CameraDevice?> = houseService.getDevice(deviceId)
        .map { it as? CameraDevice }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = null
        )

    fun toggleCamera() {
        viewModelScope.launch {
            camera.value?.let { camera ->
                houseService.toggle(camera)
            }
        }
    }
}
