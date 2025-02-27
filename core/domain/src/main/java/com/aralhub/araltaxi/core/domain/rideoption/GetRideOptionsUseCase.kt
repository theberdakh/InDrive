package com.aralhub.araltaxi.core.domain.rideoption

import com.aralhub.indrive.core.data.repository.rideoption.RideOptionRepository
import javax.inject.Inject

class GetRideOptionsUseCase @Inject constructor(private val rideOptionRepository: RideOptionRepository) {
    suspend operator fun invoke() = rideOptionRepository.getRideOptions()
}