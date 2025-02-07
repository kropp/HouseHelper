package com.kotlinconf.workshop.househelper

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class HouseService {
    private val rooms = MutableStateFlow(
        listOf(
            Room(id = "living_room", name = "Living Room"),
            Room(id = "kitchen", name = "Kitchen"),
            Room(id = "bathroom", name = "Bathroom"),
            Room(id = "bedroom", name = "Bedroom")
        )
    )

    private val devices = MutableStateFlow(
        listOf(
            // Living Room devices
            LightDevice(name = "Main Light", roomId = "living_room", isOn = true),
            LightDevice(name = "Floor Lamp", roomId = "living_room"),
            LightDevice(name = "Reading Light", roomId = "living_room", isOn = true),
            SwitchDevice(name = "TV Switch", roomId = "living_room", isOn = true),
            SwitchDevice(name = "Gaming Console", roomId = "living_room"),
            ThermostatDevice(name = "Temperature", roomId = "living_room"),
            HumidityDevice(name = "Humidity", roomId = "living_room"),
            CameraDevice(name = "Security Camera", roomId = "living_room", isOn = true),

            // Kitchen devices
            LightDevice(name = "Ceiling Light", roomId = "kitchen"),
            LightDevice(name = "Counter Light", roomId = "kitchen", isOn = true),
            LightDevice(name = "Under Cabinet Light", roomId = "kitchen", isOn = true),
            HumidityDevice(name = "Humidity", roomId = "kitchen"),
            SwitchDevice(name = "Oven Switch", roomId = "kitchen"),
            SwitchDevice(name = "Dishwasher", roomId = "kitchen", isOn = true),
            ThermostatDevice(name = "Temperature", roomId = "kitchen"),
            CameraDevice(name = "Security Camera", roomId = "kitchen"),

            // Bathroom devices
            LightDevice(name = "Main Light", roomId = "bathroom", isOn = true),
            LightDevice(name = "Mirror Light", roomId = "bathroom", isOn = true),
            LightDevice(name = "Shower Light", roomId = "bathroom"),
            HumidityDevice(name = "Humidity", roomId = "bathroom"),
            ThermostatDevice(name = "Temperature", roomId = "bathroom"),
            CameraDevice(name = "Security Camera", roomId = "bathroom"),

            // Bedroom devices
            LightDevice(name = "Main Light", roomId = "bedroom"),
            LightDevice(name = "Bedside Lamp Left", roomId = "bedroom", isOn = true),
            LightDevice(name = "Bedside Lamp Right", roomId = "bedroom"),
            SwitchDevice(name = "TV Switch", roomId = "bedroom"),
            SwitchDevice(name = "Air Purifier", roomId = "bedroom", isOn = true),
            ThermostatDevice(name = "Temperature", roomId = "bedroom"),
            HumidityDevice(name = "Humidity", roomId = "bedroom"),
            CameraDevice(name = "Security Camera", roomId = "bedroom")
        )
    )

    fun getRooms(): Flow<List<Room>> = rooms

    fun getDevicesForRoom(roomId: String): Flow<List<Device>> = devices.map { deviceList -> 
        deviceList.filter { device -> device.roomId == roomId } 
    }

    fun toggleDevice(device: Device) {
        devices.update { currentDevices ->
            currentDevices.map { currentDevice ->
                if (currentDevice === device) {
                    when (currentDevice) {
                        is LightDevice -> currentDevice.copy(isOn = !currentDevice.isOn)
                        is SwitchDevice -> currentDevice.copy(isOn = !currentDevice.isOn)
                        is CameraDevice -> currentDevice.copy(isOn = !currentDevice.isOn)
                        is HumidityDevice -> currentDevice
                        is ThermostatDevice -> currentDevice
                    }
                } else {
                    currentDevice
                }
            }
        }
    }
}
