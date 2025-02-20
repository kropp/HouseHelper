package com.kotlinconf.workshop.househelper.dashboard

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.*
import com.kotlinconf.workshop.househelper.*
import com.kotlinconf.workshop.househelper.data.HouseService
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class DashboardScreenTest {
    private val testRoomId = RoomId("living-room")
    private val testLightId = DeviceId("light-1")
    private val testCameraId = DeviceId("camera-1")

    private val testLight = LightDevice(
        deviceId = testLightId,
        name = "Main Light",
        roomId = testRoomId,
        isOn = false
    )

    private val testCamera = CameraDevice(
        deviceId = testCameraId,
        name = "Security Camera",
        roomId = testRoomId,
        isOn = true
    )

    private val testRoom = Room(
        id = testRoomId,
        name = "Living Room"
    )

    private class TestHouseService : HouseService {
        private val roomsFlow = MutableStateFlow(listOf<Room>())
        private val devicesFlow = MutableStateFlow(listOf<Device>())
        private val deviceStates = mutableMapOf<DeviceId, Boolean>()

        fun setRooms(rooms: List<Room>) {
            roomsFlow.value = rooms
        }

        fun setDevices(devices: List<Device>) {
            devicesFlow.value = devices
            devices.filterIsInstance<Toggleable>().forEach { device ->
                if (device is Device) {
                    deviceStates[device.deviceId] = device.isOn
                }
            }
        }

        override fun getRooms(): Flow<List<Room>> = roomsFlow
        override fun getDevicesForRoom(roomId: RoomId): Flow<List<Device>> = devicesFlow
        override fun getDevice(deviceId: DeviceId): Flow<Device?> = MutableStateFlow(null)
        override fun toggle(device: Device): Boolean {
            val currentState = deviceStates[device.deviceId] ?: return false
            deviceStates[device.deviceId] = !currentState

            // Update the devices flow with the new state
            devicesFlow.value = devicesFlow.value.map { d ->
                if (d.deviceId == device.deviceId && d is Toggleable) {
                    when (d) {
                        is LightDevice -> d.copy(isOn = !currentState)
                        is CameraDevice -> d.copy(isOn = !currentState)
                        else -> d
                    }
                } else d
            }
            return true
        }
        override fun setBrightness(device: LightDevice, brightness: Int) {}
        override fun setColor(device: LightDevice, color: Color) {}
        override fun rename(device: Device, name: String) {}
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testDashboardInitialState() = runComposeUiTest {
        val houseService = TestHouseService()
        houseService.setRooms(listOf(testRoom))
        houseService.setDevices(listOf(testLight, testCamera))

        setContent {
            DashboardScreen(
                onNavigateToLightDetails = {},
                onNavigateToCameraDetails = {},
                viewModel = DashboardViewModel(houseService)
            )
        }

        // Verify room section exists
        onNode(hasTestTag("room_section_${testRoomId.value}")).assertExists()

        // Verify devices are displayed with correct initial states
        onNode(hasTestTag("device_card_${testLightId.value}")).assertExists()
        onNode(hasContentDescription("Device Main Light is OFF")).assertExists()

        onNode(hasTestTag("device_card_${testCameraId.value}")).assertExists()
        onNode(hasContentDescription("Device Security Camera is ON")).assertExists()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testDeviceToggle() = runComposeUiTest {
        val houseService = TestHouseService()
        houseService.setRooms(listOf(testRoom))
        houseService.setDevices(listOf(testLight, testCamera))

        setContent {
            DashboardScreen(
                onNavigateToLightDetails = {},
                onNavigateToCameraDetails = {},
                viewModel = DashboardViewModel(houseService)
            )
        }

        // Test light device toggle
        onNode(hasTestTag("device_card_${testLightId.value}")).performClick()
        onNode(hasContentDescription("Device Main Light is ON")).assertExists()

        // Toggle it back
        onNode(hasTestTag("device_card_${testLightId.value}")).performClick()
        onNode(hasContentDescription("Device Main Light is OFF")).assertExists()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testDeviceNavigation() = runComposeUiTest {
        var lightDetailsNavigated = false
        var cameraDetailsNavigated = false

        val houseService = TestHouseService()
        houseService.setRooms(listOf(testRoom))
        houseService.setDevices(listOf(testLight, testCamera))

        setContent {
            DashboardScreen(
                onNavigateToLightDetails = { lightDetailsNavigated = true },
                onNavigateToCameraDetails = { cameraDetailsNavigated = true },
                viewModel = DashboardViewModel(houseService)
            )
        }

        // Test navigation by clicking "View more"
        onNode(hasTestTag("device_card_${testLightId.value}")).performTouchInput { 
            longClick()
        }
        assertTrue(lightDetailsNavigated, "Navigation to light details was not triggered")

        onNode(hasTestTag("device_card_${testCameraId.value}")).performTouchInput { 
            longClick()
        }
        assertTrue(cameraDetailsNavigated, "Navigation to camera details was not triggered")
    }
}
