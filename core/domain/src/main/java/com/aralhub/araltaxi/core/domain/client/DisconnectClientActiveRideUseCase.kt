package com.aralhub.araltaxi.core.domain.client

import com.aralhub.indrive.core.data.repository.client.ClientOffersRepository
import javax.inject.Inject

class DisconnectClientActiveRideUseCase @Inject constructor(private val offersRepository: ClientOffersRepository) {

    suspend operator fun invoke() = offersRepository.close()
}