package com.kotlinconf.workshop.househelper.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.border
import androidx.compose.ui.util.fastCoerceAtLeast
import androidx.compose.ui.graphics.graphicsLayer
import com.kotlinconf.workshop.househelper.Device
import com.kotlinconf.workshop.househelper.DeviceConstants
import com.kotlinconf.workshop.househelper.DeviceId
import com.kotlinconf.workshop.househelper.Room
import com.kotlinconf.workshop.househelper.RoomId
import com.kotlinconf.workshop.househelper.Toggleable
import com.kotlinconf.workshop.househelper.LightDevice
import com.kotlinconf.workshop.househelper.CameraDevice
import com.kotlinconf.workshop.househelper.HumidityDevice
import com.kotlinconf.workshop.househelper.ThermostatDevice
import com.kotlinconf.workshop.househelper.Sensor
import kotlin.math.roundToInt
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun DashboardScreen(
    onNavigateToLightDetails: (DeviceId) -> Unit,
    onNavigateToCameraDetails: (DeviceId) -> Unit,
    viewModel: DashboardViewModel = koinViewModel(),
) {
    val rooms by viewModel.rooms.collectAsState()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(rooms) { room ->
            val devices by viewModel.getDevicesForRoom(room.id).collectAsState()
            RoomSection(
                room = room,
                devices = devices,
                onClick = { viewModel.onDeviceClicked(it) },
                onLongClick = { device ->
                    when (device) {
                        is LightDevice -> onNavigateToLightDetails(device.deviceId)
                        is CameraDevice -> onNavigateToCameraDetails(device.deviceId)
                        else -> { /* Other device types are not navigable */
                        }
                    }
                },
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RoomSection(
    room: Room,
    devices: List<Device>,
    onClick: (Device) -> Unit,
    onLongClick: (Device) -> Unit,
) {
    var expanded by remember { mutableStateOf(true) }
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val rotation by animateFloatAsState(if (expanded) 0f else -90f)
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = if (expanded) "Collapse" else "Expand",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .graphicsLayer { rotationZ = rotation }
            )
            Text(
                text = room.name,
                style = MaterialTheme.typography.titleLarge,
            )
        }
        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally),
            ) {
                devices.forEach { device ->
                    DeviceCard(
                        device = device,
                        onClick = { onClick(device) },
                        onLongClick = { onLongClick(device) },
                    )
                }
            }
        }
    }
}

@Composable
private fun DeviceCard(
    device: Device,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardShape = RoundedCornerShape(12.dp)
    val backgroundColor by animateColorAsState(
        targetValue = when {
            device is Toggleable && device.isOn -> MaterialTheme.colorScheme.primaryContainer
            device is Toggleable -> MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
            else -> MaterialTheme.colorScheme.surfaceVariant
        }
    )

    Box(
        modifier = modifier
            .clip(cardShape)
            .widthIn(max = 140.dp)
            .height(140.dp)
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                shape = cardShape
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
    ) {
        if (device is LightDevice) {
            val height by animateDpAsState(
                if (device.isOn) 0.dp
                else 140.dp * (device.brightness.toFloat() / DeviceConstants.Light.MAX_BRIGHTNESS)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .background(
                        Color(
                            red = device.color.red,
                            green = device.color.green,
                            blue = device.color.blue,
                            alpha = (255 * 0.7f).toInt()
                        )
                    )
                    .align(Alignment.BottomCenter)
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
            )
        }
        val contentColor by animateColorAsState(
            targetValue = when {
                device is Toggleable && device.isOn -> MaterialTheme.colorScheme.primary
                device is Toggleable -> MaterialTheme.colorScheme.onSurface
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(device.iconResource),
                    contentDescription = device.name,
                    modifier = Modifier.size(40.dp),
                    colorFilter = ColorFilter.tint(contentColor),
                    alpha = if (device is Toggleable && !device.isOn) 0.6f else 1.0f
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = device.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = contentColor
                )
            }
            when (device) {
                is LightDevice, is CameraDevice -> {
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onLongClick
                            )
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "View more",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                        )
                    }
                }

                is ThermostatDevice -> {
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${device.currentValue.roundToInt()}°C",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                is HumidityDevice -> {
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${device.currentValue.roundToInt()}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                else -> { /* Other device types are not navigable */
                }
            }
        }
    }
}
