package com.kotlinconf.workshop.househelper

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kotlinconf.workshop.househelper.dashboard.DashboardScreen
import com.kotlinconf.workshop.househelper.devices.CameraDetailsScreen
import com.kotlinconf.workshop.househelper.devices.LightDetailsScreen
import com.kotlinconf.workshop.househelper.devices.RenameDeviceScreen
import com.kotlinconf.workshop.househelper.navigation.CameraDetails
import com.kotlinconf.workshop.househelper.navigation.Dashboard
import com.kotlinconf.workshop.househelper.navigation.DeviceIdNavType
import com.kotlinconf.workshop.househelper.navigation.LightDetails
import com.kotlinconf.workshop.househelper.navigation.OnboardingAbout
import com.kotlinconf.workshop.househelper.navigation.OnboardingDone
import com.kotlinconf.workshop.househelper.navigation.OnboardingWelcome
import com.kotlinconf.workshop.househelper.navigation.RenameDevice
import househelper.composeapp.generated.resources.Res
import househelper.composeapp.generated.resources.onboarding_about
import househelper.composeapp.generated.resources.onboarding_about_subtitle
import househelper.composeapp.generated.resources.onboarding_done
import househelper.composeapp.generated.resources.onboarding_done_subtitle
import househelper.composeapp.generated.resources.onboarding_next_button
import househelper.composeapp.generated.resources.onboarding_welcome
import househelper.composeapp.generated.resources.onboarding_welcome_subtitle
import kotlinx.coroutines.channels.Channel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.reflect.typeOf

fun navigateToDeepLink(uri: String) {
    deepLinkUris.trySend(uri)
}

private val deepLinkUris = Channel<String>(capacity = 1)

@Composable
@Preview
fun App() {
    // TODO Task 14: customize theme
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()

            NavHost(navController, startDestination = OnboardingWelcome) {
                composable<OnboardingWelcome> {
                    OnboardingScreen(
                        text = stringResource(Res.string.onboarding_welcome),
                        subtitle = stringResource(Res.string.onboarding_welcome_subtitle),
                        buttonText = stringResource(Res.string.onboarding_next_button),
                        icon = Icons.Default.Favorite,
                        onNext = { navController.navigate(OnboardingAbout) }
                    )
                }
                composable<OnboardingAbout> {
                    OnboardingScreen(
                        text = stringResource(Res.string.onboarding_about),
                        subtitle = stringResource(Res.string.onboarding_about_subtitle),
                        buttonText = stringResource(Res.string.onboarding_next_button),
                        icon = Icons.Default.Info,
                        onNext = { navController.navigate(OnboardingDone) }
                    )
                }
                composable<OnboardingDone> {
                    OnboardingScreen(
                        text = stringResource(Res.string.onboarding_done),
                        subtitle = stringResource(Res.string.onboarding_done_subtitle),
                        buttonText = stringResource(Res.string.onboarding_next_button),
                        icon = Icons.Default.Home,
                        onNext = {
                            navController.navigate(Dashboard) {
                                popUpTo<OnboardingWelcome> {
                                    inclusive = true
                                }
                            }
                        }
                    )
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
                    typeMap = mapOf(typeOf<DeviceId>() to DeviceIdNavType),
                ) {
                    LightDetailsScreen(
                        deviceId = it.toRoute<LightDetails>().deviceId,
                        onNavigateUp = { navController.navigateUp() },
                    )
                }
                composable<CameraDetails>(
                    typeMap = mapOf(typeOf<DeviceId>() to DeviceIdNavType)
                ) {
                    CameraDetailsScreen(
                        deviceId = it.toRoute<CameraDetails>().deviceId,
                        onNavigateUp = { navController.navigateUp() },
                        onNavigateToRename = { deviceId ->
                            navController.navigate(RenameDevice(deviceId))
                        },
                    )
                }
                composable<RenameDevice>(
                    typeMap = mapOf(typeOf<DeviceId>() to DeviceIdNavType)
                ) {
                    RenameDeviceScreen(
                        deviceId = it.toRoute<RenameDevice>().deviceId,
                        onDismiss = {
                            navController.navigateUp()
                        },
                    )
                }
            }
        }
    }
}
