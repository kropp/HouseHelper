package com.kotlinconf.workshop.househelper.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinconf.workshop.househelper.Appliance
import com.kotlinconf.workshop.househelper.HouseService
import com.kotlinconf.workshop.househelper.Room
import com.kotlinconf.workshop.househelper.Toggleable
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn


class DashboardViewModel(
    private val houseService: HouseService
) : ViewModel() {
    val rooms: StateFlow<List<Room>> = houseService.getRooms()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onApplianceClicked(room: Room, appliance: Appliance) {
        houseService.toggleAppliance(appliance)
    }
}
