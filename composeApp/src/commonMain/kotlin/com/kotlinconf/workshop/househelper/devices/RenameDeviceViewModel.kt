package com.kotlinconf.workshop.househelper.devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinconf.workshop.househelper.DeviceId
import com.kotlinconf.workshop.househelper.data.HouseService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RenameDeviceViewModel(
    private val houseService: HouseService,
) : ViewModel() {
    private val _renamePerformed: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val renamePerformed: StateFlow<Boolean> = _renamePerformed.asStateFlow()

    fun renameDevice(deviceId: DeviceId, text: String) {
        viewModelScope.launch {
            houseService.rename(deviceId, text)
            _renamePerformed.value = true
        }
    }
}
