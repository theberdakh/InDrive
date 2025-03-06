package com.aralhub.network

import com.aralhub.network.utils.ClientWebSocketEvent
import kotlinx.coroutines.flow.Flow

interface ClientOffersNetworkDataSource {
    fun getOffers(): Flow<ClientWebSocketEvent>
}