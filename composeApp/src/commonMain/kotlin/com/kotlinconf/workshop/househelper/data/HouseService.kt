package com.kotlinconf.workshop.househelper.data

import androidx.compose.ui.graphics.Color
import com.kotlinconf.workshop.househelper.CameraDevice
import com.kotlinconf.workshop.househelper.Device
import com.kotlinconf.workshop.househelper.DeviceId
import com.kotlinconf.workshop.househelper.Room
import com.kotlinconf.workshop.househelper.RoomId
import kotlinx.coroutines.flow.Flow

interface HouseService {
    fun getRooms(): Flow<List<Room>>
    fun getDevicesForRoom(roomId: RoomId): Flow<List<Device>>
    fun getDevice(deviceId: DeviceId): Flow<Device?>
    fun getCamera(deviceId: DeviceId): Flow<CameraDevice?>
    fun getCameraFootage(deviceId: DeviceId): Flow<String>
    suspend fun toggle(deviceId: DeviceId): Boolean
    suspend fun setBrightness(deviceId: DeviceId, brightness: Int)
    suspend fun setColor(deviceId: DeviceId, color: Color)
    suspend fun rename(deviceId: DeviceId, name: String)
}
