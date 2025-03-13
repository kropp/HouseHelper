package com.kotlinconf.workshop.househelper.data

import androidx.compose.ui.graphics.Color
import com.kotlinconf.workshop.househelper.CameraDevice
import com.kotlinconf.workshop.househelper.Device
import com.kotlinconf.workshop.househelper.DeviceId
import com.kotlinconf.workshop.househelper.LightDevice
import com.kotlinconf.workshop.househelper.Room
import com.kotlinconf.workshop.househelper.RoomId
import com.kotlinconf.workshop.househelper.Toggleable
import kotlinx.coroutines.flow.Flow

interface HouseService {
    fun getRooms(): Flow<List<Room>>
    fun getDevicesForRoom(roomId: RoomId): Flow<List<Device>>
    fun getDevice(deviceId: DeviceId): Flow<Device?>
    fun getCameraFootage(deviceId: DeviceId): Flow<String>
    suspend fun toggle(device: Device): Boolean
    suspend fun setBrightness(device: LightDevice, brightness: Int)
    suspend fun setColor(device: LightDevice, color: Color)
    suspend fun rename(device: Device, name: String)
}
