package com.aralhub.araltaxi.core.domain.client

import com.aralhub.indrive.core.data.repository.client.ClientRideRepository
import javax.inject.Inject

class GetClientRideStatusUseCase @Inject constructor(private val rideRepository: ClientRideRepository) {
    suspend operator fun invoke() = rideRepository.getRideStatus()
}