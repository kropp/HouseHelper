package com.kotlinconf.workshop.househelper.dashboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinconf.workshop.househelper.Device
import com.kotlinconf.workshop.househelper.RoomId
import com.kotlinconf.workshop.househelper.data.HouseService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RoomViewModel(
    private val houseService: HouseService,
    private val roomId: RoomId,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val devices: StateFlow<List<Device>> = houseService
        .getDevicesForRoom(roomId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onDeviceClicked(device: Device) {
        viewModelScope.launch {
            houseService.toggle(device.deviceId)
        }
    }

    private val _expanded = savedStateHandle.getMutableStateFlow("expanded", true)
    val expanded = _expanded.asStateFlow()

    fun expand(isExpanded: Boolean) {
        _expanded.value = isExpanded
    }
}
