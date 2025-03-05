package com.kotlinconf.workshop.househelper.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import com.kotlinconf.workshop.househelper.*
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
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

    @Test
    fun testDashboardInitialState() = runComposeUiTest {
        setContent {
            val rooms = listOf(testRoom)
            val devices = listOf(testLight, testCamera)
            val devicesByRoom = mapOf<RoomId, List<Device>>(testRoomId to devices)

            DashboardScreen(
                rooms = rooms,
                devicesByRoom = devicesByRoom,
                onDeviceClicked = {},
                onNavigateToLightDetails = {},
                onNavigateToCameraDetails = {},
            )
        }

        // Verify room section exists
        onNode(hasText("Living Room")).assertExists()

        // Verify devices are displayed with correct initial states
        onNode(hasText("Security Camera")).assertExists()
        onNode(hasText("Main Light")).assertExists()
    }

    @Test
    fun testDeviceToggle() = runComposeUiTest {
        var currentLight by mutableStateOf(testLight)
        var currentCamera by mutableStateOf(testCamera)

        setContent {
            val rooms = listOf(testRoom)
            val devices = listOf(currentLight, currentCamera)
            val devicesByRoom = mapOf<RoomId, List<Device>>(testRoomId to devices)

            DashboardScreen(
                rooms = rooms,
                devicesByRoom = devicesByRoom,
                onDeviceClicked = { device ->
                    if (device is LightDevice) currentLight = device.copy(isOn = !device.isOn)
                },
                onNavigateToLightDetails = {},
                onNavigateToCameraDetails = {},
            )
        }

        // Test light device toggle
        onNode(hasText("Main Light")).performClick()
        assertTrue(currentLight.isOn)

        // Toggle it back
        onNode(hasText("Main Light")).performClick()
        assertFalse(currentLight.isOn)
    }

    @Test
    fun testDeviceNavigation() = runComposeUiTest {
        var lightDetailsNavigated = false
        var cameraDetailsNavigated = false

        setContent {
            val rooms = listOf(testRoom)
            val devices = listOf(testLight, testCamera)
            val devicesByRoom = mapOf<RoomId, List<Device>>(testRoomId to devices)

            DashboardScreen(
                rooms = rooms,
                devicesByRoom = devicesByRoom,
                onDeviceClicked = {},
                onNavigateToLightDetails = { lightDetailsNavigated = true },
                onNavigateToCameraDetails = { cameraDetailsNavigated = true },
            )
        }

        onNode(hasText("Main Light")).performTouchInput {
            longClick()
        }
        assertTrue(lightDetailsNavigated, "Navigation to light details was not triggered")

        onNode(hasText("Security Camera")).performTouchInput {
            longClick()
        }
        assertTrue(cameraDetailsNavigated, "Navigation to camera details was not triggered")
    }
}
