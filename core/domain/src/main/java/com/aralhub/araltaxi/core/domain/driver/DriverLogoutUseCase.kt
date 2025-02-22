package com.aralhub.araltaxi.core.domain.driver

import com.aralhub.indrive.core.data.repository.driver.DriverAuthRepository
import javax.inject.Inject

class DriverLogoutUseCase @Inject constructor(private val driverRepository: DriverAuthRepository) {
    suspend operator fun invoke() = driverRepository.driverLogout()
}