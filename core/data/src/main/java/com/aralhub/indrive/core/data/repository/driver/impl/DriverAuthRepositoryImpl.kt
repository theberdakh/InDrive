package com.aralhub.indrive.core.data.repository.driver.impl

import com.aralhub.indrive.core.data.model.driver.DriverProfile
import com.aralhub.indrive.core.data.repository.driver.DriverAuthRepository
import com.aralhub.network.DriverNetworkDataSource
import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.driver.NetworkDriverAuthRequest
import javax.inject.Inject
import com.aralhub.indrive.core.data.result.Result
import com.aralhub.network.models.driver.NetworkDriverVerifyRequest
import com.aralhub.network.utils.LocalStorage

class DriverAuthRepositoryImpl @Inject constructor(private val localStorage: LocalStorage, private val driverNetworkDataSource: DriverNetworkDataSource): DriverAuthRepository {
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
                is NetworkResult.Success -> {
                    localStorage.access = it.data.accessToken
                    localStorage.refresh = it.data.refreshToken
                    localStorage.isLogin = true
                    Result.Success(data = true)
                }
            }
        }
    }

    override suspend fun driverInfo(): Result<DriverProfile> {
        driverNetworkDataSource.getDriverInfo().let {
            return when(it){
                is NetworkResult.Error -> Result.Error(it.message)
                is NetworkResult.Success -> {
                    Result.Success(DriverProfile(
                        id = it.data.id,
                        driverId = it.data.driverId,
                        address = it.data.address,
                        licenseNumber = it.data.licenseNumber,
                        dateOfIssue = it.data.dateOfIssue,
                        dateOfExpiry = it.data.dateOfExpiry,
                        cardNumber = it.data.cardNumber,
                        cardHolder = it.data.nameOnCard,
                        frontPhotoUrl = it.data.frontPhotoUrl ?: "",
                        backPhotoUrl = it.data.backPhotoUrl ?: ""
                    ))
                }
            }
        }
    }

}