package com.kotlinconf.workshop.househelper

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kotlinconf.workshop.househelper.dashboard.DashboardScreen
import com.kotlinconf.workshop.househelper.dashboard.DashboardViewModel
import com.kotlinconf.workshop.househelper.data.DemoHouseService
import com.kotlinconf.workshop.househelper.data.HouseService
import com.kotlinconf.workshop.househelper.devices.CameraDetailsScreen
import com.kotlinconf.workshop.househelper.devices.CameraDetailsViewModel
import com.kotlinconf.workshop.househelper.devices.LightDetailsScreen
import com.kotlinconf.workshop.househelper.devices.LightDetailsViewModel
import com.kotlinconf.workshop.househelper.devices.RenameLightDialog
import com.kotlinconf.workshop.househelper.navigation.CameraDetails
import com.kotlinconf.workshop.househelper.navigation.Dashboard
import com.kotlinconf.workshop.househelper.navigation.DeviceIdNavType
import com.kotlinconf.workshop.househelper.navigation.LightDetails
import com.kotlinconf.workshop.househelper.navigation.Onboarding
import com.kotlinconf.workshop.househelper.navigation.OnboardingDone
import com.kotlinconf.workshop.househelper.navigation.RenameDevice
import com.kotlinconf.workshop.househelper.navigation.StartScreens
import com.kotlinconf.workshop.househelper.navigation.Welcome
import househelper.composeapp.generated.resources.Res
import househelper.composeapp.generated.resources.onboarding_about
import househelper.composeapp.generated.resources.onboarding_about_subtitle
import househelper.composeapp.generated.resources.onboarding_done
import househelper.composeapp.generated.resources.onboarding_done_subtitle
import househelper.composeapp.generated.resources.onboarding_welcome
import househelper.composeapp.generated.resources.onboarding_welcome_subtitle
import org.jetbrains.compose.reload.DevelopmentEntryPoint
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinMultiplatformApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.koinConfiguration
import org.koin.dsl.module
import kotlin.reflect.typeOf

@Composable
@Preview
fun App() {
    KoinMultiplatformApplication(koinConfiguration()) {
        DevelopmentEntryPoint {
            MaterialTheme(
                colorScheme = if (isSystemInDarkTheme()) {
                    darkColorScheme()
                } else {
                    lightColorScheme()
                }
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = StartScreens) {
                        navigation<StartScreens>(startDestination = Welcome) {
                            composable<Welcome> {
                                Onboarding(
                                    text = Res.string.onboarding_welcome,
                                    subtitle = Res.string.onboarding_welcome_subtitle,
                                    icon = Icons.Default.Favorite,
                                    onNext = { navController.navigate(Onboarding) }
                                )
                            }
                            composable<Onboarding> {
                                Onboarding(
                                    text = Res.string.onboarding_about,
                                    subtitle = Res.string.onboarding_about_subtitle,
                                    icon = Icons.Default.Info,
                                    onNext = { navController.navigate(OnboardingDone) }
                                )
                            }
                            composable<OnboardingDone> {
                                Onboarding(
                                    text = Res.string.onboarding_done,
                                    subtitle = Res.string.onboarding_done_subtitle,
                                    icon = Icons.Default.Home,
                                    onNext = { navController.navigate(Dashboard) }
                                )
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

                            val newName = navController.currentBackStackEntry
                                ?.savedStateHandle
                                ?.getStateFlow<String?>("newName", null)
                                ?.collectAsState()
                                ?.value

                            LightDetailsScreen(
                                deviceId = deviceId,
                                newName = newName,
                                onNavigateUp = { navController.navigateUp() },
                                onNavigateToRename = { deviceId -> navController.navigate(RenameDevice(deviceId)) }
                            )
                        }
                        composable<CameraDetails>(
                            typeMap = mapOf(typeOf<DeviceId>() to DeviceIdNavType)
                        ) {
                            val deviceId = it.toRoute<CameraDetails>().deviceId
                            CameraDetailsScreen(
                                deviceId = deviceId,
                                onNavigateUp = { navController.navigateUp() },
                            )
                        }
                        dialog<RenameDevice> {
                            RenameLightDialog(
                                currentName = it.toRoute<RenameDevice>().currentName,
                                onDismiss = {
                                    navController.previousBackStackEntry?.savedStateHandle?.set("newName", it)
                                    navController.navigateUp()
                                },
                            )
                        }
                    }
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
