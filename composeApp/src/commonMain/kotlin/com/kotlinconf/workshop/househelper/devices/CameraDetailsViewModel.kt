package com.kotlinconf.workshop.househelper.devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinconf.workshop.househelper.CameraDevice
import com.kotlinconf.workshop.househelper.DeviceId
import com.kotlinconf.workshop.househelper.data.HouseService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CameraDetailsViewModel(
    private val houseService: HouseService,
    private val deviceId: DeviceId,
) : ViewModel() {
    val camera = houseService.getDevice(deviceId)
        .map { it as? CameraDevice }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = null
        )

    fun toggleCamera() {
        camera.value?.let { camera ->
            houseService.toggle(camera)
        }
    }
}
