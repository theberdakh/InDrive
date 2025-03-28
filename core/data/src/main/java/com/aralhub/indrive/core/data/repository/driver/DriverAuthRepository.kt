package com.aralhub.indrive.core.data.repository.driver

import com.aralhub.indrive.core.data.model.driver.DriverBalance
import com.aralhub.indrive.core.data.model.driver.DriverBalanceInfo
import com.aralhub.indrive.core.data.model.driver.DriverCard
import com.aralhub.indrive.core.data.model.driver.DriverInfo
import com.aralhub.indrive.core.data.model.driver.DriverProfile
import com.aralhub.indrive.core.data.model.driver.DriverProfileWithVehicle
import com.aralhub.indrive.core.data.model.location.SendLocationRequest
import com.aralhub.indrive.core.data.model.offer.ActiveOfferResponse
import com.aralhub.indrive.core.data.result.Result
import java.io.File

interface DriverAuthRepository {
    suspend fun driverAddPhone(phone: String): Result<Boolean>
    suspend fun driverAuthVerify(phone: String, code: String): Result<Boolean>
    suspend fun driverInfo(): Result<DriverInfo>
    suspend fun driverInfoWithVehicle(): Result<DriverProfileWithVehicle>
    suspend fun getDriverCard(): Result<DriverCard>
    suspend fun getDriverBalance(): Result<DriverBalance>
    suspend fun driverLogout(): Result<Boolean>
    suspend fun driverPhoto(file: File): Result<Boolean>
    suspend fun getActiveOrders(sendLocationRequest: SendLocationRequest): Result<List<ActiveOfferResponse>>
    suspend fun getDriverBalanceInfo(): Result<DriverBalanceInfo>
}