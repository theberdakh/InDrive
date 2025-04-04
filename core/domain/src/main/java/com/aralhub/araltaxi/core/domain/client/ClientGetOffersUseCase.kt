package com.aralhub.araltaxi.core.domain.client

import com.aralhub.indrive.core.data.repository.client.ClientOffersRepository
import javax.inject.Inject

class ClientGetOffersUseCase @Inject constructor(private val repository: ClientOffersRepository) {
    suspend operator fun invoke() = repository.getOffers()
}