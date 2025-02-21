package com.aralhub.indrive.core.data.repository.driver

import com.aralhub.indrive.core.data.model.driver.DriverProfile
import com.aralhub.indrive.core.data.result.Result

interface DriverAuthRepository {
    suspend fun driverAddPhone(phone: String): Result<Boolean>
    suspend fun driverAuthVerify(phone: String, code: String): Result<Boolean>
    suspend fun driverInfo(): Result<DriverProfile>
}