package com.aralhub.araltaxi.core.domain.client

import com.aralhub.indrive.core.data.repository.client.ClientWebSocketRepository
import javax.inject.Inject

class ClientGetSearchRideUseCase @Inject constructor(private val clientWebSocketRepository: ClientWebSocketRepository) {
    suspend operator fun invoke(userId: Int) = clientWebSocketRepository.getSearchRideByPassengerId(userId)
}