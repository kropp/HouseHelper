package com.kotlinconf.workshop.househelper

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class HouseService {
    private val rooms = MutableStateFlow(
        listOf(
            Room(
                name = "Living Room", appliances = listOf(
                    LightAppliance(name = "Main Light"),
                    LightAppliance(name = "Floor Lamp"),
                    SwitchAppliance(name = "TV Switch"),
                    ThermostatAppliance(name = "Temperature"),
                )
            ),
            Room(
                name = "Kitchen", appliances = listOf(
                    LightAppliance(name = "Ceiling Light"),
                    LightAppliance(name = "Counter Light"),
                    HumidityAppliance(name = "Humidity"),
                    SwitchAppliance(name = "Oven Switch"),
                )
            ),
            Room(
                name = "Bathroom", appliances = listOf(
                    LightAppliance(name = "Main Light"),
                    LightAppliance(name = "Mirror Light"),
                    HumidityAppliance(name = "Humidity"),
                    ThermostatAppliance(name = "Temperature"),
                )
            )
        )
    )

    fun getRooms(): Flow<List<Room>> = rooms

    fun toggleAppliance(appliance: Appliance) {
        val currentRooms = rooms.value
        val updatedRooms = currentRooms.map { room ->
            val updatedAppliances = room.appliances.map { currentAppliance ->
                if (currentAppliance === appliance) {
                    when (appliance) {
                        is LightAppliance -> appliance.copy(isOn = !appliance.isOn)
                        is SwitchAppliance -> appliance.copy(isOn = !appliance.isOn)
                        is HumidityAppliance -> appliance
                        is ThermostatAppliance -> appliance
                    }
                } else {
                    currentAppliance
                }
            }
            room.copy(appliances = updatedAppliances)
        }
        rooms.value = updatedRooms
    }
}
