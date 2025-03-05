package com.kotlinconf.workshop.househelper.dashboard

import androidx.lifecycle.SavedStateHandle
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

    val expanded = savedStateHandle.getStateFlow("expanded", true)

    fun expand(isExpanded: Boolean) {
        savedStateHandle.set("expanded", isExpanded)
    }
}
