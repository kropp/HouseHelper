package com.kotlinconf.workshop.househelper.device

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinconf.workshop.househelper.Device
import com.kotlinconf.workshop.househelper.DeviceId
import com.kotlinconf.workshop.househelper.data.HouseService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceManagementScreen(
    deviceId: DeviceId,
    onNavigateUp: () -> Unit,
    viewModel: DeviceManagementViewModel = koinViewModel()
) {
    val device by viewModel.getDevice(deviceId).collectAsState(null)

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TopAppBar(
            title = { Text(text = "Device Management") },
            navigationIcon = {
                IconButton(onClick = onNavigateUp) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        device?.let { currentDevice ->
            Text(
                text = currentDevice.name,
                style = MaterialTheme.typography.headlineMedium
            )

            // Add more device management UI elements here
        }
    }
}

class DeviceManagementViewModel(
    private val houseService: HouseService,
) : ViewModel() {
    fun getDevice(deviceId: DeviceId): StateFlow<Device?> = houseService.getDevice(deviceId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
}
