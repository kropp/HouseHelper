package com.kotlinconf.workshop.househelper

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.uri.UriUtils
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.kotlinconf.workshop.househelper.dashboard.DashboardScreen
import com.kotlinconf.workshop.househelper.devices.CameraDetailsScreen
import com.kotlinconf.workshop.househelper.devices.LightDetailsScreen
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
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) {
            AppDarkColorScheme
        } else {
            AppLightColorScheme
        },
        shapes = AppShapes,
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()

            LaunchedEffect(Unit) {
                while (true) {
                    val uri = deepLinkUris.receive()
                    navController.navigate(deepLink = UriUtils.parse(uri))
                }
            }

            NavHost(navController, startDestination = StartScreens) {
                navigation<StartScreens>(startDestination = Welcome) {
                    composable<Welcome> {
                        Onboarding(
                            text = stringResource(Res.string.onboarding_welcome),
                            subtitle = stringResource(Res.string.onboarding_welcome_subtitle),
                            buttonText = stringResource(Res.string.onboarding_next_button),
                            icon = Icons.Default.Favorite,
                            onNext = { navController.navigate(Onboarding) }
                        )
                    }
                    composable<Onboarding> {
                        Onboarding(
                            text = stringResource(Res.string.onboarding_about),
                            subtitle = stringResource(Res.string.onboarding_about_subtitle),
                            buttonText = stringResource(Res.string.onboarding_next_button),
                            icon = Icons.Default.Info,
                            onNext = { navController.navigate(OnboardingDone) }
                        )
                    }
                    composable<OnboardingDone> {
                        Onboarding(
                            text = stringResource(Res.string.onboarding_done),
                            subtitle = stringResource(Res.string.onboarding_done_subtitle),
                            buttonText = stringResource(Res.string.onboarding_next_button),
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
                    typeMap = mapOf(typeOf<DeviceId>() to DeviceIdNavType),
                    deepLinks = listOf(
                        navDeepLink {
                            // TODO: make this open a stack of screens
                            uriPattern = "househelper://light/{deviceId}"
                        }
                    )
                ) { backstackEntry ->
                    val deviceId = backstackEntry.toRoute<LightDetails>().deviceId

                    val results = remember(backstackEntry) {
                        backstackEntry.savedStateHandle.getStateFlow<String?>("newName", null)
                    }
                    val newName by results.collectAsState()

                    LightDetailsScreen(
                        deviceId = deviceId,
                        newName = newName,
                        onNewNameProcessed = { backstackEntry.savedStateHandle["newName"] = null },
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
