package com.kotlinconf.workshop.househelper.devices

import androidx.lifecycle.ViewModel
import com.kotlinconf.workshop.househelper.DeviceId
import com.kotlinconf.workshop.househelper.data.HouseService

class CameraDetailsViewModel(
    private val houseService: HouseService,
    private val deviceId: DeviceId,
) : ViewModel() {

    // TODO Task 5: Implement ViewModel features here

    // TODO Task 8: Set up Metro factory for assisted injection
}
