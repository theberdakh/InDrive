package com.aralhub.network

import com.aralhub.network.utils.ClientWebSocketEventNetwork
import com.aralhub.network.utils.ClientWebSocketEventRide
import kotlinx.coroutines.flow.Flow

interface ClientRideNetworkDataSource {

    suspend fun getRide(): Flow<ClientWebSocketEventRide>

    suspend fun close()
}

