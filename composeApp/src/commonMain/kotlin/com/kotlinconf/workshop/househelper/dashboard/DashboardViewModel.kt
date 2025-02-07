package com.kotlinconf.workshop.househelper.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinconf.workshop.househelper.Device
import com.kotlinconf.workshop.househelper.HouseService
import com.kotlinconf.workshop.househelper.Room
import com.kotlinconf.workshop.househelper.RoomId
import com.kotlinconf.workshop.househelper.Toggleable
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn


class DashboardViewModel(
    private val houseService: HouseService,
) : ViewModel() {
    val rooms: StateFlow<List<Room>> = houseService.getRooms()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun getDevicesForRoom(roomId: RoomId): StateFlow<List<Device>> = houseService.getDevicesForRoom(roomId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onDeviceClicked(device: Device) {
        houseService.toggleDevice(device)
    }

    fun onDeviceLongPressed(device: Device) {
        // Navigation is now handled at the screen level
    }
}
