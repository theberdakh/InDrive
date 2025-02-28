package com.aralhub.araltaxi.core.domain.driver

import com.aralhub.indrive.core.data.repository.driver.DriverWebSocketRepository
import javax.inject.Inject

class CloseDriverWebSocketConnectionUseCase @Inject constructor(
    private val repository: DriverWebSocketRepository
) {
    suspend fun close() = repository.close()
}