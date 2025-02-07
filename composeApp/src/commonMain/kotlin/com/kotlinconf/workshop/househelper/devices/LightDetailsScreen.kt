package com.kotlinconf.workshop.househelper.devices

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinconf.workshop.househelper.DeviceId
import com.kotlinconf.workshop.househelper.LightDevice
import com.kotlinconf.workshop.househelper.data.HouseService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LightDetailsScreen(
    deviceId: DeviceId,
    onNavigateUp: () -> Unit,
    viewModel: LightDetailsViewModel = koinViewModel()
) {
    val device by viewModel.getLight(deviceId).collectAsState(null)

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TopAppBar(
            title = { Text(text = "Light Details") },
            navigationIcon = {
                IconButton(onClick = onNavigateUp) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        device?.let { light ->
            Text(
                text = light.name,
                style = MaterialTheme.typography.headlineMedium
            )

            Switch(
                checked = light.isOn,
                onCheckedChange = { viewModel.toggleLight(light.deviceId) }
            )
        }
    }
}

class LightDetailsViewModel(
    private val houseService: HouseService,
) : ViewModel() {
    fun getLight(deviceId: DeviceId): StateFlow<LightDevice?> = houseService.getDevice(deviceId)
        .map { it as? LightDevice }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun toggleLight(deviceId: DeviceId) {
        getLight(deviceId).value?.let { light ->
            houseService.updateDevice(light.copy(isOn = !light.isOn))
        }
    }
}
