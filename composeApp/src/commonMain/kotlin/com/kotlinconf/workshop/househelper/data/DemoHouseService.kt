package com.kotlinconf.workshop.househelper.data

import com.kotlinconf.workshop.househelper.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class DemoHouseService : HouseService {
    private val rooms = MutableStateFlow(
        listOf(
            Room(id = RoomId("living_room"), name = "Living Room"),
            Room(id = RoomId("kitchen"), name = "Kitchen"),
            Room(id = RoomId("bathroom"), name = "Bathroom"),
            Room(id = RoomId("bedroom"), name = "Bedroom")
        )
    )

    private val devices = MutableStateFlow(
        listOf(
            // Living Room devices
            LightDevice(
                deviceId = DeviceId("living_room_main_light"),
                name = "Main Light",
                roomId = RoomId("living_room"),
                isOn = true
            ),
            LightDevice(
                deviceId = DeviceId("living_room_floor_lamp"),
                name = "Floor Lamp",
                roomId = RoomId("living_room")
            ),
            LightDevice(
                deviceId = DeviceId("living_room_reading_light"),
                name = "Reading Light",
                roomId = RoomId("living_room"),
                isOn = true
            ),
            SwitchDevice(
                deviceId = DeviceId("living_room_tv_switch"),
                name = "TV Switch",
                roomId = RoomId("living_room"),
                isOn = true
            ),
            SwitchDevice(
                deviceId = DeviceId("living_room_gaming_console"),
                name = "Gaming Console",
                roomId = RoomId("living_room")
            ),
            ThermostatDevice(
                deviceId = DeviceId("living_room_temperature"),
                name = "Temperature",
                roomId = RoomId("living_room")
            ),
            HumidityDevice(
                deviceId = DeviceId("living_room_humidity"),
                name = "Humidity",
                roomId = RoomId("living_room")
            ),
            CameraDevice(
                deviceId = DeviceId("living_room_security_camera"),
                name = "Security Camera",
                roomId = RoomId("living_room"),
                isOn = true
            ),

            // Kitchen devices
            LightDevice(
                deviceId = DeviceId("kitchen_ceiling_light"),
                name = "Ceiling Light",
                roomId = RoomId("kitchen")
            ),
            LightDevice(
                deviceId = DeviceId("kitchen_counter_light"),
                name = "Counter Light",
                roomId = RoomId("kitchen"),
                isOn = true
            ),
            LightDevice(
                deviceId = DeviceId("kitchen_under_cabinet_light"),
                name = "Under Cabinet Light",
                roomId = RoomId("kitchen"),
                isOn = true
            ),
            HumidityDevice(deviceId = DeviceId("kitchen_humidity"), name = "Humidity", roomId = RoomId("kitchen")),
            SwitchDevice(deviceId = DeviceId("kitchen_oven_switch"), name = "Oven Switch", roomId = RoomId("kitchen")),
            SwitchDevice(
                deviceId = DeviceId("kitchen_dishwasher"),
                name = "Dishwasher",
                roomId = RoomId("kitchen"),
                isOn = true
            ),
            ThermostatDevice(
                deviceId = DeviceId("kitchen_temperature"),
                name = "Temperature",
                roomId = RoomId("kitchen")
            ),
            CameraDevice(
                deviceId = DeviceId("kitchen_security_camera"),
                name = "Security Camera",
                roomId = RoomId("kitchen")
            ),

            // Bathroom devices
            LightDevice(
                deviceId = DeviceId("bathroom_main_light"),
                name = "Main Light",
                roomId = RoomId("bathroom"),
                isOn = true
            ),
            LightDevice(
                deviceId = DeviceId("bathroom_mirror_light"),
                name = "Mirror Light",
                roomId = RoomId("bathroom"),
                isOn = true
            ),
            LightDevice(
                deviceId = DeviceId("bathroom_shower_light"),
                name = "Shower Light",
                roomId = RoomId("bathroom")
            ),
            HumidityDevice(deviceId = DeviceId("bathroom_humidity"), name = "Humidity", roomId = RoomId("bathroom")),
            ThermostatDevice(
                deviceId = DeviceId("bathroom_temperature"),
                name = "Temperature",
                roomId = RoomId("bathroom")
            ),
            CameraDevice(
                deviceId = DeviceId("bathroom_security_camera"),
                name = "Security Camera",
                roomId = RoomId("bathroom")
            ),

            // Bedroom devices
            LightDevice(deviceId = DeviceId("bedroom_main_light"), name = "Main Light", roomId = RoomId("bedroom")),
            LightDevice(
                deviceId = DeviceId("bedroom_bedside_lamp_left"),
                name = "Bedside Lamp Left",
                roomId = RoomId("bedroom"),
                isOn = true
            ),
            LightDevice(
                deviceId = DeviceId("bedroom_bedside_lamp_right"),
                name = "Bedside Lamp Right",
                roomId = RoomId("bedroom")
            ),
            SwitchDevice(deviceId = DeviceId("bedroom_tv_switch"), name = "TV Switch", roomId = RoomId("bedroom")),
            SwitchDevice(
                deviceId = DeviceId("bedroom_air_purifier"),
                name = "Air Purifier",
                roomId = RoomId("bedroom"),
                isOn = true
            ),
            ThermostatDevice(
                deviceId = DeviceId("bedroom_temperature"),
                name = "Temperature",
                roomId = RoomId("bedroom")
            ),
            HumidityDevice(deviceId = DeviceId("bedroom_humidity"), name = "Humidity", roomId = RoomId("bedroom")),
            CameraDevice(
                deviceId = DeviceId("bedroom_security_camera"),
                name = "Security Camera",
                roomId = RoomId("bedroom")
            )
        )
    )

    override fun getRooms(): Flow<List<Room>> = rooms

    override fun getDevicesForRoom(roomId: RoomId): Flow<List<Device>> = devices.map { deviceList ->
        deviceList.filter { device -> device.roomId == roomId }
    }

    override fun getDevice(deviceId: DeviceId): Flow<Device?> = devices.map { deviceList ->
        deviceList.find { device -> device.deviceId == deviceId }
    }

    override fun updateDevice(device: Device) {
        devices.update { currentDevices ->
            currentDevices.map { currentDevice ->
                if (currentDevice.deviceId == device.deviceId) {
                    device
                } else {
                    currentDevice
                }
            }
        }
    }
}
