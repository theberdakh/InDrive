package com.aralhub.araltaxi.core.domain.client

import com.aralhub.indrive.core.data.repository.client.ClientOffersRepository
import javax.inject.Inject

class ClientDeclineOfferUseCase @Inject constructor(private val clientOffersRepository: ClientOffersRepository) {
    suspend operator fun invoke(offerId: String) = clientOffersRepository.rejectOffer(offerId)
}