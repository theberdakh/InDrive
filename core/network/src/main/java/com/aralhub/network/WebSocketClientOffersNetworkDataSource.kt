package com.aralhub.network

import com.aralhub.network.utils.ClientWebSocketEventNetwork
import kotlinx.coroutines.flow.Flow

interface WebSocketClientOffersNetworkDataSource {
    suspend fun getOffers(): Flow<ClientWebSocketEventNetwork>

    suspend fun close()
}