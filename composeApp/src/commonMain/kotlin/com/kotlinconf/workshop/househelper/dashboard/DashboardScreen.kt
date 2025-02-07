package com.kotlinconf.workshop.househelper.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.util.fastCoerceAtLeast
import androidx.compose.ui.graphics.graphicsLayer
import com.kotlinconf.workshop.househelper.Device
import com.kotlinconf.workshop.househelper.DeviceId
import com.kotlinconf.workshop.househelper.Room
import com.kotlinconf.workshop.househelper.RoomId
import com.kotlinconf.workshop.househelper.Toggleable
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun DashboardScreen(
    onNavigateToDevice: (DeviceId) -> Unit,
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
                    onNavigateToDevice(device.deviceId)
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
    Box(
        modifier = modifier
            .clip(cardShape)
            .widthIn(max = 140.dp)
            .height(140.dp)
            .background(
                when {
                    device is Toggleable && device.isOn -> MaterialTheme.colorScheme.primaryContainer
                    device is Toggleable -> MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
            )
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
                onLongClick = onLongClick,
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(device.iconResource),
                contentDescription = device.name,
                modifier = Modifier.size(40.dp),
                colorFilter = when {
                    device is Toggleable && device.isOn -> ColorFilter.tint(MaterialTheme.colorScheme.primary)
                    device is Toggleable -> ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                    else -> ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
                },
                alpha = if (device is Toggleable && !device.isOn) 0.6f else 1.0f
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = device.name,
                style = MaterialTheme.typography.bodySmall,
                color = when {
                    device is Toggleable && device.isOn -> MaterialTheme.colorScheme.onPrimaryContainer
                    device is Toggleable -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}
