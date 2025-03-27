package com.aralhub.araltaxi.core.domain.client

import com.aralhub.indrive.core.data.repository.client.ClientWebSocketRepository
import javax.inject.Inject

class GetDriverCardUseCase @Inject constructor(private val clientWebSocketRepository: ClientWebSocketRepository) {
    suspend operator fun invoke(driverId: Int) = clientWebSocketRepository.getDriverCard(driverId)
}