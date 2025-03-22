package com.aralhub.indrive.core.data.repository.driver

import com.aralhub.indrive.core.data.model.location.SendLocationRequest
import com.aralhub.indrive.core.data.util.StartedRideWebSocketEvent
import com.aralhub.indrive.core.data.util.WebSocketEvent
import kotlinx.coroutines.flow.Flow

interface DriverWebSocketRepository {

    fun getActiveRides(): Flow<WebSocketEvent>

    suspend fun sendLocation(data: SendLocationRequest)

    suspend fun close()

    fun getStartedRideStatus(): Flow<StartedRideWebSocketEvent>
}