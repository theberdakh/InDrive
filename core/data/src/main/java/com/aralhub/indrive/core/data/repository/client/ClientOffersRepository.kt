package com.aralhub.indrive.core.data.repository.client

import com.aralhub.indrive.core.data.util.ClientWebSocketEvent
import kotlinx.coroutines.flow.Flow

interface ClientOffersRepository {

    suspend fun getOffers(): Flow<ClientWebSocketEvent>

    suspend fun close()
}