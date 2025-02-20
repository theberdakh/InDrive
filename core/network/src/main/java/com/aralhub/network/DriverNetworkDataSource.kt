package com.aralhub.network

import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.ServerResponse
import com.aralhub.network.models.driver.NetworkDriverAuthRequest
import com.aralhub.network.models.driver.NetworkDriverCardRequest
import com.aralhub.network.models.driver.NetworkDriverVerifyRequest

interface DriverNetworkDataSource {
    suspend fun driverAuth(networkDriverAuthRequest: NetworkDriverAuthRequest): NetworkResult<String>
    suspend fun driverVerify(networkDriverVerifyRequest: NetworkDriverVerifyRequest): NetworkResult<String>
    suspend fun getDriverVehicle(): NetworkResult<String>
    suspend fun getDriverInfo(): NetworkResult<String>
    suspend fun getDriverInfoWithVehicle(): NetworkResult<String>
    suspend fun driverCard(networkDriverCardRequest: NetworkDriverCardRequest): NetworkResult<String>
}