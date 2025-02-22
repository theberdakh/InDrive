package com.aralhub.indrive.core.data.repository.driver.impl

import com.aralhub.indrive.core.data.model.driver.DriverBalance
import com.aralhub.indrive.core.data.model.driver.DriverCard
import com.aralhub.indrive.core.data.model.driver.DriverProfile
import com.aralhub.indrive.core.data.model.driver.DriverProfileWithVehicle
import com.aralhub.indrive.core.data.repository.driver.DriverAuthRepository
import com.aralhub.network.DriverNetworkDataSource
import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.driver.NetworkDriverAuthRequest
import javax.inject.Inject
import com.aralhub.indrive.core.data.result.Result
import com.aralhub.network.models.driver.NetworkDriverLogoutRequest
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
                        driverId = it.data.driverId,
                        fullName = it.data.fullName,
                        rating = it.data.rating,
                        color = it.data.color,
                        vehicleType = it.data.vehicleType,
                        plateNumber = it.data.plateNumber,
                        phoneNumber = it.data.phoneNumber
                    ))
                }
            }
        }
    }

    override suspend fun driverInfoWithVehicle(): Result<DriverProfileWithVehicle> {
        driverNetworkDataSource.getDriverInfoWithVehicle().let {
            return when(it){
                is NetworkResult.Error -> Result.Error(it.message)
                is NetworkResult.Success -> {
                    Result.Success(DriverProfileWithVehicle(
                        driverId = it.data.driverId,
                        fullName = it.data.fullName,
                        rating = it.data.rating,
                        color = it.data.color,
                        vehicleType = it.data.vehicleType,
                        plateNumber = it.data.plateNumber
                    ))
                }
            }
        }
    }

    override suspend fun getDriverCard(): Result<DriverCard> {
        driverNetworkDataSource.getDriverCard().let {
            return when(it){
                is NetworkResult.Error -> Result.Error(it.message)
                is NetworkResult.Success -> {
                    Result.Success(DriverCard(
                        cardNumber = it.data.cardNumber,
                        nameOnCard = it.data.nameOnCard
                    ))
                }
            }
        }
    }

    override suspend fun getDriverBalance(): Result<DriverBalance> {
        driverNetworkDataSource.getDriverBalance().let {
            return when(it){
                is NetworkResult.Error -> Result.Error(it.message)
                is NetworkResult.Success -> {
                    Result.Success(DriverBalance(
                        balance = it.data.balance ?: 0,
                        dailyBalance = it.data.dayBalance ?: 0
                    ))
                }
            }
        }
    }

    override suspend fun driverLogout(): Result<Boolean> {
        driverNetworkDataSource.driverLogout(NetworkDriverLogoutRequest(localStorage.refresh)).let {
            return when(it){
                is NetworkResult.Error -> Result.Error(it.message)
                is NetworkResult.Success -> {
                    localStorage.clear()
                    Result.Success(data = true)
                }
            }
        }
    }

}