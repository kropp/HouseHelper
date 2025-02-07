package com.kotlinconf.workshop.househelper.data

import com.kotlinconf.workshop.househelper.Device
import com.kotlinconf.workshop.househelper.DeviceId
import com.kotlinconf.workshop.househelper.Room
import com.kotlinconf.workshop.househelper.RoomId
import kotlinx.coroutines.flow.Flow

interface HouseService {
    fun getRooms(): Flow<List<Room>>
    fun getDevicesForRoom(roomId: RoomId): Flow<List<Device>>
    fun getDevice(deviceId: DeviceId): Flow<Device?>
    fun updateDevice(newDevice: Device)
}
