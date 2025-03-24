package com.aralhub.network

import com.aralhub.network.utils.ClientWebSocketEventRideMessage
import kotlinx.coroutines.flow.SharedFlow

interface ClientRideNetworkDataSource {

    suspend fun getRide(): SharedFlow<ClientWebSocketEventRideMessage>

    suspend fun close()
}

