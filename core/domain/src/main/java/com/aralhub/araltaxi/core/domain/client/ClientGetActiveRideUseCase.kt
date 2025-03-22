package com.aralhub.araltaxi.core.domain.client

import com.aralhub.indrive.core.data.repository.client.ClientWebSocketRepository
import javax.inject.Inject

class ClientGetActiveRideUseCase @Inject constructor(private val webSocketRepository: ClientWebSocketRepository) {
    suspend  operator fun invoke() = webSocketRepository.getActiveRideByPassenger()
}