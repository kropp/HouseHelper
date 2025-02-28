package com.kotlinconf.workshop.househelper.data

import com.kotlinconf.workshop.househelper.*

val demoRooms = listOf(
    Room(id = RoomId("living_room"), name = "Living Room"),
    Room(id = RoomId("kitchen"), name = "Kitchen"),
    Room(id = RoomId("bathroom"), name = "Bathroom"),
    Room(id = RoomId("bedroom"), name = "Bedroom")
)

val demoDevices = listOf(
    // Living Room devices
    LightDevice(
        deviceId = DeviceId("living_room_main_light"),
        name = "Main Light",
        roomId = RoomId("living_room"),
        isOn = true,
        brightness = 85,
        color = DeviceConstants.Light.PREDEFINED_COLORS[2]
    ),
    LightDevice(
        deviceId = DeviceId("living_room_floor_lamp"),
        name = "Floor Lamp",
        roomId = RoomId("living_room"),
        brightness = 0
    ),
    LightDevice(
        deviceId = DeviceId("living_room_reading_light"),
        name = "Reading Light",
        roomId = RoomId("living_room"),
        isOn = true,
        brightness = 65,
        color = DeviceConstants.Light.PREDEFINED_COLORS[3]
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
        roomId = RoomId("kitchen"),
        brightness = 0
    ),
    LightDevice(
        deviceId = DeviceId("kitchen_counter_light"),
        name = "Counter Light",
        roomId = RoomId("kitchen"),
        isOn = true,
        brightness = 90,
        color = DeviceConstants.Light.PREDEFINED_COLORS[5]
    ),
    LightDevice(
        deviceId = DeviceId("kitchen_under_cabinet_light"),
        name = "Under Cabinet Light",
        roomId = RoomId("kitchen"),
        isOn = true,
        brightness = 75
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
        isOn = true,
        brightness = 95
    ),
    LightDevice(
        deviceId = DeviceId("bathroom_mirror_light"),
        name = "Mirror Light",
        roomId = RoomId("bathroom"),
        isOn = true,
        brightness = 80
    ),
    LightDevice(
        deviceId = DeviceId("bathroom_shower_light"),
        name = "Shower Light",
        roomId = RoomId("bathroom"),
        brightness = 0
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
    LightDevice(
        deviceId = DeviceId("bedroom_main_light"),
        name = "Main Light",
        roomId = RoomId("bedroom"),
        brightness = 0
    ),
    LightDevice(
        deviceId = DeviceId("bedroom_bedside_lamp_left"),
        name = "Bedside Lamp Left",
        roomId = RoomId("bedroom"),
        isOn = true,
        brightness = 45
    ),
    LightDevice(
        deviceId = DeviceId("bedroom_bedside_lamp_right"),
        name = "Bedside Lamp Right",
        roomId = RoomId("bedroom"),
        brightness = 0
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
