package com.aralhub.indrive.core.data.repository.driver.impl

import com.aralhub.indrive.core.data.repository.driver.DriverAuthRepository
import com.aralhub.network.DriverNetworkDataSource
import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.driver.NetworkDriverAuthRequest
import javax.inject.Inject
import com.aralhub.indrive.core.data.result.Result
import com.aralhub.network.models.driver.NetworkDriverVerifyRequest

class DriverAuthRepositoryImpl @Inject constructor(private val driverNetworkDataSource: DriverNetworkDataSource): DriverAuthRepository {
    override suspend fun driverAddPhone(phone: String): Result<Boolean> {
         driverNetworkDataSource.driverAuth(networkDriverAuthRequest = NetworkDriverAuthRequest(phoneNumber = phone)).let {
            return when (it) {
                is NetworkResult.Error -> Result.Error(it.message)
                is NetworkResult.Success -> Result.Success(data = true)
            }
        }
    }

    override suspend fun driverAuthVerify(phone: String, code: String): Result<Boolean> {
        driverNetworkDataSource.driverVerify(networkDriverVerifyRequest = NetworkDriverVerifyRequest(phoneNumber = phone, code = code)).let {
            return when (it) {
                is NetworkResult.Error -> Result.Error(it.message)
                is NetworkResult.Success -> Result.Success(data = true)
            }
        }
    }

}