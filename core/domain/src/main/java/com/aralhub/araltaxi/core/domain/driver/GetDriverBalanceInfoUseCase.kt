package com.aralhub.araltaxi.core.domain.driver

import com.aralhub.indrive.core.data.repository.driver.DriverAuthRepository
import javax.inject.Inject

class GetDriverBalanceInfoUseCase @Inject constructor(private val driverAuthRepository: DriverAuthRepository) {
    suspend operator fun invoke() = driverAuthRepository.getDriverBalanceInfo()
}