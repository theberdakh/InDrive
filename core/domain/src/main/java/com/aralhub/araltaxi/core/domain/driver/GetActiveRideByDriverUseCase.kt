package com.aralhub.araltaxi.core.domain.driver

import com.aralhub.indrive.core.data.repository.driver.DriverRepository
import javax.inject.Inject

class GetActiveRideByDriverUseCase @Inject constructor(
    private val repository: DriverRepository
) {
    suspend operator fun invoke() = repository.getActiveRide()
}