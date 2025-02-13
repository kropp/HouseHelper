package com.kotlinconf.workshop.househelper.devices

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.graphics.Color
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinconf.workshop.househelper.DeviceConstants
import com.kotlinconf.workshop.househelper.DeviceId
import com.kotlinconf.workshop.househelper.LightDevice
import com.kotlinconf.workshop.househelper.RGBColor
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
                var isDragging by remember { mutableStateOf(false) }

                LaunchedEffect(localBrightness) {
                    if (!isDragging) {
                        delay(50)
                    }
                    viewModel.updateBrightness(localBrightness)
                }

                Text(
                    text = "${localBrightness.toInt()}%",
                    style = MaterialTheme.typography.bodyLarge
                )

                Box(
                    modifier = Modifier
                        .width(160.dp)
                        .height(400.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .background(MaterialTheme.colorScheme.surface)
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                while (true) {
                                    val event = awaitPointerEvent()
                                    val firstChange = event.changes.first()
                                    val position = firstChange.position
                                    when (event.type) {
                                        PointerEventType.Press -> {
                                            if (firstChange.pressed) {
                                                val newBrightness = ((1f - position.y / size.height) * DeviceConstants.Light.MAX_BRIGHTNESS).toInt()
                                                localBrightness = newBrightness.coerceIn(DeviceConstants.Light.MIN_BRIGHTNESS, DeviceConstants.Light.MAX_BRIGHTNESS)
                                            }
                                        }
                                        PointerEventType.Move -> {
                                            if (firstChange.pressed) {
                                                // Only set dragging if there's actual movement
                                                if (firstChange.position != firstChange.previousPosition) {
                                                    isDragging = true
                                                }
                                                val newBrightness = ((1f - position.y / size.height) * DeviceConstants.Light.MAX_BRIGHTNESS).toInt()
                                                localBrightness = newBrightness.coerceIn(DeviceConstants.Light.MIN_BRIGHTNESS, DeviceConstants.Light.MAX_BRIGHTNESS)
                                            }
                                        }
                                        PointerEventType.Release -> {
                                            isDragging = false
                                        }
                                        else -> Unit
                                    }
                                }
                            }
                        }
                ) {

                    val animatedHeight by animateFloatAsState(
                        targetValue = localBrightness.toFloat() / DeviceConstants.Light.MAX_BRIGHTNESS,
                        animationSpec = tween(
                            durationMillis = if (isDragging) 0 else 200
                        ),
                        label = "brightness height"
                    )
                    val animatedAlpha by animateFloatAsState(
                        targetValue = if (light.isOn) 1f else 0.5f,
                        animationSpec = tween(durationMillis = 200),
                        label = "brightness alpha"
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp * animatedHeight)
                            .background(
                                color = Color(
                                    red = light.color.red,
                                    green = light.color.green,
                                    blue = light.color.blue,
                                    alpha = (255 * animatedAlpha).toInt()
                                )
                            )
                            .align(Alignment.BottomCenter)
                    )
                }

                Text(
                    text = "Color",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 32.dp)
                ) {
                    items(DeviceConstants.Light.PREDEFINED_COLORS) { color ->
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(color.red, color.green, color.blue))
                                .border(
                                    width = 2.dp,
                                    color = if (color == light.color) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { viewModel.updateColor(color) }
                        )
                    }
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

    fun updateColor(color: RGBColor) {
        light.value?.let { light ->
            houseService.updateDevice(light.copy(color = color))
        }
    }
}
