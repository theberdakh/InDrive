package com.aralhub.araltaxi.core.domain.driver

import com.aralhub.indrive.core.data.repository.driver.DriverWebSocketRepository
import javax.inject.Inject

class GetStartedRideStatusUseCase @Inject constructor(
    private val repository: DriverWebSocketRepository
) {
    operator fun invoke() = repository.getStartedRideStatus()
}