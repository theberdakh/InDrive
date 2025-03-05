package com.aralhub.araltaxi.core.domain.driver.offer

import com.aralhub.indrive.core.data.repository.driver.DriverOfferRepository
import javax.inject.Inject

class CreateOfferUseCase @Inject constructor(
    private val repository: DriverOfferRepository
) {

    suspend operator fun invoke(rideUUID: String, amount: Int) =
        repository.createRide(rideUUID, amount)

}