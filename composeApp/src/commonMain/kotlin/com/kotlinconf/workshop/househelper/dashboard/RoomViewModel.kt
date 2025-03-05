package com.kotlinconf.workshop.househelper.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinconf.workshop.househelper.Device
import com.kotlinconf.workshop.househelper.RoomId
import com.kotlinconf.workshop.househelper.data.HouseService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class RoomViewModel(
    val houseService: HouseService,
    val roomId: RoomId
): ViewModel() {
    val devices: StateFlow<List<Device>> = houseService
        .getDevicesForRoom(roomId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _expanded = MutableStateFlow(true)
    val expanded = _expanded

    fun expand(isExpanded: Boolean) {
        _expanded.update { isExpanded }
    }
}
