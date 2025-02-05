package com.kotlinconf.workshop.househelper.data

import androidx.compose.ui.graphics.Color
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
import com.kotlinconf.workshop.househelper.utils.imageUrls
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class DemoHouseService : HouseService {
    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        startPeriodicSensorUpdates()
    }

    private fun startPeriodicSensorUpdates() {
        scope.launch {
            while (true) {
                delay(5000) // 15 seconds
                devices.update { deviceList ->
                    deviceList.map { device ->
                        when (device) {
                            is HumidityDevice -> {
                                val change = Random.nextFloat() * 4f - 2f // Random value between -2 and 2
                                device.copy(
                                    currentValue = (device.currentValue + change).coerceIn(
                                        DeviceConstants.Humidity.MIN_HUMIDITY,
                                        DeviceConstants.Humidity.MAX_HUMIDITY
                                    )
                                )
                            }

                            is ThermostatDevice -> {
                                val change = Random.nextFloat() * 4f - 2f // Random value between -2 and 2
                                device.copy(
                                    currentValue = (device.currentValue + change).coerceIn(
                                        DeviceConstants.Thermostat.MIN_TEMPERATURE,
                                        DeviceConstants.Thermostat.MAX_TEMPERATURE
                                    )
                                )
                            }

                            else -> device
                        }
                    }
                }
            }
        }
    }

    private val rooms = MutableStateFlow(demoRooms)

    private val devices = MutableStateFlow(demoDevices)

    override fun getRooms(): Flow<List<Room>> = rooms

    override fun getDevicesForRoom(roomId: RoomId): Flow<List<Device>> = devices.map { deviceList ->
        deviceList.filter { device -> device.roomId == roomId }
    }

    override fun getDevice(deviceId: DeviceId): Flow<Device?> = devices.map { deviceList ->
        deviceList.find { device -> device.deviceId.value == deviceId.value }
    }

    override fun getCamera(deviceId: DeviceId): Flow<CameraDevice?> =
        getDevice(deviceId).map { it as? CameraDevice }

    override fun getCameraFootage(deviceId: DeviceId): Flow<String> {
        return flow {
            while (true) {
                val images = imageUrls.shuffled()
                for (image in images) {
                    emit(image)
                    delay(5000)
                }
            }
        }
    }

    private fun updateDevice(device: Device) {
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

    override suspend fun toggle(deviceId: DeviceId): Boolean {
        val device: Device? = getDevice(deviceId).first()

        if (device !is Toggleable) {
            return false
        }
        when (device) {
            is SwitchDevice -> updateDevice(device.copy(isOn = !device.isOn))
            is CameraDevice -> updateDevice(device.copy(isOn = !device.isOn))
            is LightDevice -> {
                val newIsOn = !device.isOn
                if (newIsOn && device.brightness == 0) {
                    updateDevice(device.copy(isOn = true, brightness = 100))
                } else {
                    updateDevice(device.copy(isOn = newIsOn))
                }
            }
        }
        return true
    }

    override suspend fun setBrightness(deviceId: DeviceId, brightness: Int) {
        val light = getDevice(deviceId).first() as? LightDevice
            ?: throw IllegalArgumentException("Light not found for $deviceId")
        updateDevice(
            light.copy(
                brightness = brightness,
                isOn = brightness != 0,
            )
        )
    }

    override suspend fun setColor(deviceId: DeviceId, color: Color) {
        val light = getDevice(deviceId).first() as? LightDevice
            ?: throw IllegalArgumentException("Light not found for $deviceId")
        updateDevice(light.copy(color = color))
    }

    override suspend fun rename(deviceId: DeviceId, name: String) {
        val device: Device = getDevice(deviceId).first()
            ?: throw IllegalArgumentException("Device not found for $deviceId")

        when (device) {
            is LightDevice -> updateDevice(device.copy(name = name))
            is SwitchDevice -> updateDevice(device.copy(name = name))
            is HumidityDevice -> updateDevice(device.copy(name = name))
            is ThermostatDevice -> updateDevice(device.copy(name = name))
            is CameraDevice -> updateDevice(device.copy(name = name))
        }
    }
}
