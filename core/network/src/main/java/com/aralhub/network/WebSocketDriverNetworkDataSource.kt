package com.aralhub.network

import com.aralhub.network.models.location.NetworkSendLocationRequest
import com.aralhub.network.utils.StartedRideWebSocketEventNetwork
import com.aralhub.network.utils.WebSocketEventNetwork
import kotlinx.coroutines.flow.Flow

interface WebSocketDriverNetworkDataSource {

    fun getActiveOrders(): Flow<WebSocketEventNetwork>

    suspend fun sendLocation(data: NetworkSendLocationRequest)

    suspend fun close()

    //after offer accepted
    fun getStartedRideStatus(): Flow<StartedRideWebSocketEventNetwork>

    suspend fun closeStartedRideStatusSocket()
}