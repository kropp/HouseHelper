package com.kotlinconf.workshop.househelper.devices

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kotlinconf.workshop.househelper.CameraDevice
import com.kotlinconf.workshop.househelper.DeviceId
import com.kotlinconf.workshop.househelper.RoomId
import com.kotlinconf.workshop.househelper.data.DemoHouseService
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraDetailsScreen(
    deviceId: DeviceId,
    onNavigateUp: () -> Unit,
    onNavigateToRename: (DeviceId) -> Unit,
    // TODO Task 8: use Koin APIs
    viewModel: CameraDetailsViewModel = viewModel { CameraDetailsViewModel(DemoHouseService(), deviceId) },
) {
    // TODO Task 5: replace this hardcoded value with data from the ViewModel
    val device = CameraDevice(deviceId, "Fake Camera", roomId = RoomId(""))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { device?.let { Text(text = it.name) } },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { device?.deviceId?.let { onNavigateToRename(it) } }
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit name")
                    }
                },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            device?.let { camera ->
                Switch(
                    checked = camera.isOn,
                    onCheckedChange = {
                        // TODO Task 5: toggle the camera state through the ViewModel
                    }
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (camera.isOn) {
                        val infiniteTransition = rememberInfiniteTransition()
                        val alpha by infiniteTransition.animateFloat(
                            initialValue = 0f, targetValue = 1f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1000, easing = LinearEasing),
                                repeatMode = RepeatMode.Reverse
                            ),
                        )

                        Box(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .clip(CircleShape)
                                .alpha(alpha)
                                .background(Color.Red)
                                .size(12.dp)
                        )
                    }

                    Text(
                        text = if (camera.isOn) "Camera is streaming" else "Camera is off",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                if (camera.isOn) {
                    // TODO Task 6: display the camera image here
                }
            }
        }
    }
}
