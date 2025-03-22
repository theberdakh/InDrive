package com.aralhub.indrive.core.data.repository.client

import com.aralhub.indrive.core.data.result.Result
import com.aralhub.indrive.core.data.util.ClientWebSocketEvent
import kotlinx.coroutines.flow.Flow

interface ClientOffersRepository {

    suspend fun acceptOffer(offerId: String): Result<Boolean>

    suspend fun rejectOffer(offerId: String): Result<Boolean>

    suspend fun getOffers(): Flow<ClientWebSocketEvent>

    suspend fun close()
}