package com.kotlinconf.workshop.househelper.devices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kotlinconf.workshop.househelper.DeviceId
import com.kotlinconf.workshop.househelper.VideoPlayer
import com.kotlinconf.workshop.househelper.rememberVideoPlayerState
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

            Text(
                text = if (camera.isOn) "Camera is streaming" else "Camera is off",
                style = MaterialTheme.typography.bodyLarge
            )

            VideoPlayer(
                url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                modifier = Modifier.fillMaxSize(),
                videoPlayerState = rememberVideoPlayerState(),
            )
        }
    }
}

