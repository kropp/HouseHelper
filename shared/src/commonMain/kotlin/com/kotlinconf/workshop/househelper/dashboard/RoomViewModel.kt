package com.kotlinconf.workshop.househelper.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinconf.workshop.househelper.Device
import com.kotlinconf.workshop.househelper.RoomId
import com.kotlinconf.workshop.househelper.data.HouseService
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@AssistedInject
class RoomViewModel(
    private val houseService: HouseService,
    @Assisted private val roomId: RoomId,
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


    @AssistedFactory
    @ManualViewModelAssistedFactoryKey(Factory::class)
    @ContributesIntoMap(AppScope::class)
    fun interface Factory : ManualViewModelAssistedFactory {
        fun create(roomId: RoomId): RoomViewModel
    }
}
