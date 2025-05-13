package com.kotlinconf.workshop.househelper.dashboard

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kotlinconf.workshop.househelper.DeviceId
import househelper.composeapp.generated.resources.Res
import househelper.composeapp.generated.resources.dashboard_tab_rooms
import househelper.composeapp.generated.resources.dashboard_tab_settings
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DashboardScreen(
    onNavigateToLightDetails: (DeviceId) -> Unit,
    onNavigateToCameraDetails: (DeviceId) -> Unit,
    viewModel: DashboardViewModel = koinViewModel(),
) {
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                text = { Text(stringResource(Res.string.dashboard_tab_rooms)) }
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                text = { Text(stringResource(Res.string.dashboard_tab_settings)) }
            )
        }

        AnimatedContent(
            targetState = selectedTabIndex,
            transitionSpec = {
                val direction = if (targetState > initialState) 1 else -1
                slideInHorizontally { width -> direction * width } togetherWith
                    slideOutHorizontally { width -> -direction * width }
            }
        ) { tabIndex ->
            when (tabIndex) {
                0 -> {
                    val rooms by viewModel.rooms.collectAsStateWithLifecycle()
                    RoomsContent(
                        rooms = rooms,
                        onNavigateToLightDetails = onNavigateToLightDetails,
                        onNavigateToCameraDetails = onNavigateToCameraDetails,
                    )
                }

                1 -> SettingsContent()
            }
        }
    }
}
