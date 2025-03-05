package com.kotlinconf.workshop.househelper.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinconf.workshop.househelper.*
import com.kotlinconf.workshop.househelper.data.HouseService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class DashboardViewModel(
    private val houseService: HouseService,
) : ViewModel() {
    val rooms: StateFlow<List<Room>> = houseService.getRooms()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun getDevicesForRoom(roomId: RoomId): StateFlow<List<Device>> = houseService
        .getDevicesForRoom(roomId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onDeviceClicked(device: Device) {
        viewModelScope.launch {
            houseService.toggle(device)
        }
    }
}
