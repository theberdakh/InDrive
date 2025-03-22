package com.aralhub.araltaxi.core.domain.driver

import com.aralhub.indrive.core.data.model.location.SendLocationRequest
import com.aralhub.indrive.core.data.repository.driver.DriverWebSocketRepository
import javax.inject.Inject

class SendDriverLocationUseCase @Inject constructor(
    private val repository: DriverWebSocketRepository
) {
    suspend operator fun invoke(latitude: Double, longitude: Double, distance: Int) = repository.sendLocation(SendLocationRequest(latitude = latitude, longitude = longitude, distance = distance))
}