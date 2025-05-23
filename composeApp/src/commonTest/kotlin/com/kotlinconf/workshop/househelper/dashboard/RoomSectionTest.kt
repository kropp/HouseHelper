package com.kotlinconf.workshop.househelper.dashboard

import androidx.compose.runtime.*
import androidx.compose.ui.test.*
import com.kotlinconf.workshop.househelper.*
import kotlin.test.*

@OptIn(ExperimentalTestApi::class)
class RoomSectionTest {
    private val testRoomId = RoomId("test-room")
    private val testLightId = DeviceId("light-1")
    private val testThermostatId = DeviceId("thermostat-1")

    private val testRoom = Room(
        id = testRoomId,
        name = "Test Room"
    )

    private val testLight = LightDevice(
        deviceId = testLightId,
        name = "Test Light",
        roomId = testRoomId,
        isOn = false,
        brightness = 50
    )

    private val testThermostat = ThermostatDevice(
        deviceId = testThermostatId,
        name = "Test Thermostat",
        roomId = testRoomId,
        currentValue = 22.5f
    )

    @Test
    fun testRoomSectionExpansion() = runComposeUiTest {
        var expanded by mutableStateOf(false)

        setContent {
            RoomSection(
                room = testRoom,
                expanded = expanded,
                devices = listOf(testLight, testThermostat),
                onExpand = { expanded = it },
                onClick = {},
                onLongClick = {}
            )
        }

        // TODO Task 18: implement test

        // Initially collapsed
        // Click on the room header to expand
        // Verify devices are now visible
        // Click again to collapse
        // Verify devices are hidden again
    }
}
