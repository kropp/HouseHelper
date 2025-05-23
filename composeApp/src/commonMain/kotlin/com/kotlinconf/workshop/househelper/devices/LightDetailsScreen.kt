package com.kotlinconf.workshop.househelper.devices

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kotlinconf.workshop.househelper.DeviceConstants
import com.kotlinconf.workshop.househelper.DeviceId
import com.kotlinconf.workshop.househelper.LightDevice
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LightDetailsScreen(
    deviceId: DeviceId,
    onNavigateUp: () -> Unit,
    viewModel: LightDetailsViewModel = koinViewModel { parametersOf(deviceId) }
) {
    val device = viewModel.light.collectAsStateWithLifecycle().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { device?.let { Text(text = it.name) } },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            device?.let { light ->
                Switch(
                    checked = light.isOn,
                    onCheckedChange = { viewModel.toggleLight() },
                )

                var localBrightness by remember { mutableStateOf(if (light.isOn) light.brightness else null) }
                var isDragging by remember { mutableStateOf(false) }

                LaunchedEffect(light.isOn) {
                    if (light.isOn) {
                        localBrightness = light.brightness
                    }
                }
                LaunchedEffect(localBrightness) {
                    localBrightness?.let { value ->
                        delay(50)
                        viewModel.updateBrightness(value)
                    }
                }

                Text(
                    text = if (light.isOn) "${localBrightness}%" else "0%",
                    style = MaterialTheme.typography.bodyLarge
                )

                LightSlider(
                    light = light,
                    localBrightness = localBrightness ?: 0,
                    onChangeBrightness = { localBrightness = it },
                    isDragging = isDragging,
                    onDragging = { isDragging = it },
                )

                Text(
                    text = "Color",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 16.dp)
                        .semantics { heading() }
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .selectableGroup()
                ) {
                    DeviceConstants.Light.PREDEFINED_COLORS.forEach { (color, name) ->
                        val selected = color == light.color
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .border(
                                    width = 4.dp,
                                    color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
                                    shape = MaterialTheme.shapes.medium
                                )
                                .clip(MaterialTheme.shapes.medium)
                                .background(color)
                                .selectable(
                                    selected = selected,
                                    onClick = { viewModel.updateColor(color) }
                                )
                                .semantics {
                                    contentDescription = name
                                }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LightSlider(
    light: LightDevice,
    localBrightness: Int,
    onChangeBrightness: (Int) -> Unit,
    isDragging: Boolean,
    onDragging: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .width(160.dp)
            .height(400.dp)
            .clip(MaterialTheme.shapes.large)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                shape = MaterialTheme.shapes.large
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
                                    val newBrightness =
                                        ((1f - position.y / size.height) * DeviceConstants.Light.MAX_BRIGHTNESS).toInt()
                                            .coerceIn(
                                                DeviceConstants.Light.MIN_BRIGHTNESS,
                                                DeviceConstants.Light.MAX_BRIGHTNESS
                                            )
                                    onChangeBrightness(newBrightness)
                                }
                            }

                            PointerEventType.Move -> {
                                if (firstChange.pressed) {
                                    // Only set dragging if there's actual movement
                                    if (firstChange.position != firstChange.previousPosition) {
                                        onDragging(true)
                                    }
                                    val newBrightness =
                                        ((1f - position.y / size.height) * DeviceConstants.Light.MAX_BRIGHTNESS).toInt()
                                            .coerceIn(
                                                DeviceConstants.Light.MIN_BRIGHTNESS,
                                                DeviceConstants.Light.MAX_BRIGHTNESS
                                            )
                                    onChangeBrightness(newBrightness)
                                }
                            }

                            PointerEventType.Release -> {
                                onDragging(false)
                            }

                            else -> Unit
                        }
                    }
                }
            }
    ) {

        val animatedHeight by animateFloatAsState(
            targetValue = if (light.isOn) localBrightness.toFloat() / DeviceConstants.Light.MAX_BRIGHTNESS else 0f,
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
                    color = light.color.copy(
                        alpha = animatedAlpha
                    )
                )
                .align(Alignment.BottomCenter)
        )
    }
}
