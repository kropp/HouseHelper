package com.kotlinconf.workshop.househelper.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kotlinconf.workshop.househelper.CameraDevice
import com.kotlinconf.workshop.househelper.Device
import com.kotlinconf.workshop.househelper.DeviceConstants
import com.kotlinconf.workshop.househelper.DeviceId
import com.kotlinconf.workshop.househelper.HumidityDevice
import com.kotlinconf.workshop.househelper.LightDevice
import com.kotlinconf.workshop.househelper.Room
import com.kotlinconf.workshop.househelper.RoomId
import com.kotlinconf.workshop.househelper.ThermostatDevice
import com.kotlinconf.workshop.househelper.Toggleable
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.roundToInt

@Composable
fun DashboardScreen(
    onNavigateToLightDetails: (DeviceId) -> Unit,
    onNavigateToCameraDetails: (DeviceId) -> Unit,
    viewModel: DashboardViewModel = koinViewModel(),
) {
    val rooms by viewModel.rooms.collectAsState()

    DashboardScreen(
        rooms = rooms,
        onNavigateToLightDetails = onNavigateToLightDetails,
        onNavigateToCameraDetails = onNavigateToCameraDetails,
    )
}

@Composable
fun DashboardScreen(
    rooms: List<Room>,
    onNavigateToLightDetails: (DeviceId) -> Unit,
    onNavigateToCameraDetails: (DeviceId) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = WindowInsets.safeDrawing.asPaddingValues(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(rooms) { room ->
            val roomViewModel: RoomViewModel = koinViewModel(
                key = room.id.value,
                parameters = { parametersOf(room.id) },
            )
            val devices by roomViewModel.devices.collectAsStateWithLifecycle()
            val expanded by roomViewModel.expanded.collectAsStateWithLifecycle()

            RoomSection(
                room = room,
                expanded = expanded,
                devices = devices,
                onExpand = { isExpanded -> roomViewModel.expand(isExpanded) },
                onClick = { device -> roomViewModel.onDeviceClicked(device) },
                onLongClick = { device ->
                    when (device) {
                        is LightDevice -> onNavigateToLightDetails(device.deviceId)
                        is CameraDevice -> onNavigateToCameraDetails(device.deviceId)
                        else -> {
                            /* Other device types are not navigable */
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
    expanded: Boolean,
    devices: List<Device>,
    onExpand: (Boolean) -> Unit,
    onClick: (Device) -> Unit,
    onLongClick: (Device) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onExpand(!expanded) }
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
    Card(
        modifier = modifier.size(140.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (device is LightDevice) {
                val height by animateFloatAsState(
                    if (!device.isOn) 0f
                    else (device.brightness.toFloat() / DeviceConstants.Light.MAX_BRIGHTNESS)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(height)
                        .background(device.color.copy(alpha = 0.3f))
                        .align(Alignment.BottomCenter)
                )
            }

            DeviceCardContent(
                device = device,
                modifier = if (device is Toggleable) {
                    Modifier.combinedClickable(onClick = onClick, onLongClick = onLongClick)
                } else {
                    Modifier
                },
                bottomText = when (device) {
                    is ThermostatDevice -> "${device.currentValue.roundToInt()}°C"
                    is HumidityDevice -> "${device.currentValue.roundToInt()}%"
                    else -> null
                }
            )
        }
    }
}

@Composable
private fun DeviceCardContent(
    device: Device,
    modifier: Modifier = Modifier,
    bottomText: String? = null,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val contentColor = when {
            device is LightDevice && device.isOn -> device.color
            device is Toggleable && device.isOn -> MaterialTheme.colorScheme.primary
            device is Toggleable -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            else -> MaterialTheme.colorScheme.onSurface
        }

        Column(
            Modifier.weight(1f).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(device.iconResource),
                contentDescription = if (device is Toggleable) {
                    "Device ${device.name} is ${if (device.isOn) "ON" else "OFF"}"
                } else {
                    device.name
                },
                modifier = Modifier.size(40.dp),
                colorFilter = ColorFilter.tint(contentColor),
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = device.name,
                style = MaterialTheme.typography.bodyMedium,
                color = contentColor,
                modifier = Modifier.padding(horizontal = 6.dp),
                textAlign = TextAlign.Center,
            )
        }
        if (bottomText != null) {
            HorizontalDivider(
                Modifier.fillMaxWidth(),
                1.dp,
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = bottomText,
                    style = MaterialTheme.typography.labelMedium,
                    color = contentColor,
                )
            }
        }

    }
}
