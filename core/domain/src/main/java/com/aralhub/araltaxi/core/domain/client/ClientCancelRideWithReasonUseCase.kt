package com.aralhub.araltaxi.core.domain.client

import com.aralhub.indrive.core.data.repository.client.ClientWebSocketRepository
import javax.inject.Inject

class ClientCancelRideWithReasonUseCase @Inject constructor(
    private val clientWebSocketRepository: ClientWebSocketRepository
) {
    suspend operator fun invoke(rideId: Int, cancelCauseId: Int) =
        clientWebSocketRepository.cancelRide(rideId, cancelCauseId)
}