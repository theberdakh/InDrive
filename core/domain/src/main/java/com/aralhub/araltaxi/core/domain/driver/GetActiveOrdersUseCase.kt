package com.aralhub.araltaxi.core.domain.driver

import com.aralhub.indrive.core.data.repository.driver.DriverWebSocketRepository
import javax.inject.Inject

class GetActiveOrdersUseCase @Inject constructor(
    private val repository: DriverWebSocketRepository
) {
    operator fun invoke() = repository.getActiveRides()
}