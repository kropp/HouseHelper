package com.kotlinconf.workshop.househelper.dashboard

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kotlinconf.workshop.househelper.CameraDevice
import com.kotlinconf.workshop.househelper.Device
import com.kotlinconf.workshop.househelper.DeviceConstants
import com.kotlinconf.workshop.househelper.DeviceId
import com.kotlinconf.workshop.househelper.HumidityDevice
import com.kotlinconf.workshop.househelper.LightDevice
import com.kotlinconf.workshop.househelper.Room
import com.kotlinconf.workshop.househelper.RoomId
import com.kotlinconf.workshop.househelper.SwitchDevice
import com.kotlinconf.workshop.househelper.ThermostatDevice
import com.kotlinconf.workshop.househelper.Toggleable
import com.kotlinconf.workshop.househelper.data.DemoHouseService
import com.kotlinconf.workshop.househelper.utils.onRightClick
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt

@Composable
fun RoomsContent(
    rooms: List<Room>,
    onNavigateToLightDetails: (DeviceId) -> Unit,
    onNavigateToCameraDetails: (DeviceId) -> Unit,
) {
    // TODO Task 15: update content inset
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(rooms) { room ->
            // TODO Task 8: use Koin APIs
            val roomViewModel: RoomViewModel = viewModel(key = room.id.value) {
                RoomViewModel(DemoHouseService(), room.id)
            }
            val devices by roomViewModel.devices.collectAsStateWithLifecycle()

            var expanded by rememberSaveable { mutableStateOf(true) }

            RoomSection(
                room = room,
                expanded = expanded,
                devices = devices,
                onExpand = { isExpanded ->
                    expanded = isExpanded
                },
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
internal fun RoomSection(
    room: Room,
    expanded: Boolean,
    devices: List<Device>,
    onExpand: (Boolean) -> Unit,
    onClick: (Device) -> Unit,
    onLongClick: (Device) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onExpand(!expanded) } // TODO Task 17: improve accessibility
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .graphicsLayer {
                        // TODO Task 11: animate rotation
                        rotationZ = if (expanded) 0f else -90f
                    }
            )
            Text(
                text = room.name,
                style = MaterialTheme.typography.titleLarge,
            )
        }

        // TODO Task 12: animate visibility change
        if (expanded) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 16.dp)
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
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
) {
    Card(
        modifier = modifier.size(140.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (device is LightDevice) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(
                            // TODO Task 11: animate height change
                            if (!device.isOn) {
                                0f
                            } else {
                                (device.brightness.toFloat() / DeviceConstants.Light.MAX_BRIGHTNESS)
                            }
                        )
                        .background(device.color.copy(alpha = 0.3f))
                        .align(Alignment.BottomCenter)
                )
            }

            DeviceCardContent(
                device = device,
                modifier = if (device is Toggleable) {
                    Modifier
                        .combinedClickable(onClick = onClick, onLongClick = onLongClick)
                        .onRightClick(onLongClick)
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

@Preview
@Composable
private fun LightCardPreview() {
    var isOn by remember { mutableStateOf(true) }
    DeviceCard(
        device = LightDevice(
            deviceId = DeviceId(""),
            name = "Ceiling light",
            roomId = RoomId(""),
            isOn = isOn,
            brightness = 75,
            color = Color.Red,
        ),
        onClick = { isOn = !isOn },
        modifier = Modifier.padding(8.dp),
    )
}

@Preview
@Composable
private fun SwitchCardPreview() {
    var isOn by remember { mutableStateOf(true) }
    DeviceCard(
        device = SwitchDevice(
            deviceId = DeviceId(""),
            name = "TV",
            roomId = RoomId(""),
            isOn = isOn,
        ),
        onClick = { isOn = !isOn },
        modifier = Modifier.padding(8.dp),
    )
}

@Preview
@Composable
private fun CameraCardPreview() {
    var isOn by remember { mutableStateOf(false) }
    DeviceCard(
        device = CameraDevice(
            deviceId = DeviceId(""),
            name = "Garage interior",
            roomId = RoomId(""),
            isOn = isOn,
        ),
        onClick = { isOn = !isOn },
        modifier = Modifier.padding(8.dp),
    )
}

@Preview
@Composable
private fun TempCardPreview() {
    DeviceCard(
        device = ThermostatDevice(
            deviceId = DeviceId(""),
            name = "Weather station",
            roomId = RoomId(""),
            currentValue = 25f
        ),
        modifier = Modifier.padding(8.dp),
    )
}

@Preview
@Composable
private fun HumidityCardPreview() {
    DeviceCard(
        device = HumidityDevice(
            deviceId = DeviceId(""),
            name = "Weather station",
            roomId = RoomId(""),
            currentValue = 42.42f
        ),
        modifier = Modifier.padding(8.dp),
    )
}

@Composable
private fun DeviceCardContent(
    device: Device,
    modifier: Modifier = Modifier,
    bottomText: String? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .semantics { // TODO Task 17: merge semantics here
                if (device is Toggleable) {
                    role = Role.Switch
                    toggleableState = ToggleableState(device.isOn)
                }
            },
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
                contentDescription = null,
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
