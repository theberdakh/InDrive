package com.aralhub.indrive.core.data.repository.driver.impl

import com.aralhub.indrive.core.data.model.driver.DriverBalance
import com.aralhub.indrive.core.data.model.driver.DriverCard
import com.aralhub.indrive.core.data.model.driver.DriverProfile
import com.aralhub.indrive.core.data.model.driver.DriverProfileWithVehicle
import com.aralhub.indrive.core.data.model.location.SendLocationRequest
import com.aralhub.indrive.core.data.model.location.toDTO
import com.aralhub.indrive.core.data.model.location.toDTO2
import com.aralhub.indrive.core.data.model.offer.ActiveOfferResponse
import com.aralhub.indrive.core.data.model.offer.toDomain
import com.aralhub.indrive.core.data.repository.driver.DriverAuthRepository
import com.aralhub.indrive.core.data.result.Result
import com.aralhub.network.DriverNetworkDataSource
import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.driver.NetworkDriverAuthRequest
import com.aralhub.network.models.driver.NetworkDriverLogoutRequest
import com.aralhub.network.models.driver.NetworkDriverVerifyRequest
import com.aralhub.network.utils.LocalStorage
import javax.inject.Inject
import com.aralhub.network.requests.auth.NetworkDriverAuthRequest
import javax.inject.Inject
import com.aralhub.indrive.core.data.result.Result
import com.aralhub.network.requests.logout.NetworkLogoutRequest
import com.aralhub.network.requests.verify.NetworkVerifyRequest
import com.aralhub.network.local.LocalStorage
import java.io.File

class DriverAuthRepositoryImpl @Inject constructor(
    private val localStorage: LocalStorage,
    private val driverNetworkDataSource: DriverNetworkDataSource
) : DriverAuthRepository {
    override suspend fun driverAddPhone(phone: String): Result<Boolean> {
        driverNetworkDataSource.driverAuth(
            networkDriverAuthRequest = NetworkDriverAuthRequest(
                phoneNumber = phone
            )
        ).let {
            return when (it) {
                is NetworkResult.Error -> Result.Error(it.message)
                is NetworkResult.Success -> Result.Success(data = true)
            }
        }
    }

    override suspend fun driverAuthVerify(phone: String, code: String): Result<Boolean> {
        driverNetworkDataSource.driverVerify(
            networkDriverVerifyRequest = NetworkDriverVerifyRequest(
                phoneNumber = phone,
                code = code
            )
        ).let {
        driverNetworkDataSource.driverVerify(networkDriverVerifyRequest = NetworkVerifyRequest(phoneNumber = phone, code = code)).let {
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
            return when (it) {
                is NetworkResult.Error -> Result.Error(it.message)
                is NetworkResult.Success -> {
                    Result.Success(DriverProfile(
                        driverId = it.data.driverId,
                        fullName = it.data.fullName,
                        rating = it.data.rating,
                        color = it.data.color.kk,
                        vehicleType = it.data.vehicleType.kk,
                        plateNumber = it.data.plateNumber,
                        phoneNumber = it.data.phoneNumber,
                        photoUrl = it.data.photoUrl
                    ))
                    Result.Success(
                        DriverProfile(
                            driverId = it.data.driverId,
                            fullName = it.data.fullName,
                            rating = it.data.rating,
                            color = it.data.color.kk,
                            vehicleType = it.data.vehicleType.kk,
                            plateNumber = it.data.plateNumber,
                            phoneNumber = it.data.phoneNumber
                        )
                    )
                }
            }
        }
    }

    override suspend fun driverInfoWithVehicle(): Result<DriverProfileWithVehicle> {
        driverNetworkDataSource.getDriverInfoWithVehicle().let {
            return when (it) {
                is NetworkResult.Error -> Result.Error(it.message)
                is NetworkResult.Success -> {
                    Result.Success(DriverProfileWithVehicle(
                        driverId = it.data.driverId,
                        fullName = it.data.fullName,
                        rating = it.data.rating,
                        color = it.data.vehicleColor.kk,
                        vehicleType = it.data.vehicleType.kk,
                        plateNumber = it.data.plateNumber
                    ))
                    Result.Success(
                        DriverProfileWithVehicle(
                            driverId = it.data.driverId,
                            fullName = it.data.fullName,
                            rating = it.data.rating,
                            color = it.data.color.kk,
                            vehicleType = it.data.vehicleType.kk,
                            plateNumber = it.data.plateNumber
                        )
                    )
                }
            }
        }
    }

    override suspend fun getDriverCard(): Result<DriverCard> {
        driverNetworkDataSource.getDriverCard().let {
            return when (it) {
                is NetworkResult.Error -> Result.Error(it.message)
                is NetworkResult.Success -> {
                    Result.Success(
                        DriverCard(
                            cardNumber = it.data.cardNumber,
                            nameOnCard = it.data.nameOnCard
                        )
                    )
                }
            }
        }
    }

    override suspend fun getDriverBalance(): Result<DriverBalance> {
        driverNetworkDataSource.getDriverBalance().let {
            return when (it) {
                is NetworkResult.Error -> Result.Error(it.message)
                is NetworkResult.Success -> {
                    Result.Success(
                        DriverBalance(
                            balance = it.data.balance ?: 0,
                            dailyBalance = it.data.dayBalance ?: 0
                        )
                    )
                }
            }
        }
    }

    override suspend fun driverLogout(): Result<Boolean> {
        driverNetworkDataSource.driverLogout(NetworkLogoutRequest(localStorage.refresh)).let {
            return when(it){
        driverNetworkDataSource.driverLogout(NetworkDriverLogoutRequest(localStorage.refresh)).let {
            return when (it) {
                is NetworkResult.Error -> Result.Error(it.message)
                is NetworkResult.Success -> {
                    localStorage.clear()
                    Result.Success(data = true)
                }
            }
        }
    }

    override suspend fun driverPhoto(file: File): Result<Boolean> {
        driverNetworkDataSource.driverPhoto(file).let {
            return when(it){
                is NetworkResult.Error -> Result.Error(it.message)
                is NetworkResult.Success -> {
                    Result.Success(data = true)
                }
            }
        }
    }

    override suspend fun getActiveOrders(sendLocationRequest: SendLocationRequest): Result<List<ActiveOfferResponse>> {
        driverNetworkDataSource.getActiveRides(sendLocationRequest.toDTO2()).let {
            return when (it) {
                is NetworkResult.Error -> Result.Error(it.message)
                is NetworkResult.Success -> {
                    val listOfOrders = it.data.map { order -> order.toDomain() }
                    Result.Success(listOfOrders)
                }
            }
        }
    }

}