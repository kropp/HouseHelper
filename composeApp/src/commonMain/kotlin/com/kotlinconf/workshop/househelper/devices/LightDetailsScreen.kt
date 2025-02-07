package com.kotlinconf.workshop.househelper.devices

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinconf.workshop.househelper.DeviceId
import com.kotlinconf.workshop.househelper.LightDevice
import com.kotlinconf.workshop.househelper.data.HouseService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LightDetailsScreen(
    deviceId: DeviceId,
    onNavigateUp: () -> Unit,
    viewModel: LightDetailsViewModel = koinViewModel { parametersOf(deviceId) }
) {
    val device by viewModel.light.collectAsState(null)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopAppBar(
            title = { device?.let { Text(text = it.name) } },
            navigationIcon = {
                IconButton(onClick = onNavigateUp) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            device?.let { light ->
                var localBrightness by remember { mutableIntStateOf(light.brightness) }

                LaunchedEffect(localBrightness) {
                    if (light.isOn) {
                        delay(100)
                        viewModel.updateBrightness(localBrightness)
                    }
                }

                Text(
                    text = "Brightness: ${localBrightness.toInt()}%",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.graphicsLayer { alpha = if (light.isOn) 1f else 0.5f }
                )

                Box(
                    modifier = Modifier
                        .width(160.dp)
                        .height(400.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .graphicsLayer { alpha = if (light.isOn) 1f else 0.5f }
                        .let { modifier ->
                            if (light.isOn) {
                                modifier.pointerInput(Unit) {
                                    awaitPointerEventScope {
                                        while (true) {
                                            val event = awaitPointerEvent()
                                            val position = event.changes.first().position
                                            when (event.type) {
                                                androidx.compose.ui.input.pointer.PointerEventType.Press -> {
                                                    val newBrightness = ((1f - position.y / size.height) * 100f).toInt()
                                                    localBrightness = newBrightness.coerceIn(0, 100)
                                                }
                                                androidx.compose.ui.input.pointer.PointerEventType.Move -> {
                                                    if (event.changes.first().pressed) {
                                                        val newBrightness = ((1f - position.y / size.height) * 100f).toInt()
                                                        localBrightness = newBrightness.coerceIn(0, 100)
                                                    }
                                                }
                                                else -> Unit
                                            }
                                        }
                                    }
                                }
                            } else modifier
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp * (localBrightness.toFloat() / 100f))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .align(Alignment.BottomCenter)
                    )
                }
            }
        }

        device?.let { light ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Switch(
                    checked = light.isOn,
                    onCheckedChange = { viewModel.toggleLight() }
                )
            }
        }
    }
}

class LightDetailsViewModel(
    private val houseService: HouseService,
    deviceId: DeviceId,
) : ViewModel() {
    val light: StateFlow<LightDevice?> = houseService.getDevice(deviceId)
        .map { device -> device as? LightDevice }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun toggleLight() {
        light.value?.let { light ->
            houseService.updateDevice(light.copy(isOn = !light.isOn))
        }
    }

    fun updateBrightness(brightness: Int) {
        light.value?.let { light ->
            houseService.updateDevice(light.copy(brightness = brightness))
        }
    }
}
