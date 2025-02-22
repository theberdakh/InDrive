package com.aralhub.network

import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.ServerResponse
import com.aralhub.network.models.driver.DriverInfoWithVehicleResponse
import com.aralhub.network.models.driver.NetworkDriverAuthRequest
import com.aralhub.network.models.driver.NetworkDriverBalanceResponse
import com.aralhub.network.models.driver.NetworkDriverCardRequest
import com.aralhub.network.models.driver.NetworkDriverCardResponse
import com.aralhub.network.models.driver.NetworkDriverInfoResponse
import com.aralhub.network.models.driver.NetworkDriverVerifyRequest
import com.aralhub.network.models.driver.NetworkDriverVerifyResponse

interface DriverNetworkDataSource {
    suspend fun driverAuth(networkDriverAuthRequest: NetworkDriverAuthRequest): NetworkResult<String>
    suspend fun driverVerify(networkDriverVerifyRequest: NetworkDriverVerifyRequest): NetworkResult<NetworkDriverVerifyResponse>
    suspend fun getDriverVehicle(): NetworkResult<String>
    suspend fun getDriverInfo(): NetworkResult<NetworkDriverInfoResponse>
    suspend fun getDriverInfoWithVehicle(): NetworkResult<DriverInfoWithVehicleResponse>
    suspend fun driverCard(networkDriverCardRequest: NetworkDriverCardRequest): NetworkResult<Boolean>
    suspend fun getDriverBalance(): NetworkResult<NetworkDriverBalanceResponse>
    suspend fun getDriverCard(): NetworkResult<NetworkDriverCardResponse>
}