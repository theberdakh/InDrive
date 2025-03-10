package com.aralhub.araltaxi.core.domain.client

import com.aralhub.indrive.core.data.repository.client.ClientOffersRepository
import javax.inject.Inject

class ClientAcceptOfferUseCase @Inject constructor(private val clientOffersRepository: ClientOffersRepository) {
    suspend operator fun invoke(offerId: String) = clientOffersRepository.acceptOffer(offerId)
}