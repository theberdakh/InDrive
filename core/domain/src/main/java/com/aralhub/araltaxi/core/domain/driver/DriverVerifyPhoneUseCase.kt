package com.aralhub.araltaxi.core.domain.driver

import com.aralhub.indrive.core.data.repository.driver.DriverAuthRepository
import com.aralhub.indrive.core.data.result.Result
import javax.inject.Inject

class DriverVerifyPhoneUseCase @Inject constructor(private val repository: DriverAuthRepository) {
    suspend operator fun invoke(phone: String, code: String): Result<Boolean> {
        return repository.driverAuthVerify(phone, code)
    }
}