package com.kotlinconf.workshop.househelper.devices

import androidx.lifecycle.ViewModel
import com.kotlinconf.workshop.househelper.DeviceId
import com.kotlinconf.workshop.househelper.data.HouseService
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey

@AssistedInject
class CameraDetailsViewModel(
    private val houseService: HouseService,
    @Assisted private val deviceId: DeviceId,
) : ViewModel() {

    // TODO Task 5: Implement ViewModel features here

    @AssistedFactory
    @ManualViewModelAssistedFactoryKey(Factory::class)
    @ContributesIntoMap(AppScope::class)
    fun interface Factory : ManualViewModelAssistedFactory {
        fun create(deviceId: DeviceId): CameraDetailsViewModel
    }
}
