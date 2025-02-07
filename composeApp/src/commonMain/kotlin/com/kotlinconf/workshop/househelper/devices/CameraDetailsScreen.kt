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
import com.kotlinconf.workshop.househelper.CameraDevice
import com.kotlinconf.workshop.househelper.data.HouseService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraDetailsScreen(
    deviceId: DeviceId,
    onNavigateUp: () -> Unit,
    viewModel: CameraDetailsViewModel = koinViewModel()
) {
    val device by viewModel.getCamera(deviceId).collectAsState(null)

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TopAppBar(
            title = { Text(text = "Camera Details") },
            navigationIcon = {
                IconButton(onClick = onNavigateUp) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        device?.let { camera ->
            Text(
                text = camera.name,
                style = MaterialTheme.typography.headlineMedium
            )

            Switch(
                checked = camera.isOn,
                onCheckedChange = { viewModel.toggleCamera(camera.deviceId) }
            )

            // Additional camera-specific UI elements can be added here
            Text(
                text = if (camera.isOn) "Camera is streaming" else "Camera is off",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

class CameraDetailsViewModel(
    private val houseService: HouseService,
) : ViewModel() {
    fun getCamera(deviceId: DeviceId): StateFlow<CameraDevice?> = houseService.getDevice(deviceId)
        .map { it as? CameraDevice }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun toggleCamera(deviceId: DeviceId) {
        getCamera(deviceId).value?.let { camera ->
            houseService.updateDevice(camera.copy(isOn = !camera.isOn))
        }
    }
}
