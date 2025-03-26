package com.kotlinconf.workshop.househelper

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.compose.serialization.serializers.SnapshotStateListSerializer
import com.kotlinconf.workshop.househelper.dashboard.DashboardScreen
import com.kotlinconf.workshop.househelper.devices.CameraDetailsScreen
import com.kotlinconf.workshop.househelper.devices.LightDetailsScreen
import com.kotlinconf.workshop.househelper.devices.RenameDeviceScreen
import com.kotlinconf.workshop.househelper.di.AppGraph
import com.kotlinconf.workshop.househelper.navigation.CameraDetails
import com.kotlinconf.workshop.househelper.navigation.Dashboard
import com.kotlinconf.workshop.househelper.navigation.LightDetails
import com.kotlinconf.workshop.househelper.navigation.OnboardingAbout
import com.kotlinconf.workshop.househelper.navigation.OnboardingDone
import com.kotlinconf.workshop.househelper.navigation.OnboardingWelcome
import com.kotlinconf.workshop.househelper.navigation.RenameDevice
import com.kotlinconf.workshop.househelper.navigation.Screen
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import househelper.shared.generated.resources.Res
import househelper.shared.generated.resources.onboarding_about
import househelper.shared.generated.resources.onboarding_about_subtitle
import househelper.shared.generated.resources.onboarding_done
import househelper.shared.generated.resources.onboarding_done_subtitle
import househelper.shared.generated.resources.onboarding_next_button
import househelper.shared.generated.resources.onboarding_welcome
import househelper.shared.generated.resources.onboarding_welcome_subtitle
import kotlinx.coroutines.channels.Channel
import org.jetbrains.compose.resources.stringResource

/**
 * Handles links in the househelper://{type}/{id} format
 *
 * Examples:
 * househelper://light/living_room_floor_lamp
 * househelper://camera/bathroom_security_camera
 */
fun navigateToDeepLink(uri: String) {
    if (!uri.startsWith("househelper://")) return

    // TODO Bonus Task 3: parse deep links
}

private val deeplinkRequests = Channel<Screen>(capacity = 1)

@Composable
fun App(appGraph: AppGraph) {
    // TODO Task 14: customize theme
    MaterialTheme {
        CompositionLocalProvider(LocalMetroViewModelFactory provides appGraph.metroViewModelFactory) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                val backStack = rememberSerializable(serializer = SnapshotStateListSerializer()) {
                    mutableStateListOf<Screen>(OnboardingWelcome)
                }

                // TODO Bonus Task 3: receive and handle deep link requests

                NavDisplay(
                    backStack = backStack,
                    onBack = { backStack.removeLastOrNull() },
                    entryProvider = entryProvider {
                        entry<OnboardingWelcome> {
                            OnboardingScreen(
                                text = stringResource(Res.string.onboarding_welcome),
                                subtitle = stringResource(Res.string.onboarding_welcome_subtitle),
                                buttonText = stringResource(Res.string.onboarding_next_button),
                                icon = Icons.Default.Favorite,
                                onNext = { backStack.add(OnboardingAbout) }
                            )
                        }
                        entry<OnboardingAbout> {
                            OnboardingScreen(
                                text = stringResource(Res.string.onboarding_about),
                                subtitle = stringResource(Res.string.onboarding_about_subtitle),
                                buttonText = stringResource(Res.string.onboarding_next_button),
                                icon = Icons.Default.Info,
                                onNext = { backStack.add(OnboardingDone) }
                            )
                        }
                        entry<OnboardingDone> {
                            OnboardingScreen(
                                text = stringResource(Res.string.onboarding_done),
                                subtitle = stringResource(Res.string.onboarding_done_subtitle),
                                buttonText = stringResource(Res.string.onboarding_next_button),
                                icon = Icons.Default.Home,
                                onNext = {
                                    backStack.clear()
                                    backStack.add(Dashboard)
                                }
                            )
                        }
                        entry<Dashboard> {
                            DashboardScreen(
                                onNavigateToLightDetails = { deviceId ->
                                    backStack.add(LightDetails(deviceId))
                                },
                                onNavigateToCameraDetails = { deviceId ->
                                    backStack.add(CameraDetails(deviceId))
                                }
                            )
                        }
                        entry<LightDetails> {
                            LightDetailsScreen(
                                deviceId = it.deviceId,
                                onNavigateUp = { backStack.removeLastOrNull() },
                            )
                        }
                        entry<CameraDetails> {
                            CameraDetailsScreen(
                                deviceId = it.deviceId,
                                onNavigateUp = { backStack.removeLastOrNull() },
                                onNavigateToRename = { deviceId ->
                                    backStack.add(RenameDevice(deviceId))
                                },
                            )
                        }
                        entry<RenameDevice> {
                            RenameDeviceScreen(
                                deviceId = it.deviceId,
                                onDismiss = {
                                    backStack.removeLastOrNull()
                                },
                            )
                        }
                    },
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator(),
                    ),
                )
            }
        }
    }
}
