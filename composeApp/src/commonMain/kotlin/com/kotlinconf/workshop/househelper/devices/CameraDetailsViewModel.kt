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
    private val deviceId: DeviceId,
) : ViewModel() {
    val camera: StateFlow<CameraDevice?> = houseService.getCamera(deviceId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val cameraFootage: StateFlow<String?> = houseService.getCameraFootage(deviceId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun toggleCamera() {
        viewModelScope.launch {
            houseService.toggle(deviceId)
        }
    }

    fun renameDevice(newName: String) {
        viewModelScope.launch {
            houseService.rename(deviceId, newName)
        }
    }
}
