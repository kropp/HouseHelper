package com.kotlinconf.workshop.househelper

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kotlinconf.workshop.househelper.dashboard.DashboardScreen
import com.kotlinconf.workshop.househelper.devices.CameraDetailsScreen
import com.kotlinconf.workshop.househelper.devices.RenameDeviceScreen
import com.kotlinconf.workshop.househelper.navigation.CameraDetails
import com.kotlinconf.workshop.househelper.navigation.Dashboard
import com.kotlinconf.workshop.househelper.navigation.DeviceIdNavType
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
import kotlin.collections.set
import kotlin.reflect.typeOf

fun navigateToDeepLink(uri: String) {
    deepLinkUris.trySend(uri)
}

private val deepLinkUris = Channel<String>(capacity = 1)

@Composable
@Preview
fun App() {
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
                            // TODO
                        },
                        onNavigateToCameraDetails = { deviceId ->
                            navController.navigate(CameraDetails(deviceId))
                        }
                    )
                }
                composable<CameraDetails>(
                    typeMap = mapOf(typeOf<DeviceId>() to DeviceIdNavType)
                ) { backstackEntry ->
                    val results = remember(backstackEntry) {
                        backstackEntry.savedStateHandle.getStateFlow<String?>("newName", null)
                    }

                    val newName by results.collectAsStateWithLifecycle()
                    CameraDetailsScreen(
                        deviceId = backstackEntry.toRoute<CameraDetails>().deviceId,
                        newName = newName,
                        onNewNameProcessed = { backstackEntry.savedStateHandle["newName"] = null },
                        onNavigateToRename = { currentName ->
                            navController.navigate(RenameDevice(currentName))
                        },
                        onNavigateUp = { navController.navigateUp() }
                    )
                }
                dialog<RenameDevice> {
                    RenameDeviceScreen(
                        currentName = it.toRoute<RenameDevice>().currentName,
                        onDismiss = { newName ->
                            navController.previousBackStackEntry?.savedStateHandle?.set("newName", newName)
                            navController.navigateUp()
                        },
                    )
                }
            }
        }
    }
}
