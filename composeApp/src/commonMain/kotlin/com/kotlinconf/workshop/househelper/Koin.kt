package com.kotlinconf.workshop.househelper

import com.kotlinconf.workshop.househelper.dashboard.DashboardViewModel
import com.kotlinconf.workshop.househelper.dashboard.RoomViewModel
import com.kotlinconf.workshop.househelper.data.DemoHouseService
import com.kotlinconf.workshop.househelper.data.HouseService
import com.kotlinconf.workshop.househelper.devices.CameraDetailsViewModel
import com.kotlinconf.workshop.househelper.devices.LightDetailsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.koinConfiguration
import org.koin.dsl.module


fun createKoinConfig() = koinConfiguration {
    val appModule = module {
        single<HouseService> { DemoHouseService() }
    }

    val viewModelModule = module {
        viewModelOf(::DashboardViewModel)
        viewModelOf(::RoomViewModel)
        viewModelOf(::LightDetailsViewModel)
        viewModelOf(::CameraDetailsViewModel)
    }

    modules(appModule, viewModelModule)
}
