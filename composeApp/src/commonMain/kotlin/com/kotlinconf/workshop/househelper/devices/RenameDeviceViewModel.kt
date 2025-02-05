package com.kotlinconf.workshop.househelper.devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinconf.workshop.househelper.DeviceId
import com.kotlinconf.workshop.househelper.data.HouseService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RenameDeviceViewModel(
    private val deviceId: DeviceId,
    private val houseService: HouseService,
) : ViewModel() {

    val deviceName: StateFlow<String?> = houseService.getDevice(deviceId)
        .map { it?.name }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _renamePerformed: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val renamePerformed: StateFlow<Boolean> = _renamePerformed.asStateFlow()

    fun renameDevice(text: String) {
        viewModelScope.launch {
            houseService.rename(deviceId, text)
            _renamePerformed.value = true
        }
    }
}
