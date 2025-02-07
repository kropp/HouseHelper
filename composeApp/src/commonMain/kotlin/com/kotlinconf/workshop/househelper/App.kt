package com.kotlinconf.workshop.househelper

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.kotlinconf.workshop.househelper.dashboard.DashboardScreen
import com.kotlinconf.workshop.househelper.dashboard.DashboardViewModel
import com.kotlinconf.workshop.househelper.navigation.Dashboard
import com.kotlinconf.workshop.househelper.navigation.Onboarding
import com.kotlinconf.workshop.househelper.navigation.OnboardingDone
import com.kotlinconf.workshop.househelper.navigation.StartScreens
import com.kotlinconf.workshop.househelper.navigation.Welcome
import com.kotlinconf.workshop.househelper.navigation.LightDetails
import com.kotlinconf.workshop.househelper.navigation.CameraDetails
import com.kotlinconf.workshop.househelper.navigation.DeviceIdNavType
import com.kotlinconf.workshop.househelper.devices.LightDetailsScreen
import com.kotlinconf.workshop.househelper.devices.LightDetailsViewModel
import com.kotlinconf.workshop.househelper.devices.CameraDetailsScreen
import com.kotlinconf.workshop.househelper.devices.CameraDetailsViewModel
import househelper.composeapp.generated.resources.Res
import househelper.composeapp.generated.resources.onboarding_about
import househelper.composeapp.generated.resources.onboarding_done
import househelper.composeapp.generated.resources.onboarding_welcome
import org.jetbrains.compose.reload.DevelopmentEntryPoint
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinMultiplatformApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.koinConfiguration
import org.koin.dsl.module
import kotlin.reflect.typeOf
import com.kotlinconf.workshop.househelper.data.HouseService
import com.kotlinconf.workshop.househelper.data.DemoHouseService

@Composable
@Preview
fun App() {
    KoinMultiplatformApplication(koinConfiguration()) {
        DevelopmentEntryPoint {
            val navController = rememberNavController()
            NavHost(navController, startDestination = StartScreens) {
                navigation<StartScreens>(startDestination = Welcome) {
                    composable<Welcome> {
                        Onboarding(Res.string.onboarding_welcome, { navController.navigate(Onboarding) })
                    }
                    composable<Onboarding> {
                        Onboarding(Res.string.onboarding_about, { navController.navigate(OnboardingDone) })
                    }
                    composable<OnboardingDone> {
                        Onboarding(Res.string.onboarding_done, { navController.navigate(Dashboard) })
                    }
                }
                composable<Dashboard> {
                    DashboardScreen(
                        onNavigateToLightDetails = { deviceId ->
                            navController.navigate(LightDetails(deviceId))
                        },
                        onNavigateToCameraDetails = { deviceId ->
                            navController.navigate(CameraDetails(deviceId))
                        }
                    )
                }
                composable<LightDetails>(
                    typeMap = mapOf(typeOf<DeviceId>() to DeviceIdNavType)
                ) {
                    val deviceId = it.toRoute<LightDetails>().deviceId
                    LightDetailsScreen(
                        deviceId = deviceId,
                        onNavigateUp = { navController.navigateUp() }
                    )
                }
                composable<CameraDetails>(
                    typeMap = mapOf(typeOf<DeviceId>() to DeviceIdNavType)
                ) {
                    val deviceId = it.toRoute<CameraDetails>().deviceId
                    CameraDetailsScreen(
                        deviceId = deviceId,
                        onNavigateUp = { navController.navigateUp() }
                    )
                }
            }
        }
    }
}

private fun koinConfiguration() = koinConfiguration {
    val appModule = module {
        single<HouseService> { DemoHouseService() }
    }

    val viewModelModule = module {
        viewModelOf(::DashboardViewModel)
        viewModelOf(::LightDetailsViewModel)
        viewModelOf(::CameraDetailsViewModel)
    }

    modules(appModule, viewModelModule)
}
