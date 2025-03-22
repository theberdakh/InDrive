package com.aralhub.araltaxi.core.domain.client

import com.aralhub.indrive.core.data.repository.client.ClientWebSocketRepository
import javax.inject.Inject

class ClientChangeSearchPriceUseCase @Inject constructor(private val repository: ClientWebSocketRepository) {
    suspend operator fun invoke(rideId: String, amount: Number) = repository.updateSearchRideAmount(rideId, amount)
}