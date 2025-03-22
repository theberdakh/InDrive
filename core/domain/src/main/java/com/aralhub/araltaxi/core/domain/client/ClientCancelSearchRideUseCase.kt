package com.aralhub.araltaxi.core.domain.client

import com.aralhub.indrive.core.data.repository.client.ClientWebSocketRepository
import javax.inject.Inject

class ClientCancelSearchRideUseCase @Inject constructor(private val clientWebSocketRepository: ClientWebSocketRepository) {
    suspend operator fun invoke(rideId: String) = clientWebSocketRepository.cancelSearchRide(rideId)
}