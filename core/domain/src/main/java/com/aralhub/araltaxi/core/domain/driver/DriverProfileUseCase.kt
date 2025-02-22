package com.aralhub.araltaxi.core.domain.driver

import com.aralhub.indrive.core.data.repository.driver.DriverAuthRepository
import javax.inject.Inject

class DriverProfileUseCase @Inject constructor(private val repository: DriverAuthRepository) {
    suspend operator fun invoke() = repository.driverInfo()
}
