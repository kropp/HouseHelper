package com.kotlinconf.workshop.househelper.devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinconf.workshop.househelper.CameraDevice
import com.kotlinconf.workshop.househelper.DeviceId
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
class CameraDetailsViewModel(
    private val houseService: HouseService,
    @Assisted private val deviceId: DeviceId,
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

    @AssistedFactory
    @ManualViewModelAssistedFactoryKey(Factory::class)
    @ContributesIntoMap(AppScope::class)
    fun interface Factory : ManualViewModelAssistedFactory {
        fun create(deviceId: DeviceId): CameraDetailsViewModel
    }
}
