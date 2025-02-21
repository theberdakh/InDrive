package com.aralhub.network.impl

import com.aralhub.network.DriverNetworkDataSource
import com.aralhub.network.api.DriverNetworkApi
import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.driver.NetworkDriverAuthRequest
import com.aralhub.network.models.driver.NetworkDriverCardRequest
import com.aralhub.network.models.driver.NetworkDriverInfoResponse
import com.aralhub.network.models.driver.NetworkDriverVerifyRequest
import com.aralhub.network.models.driver.NetworkDriverVerifyResponse
import com.aralhub.network.utils.NetworkEx.safeRequestServerResponse
import javax.inject.Inject

class DriverNetworkDataSourceImpl @Inject constructor(private val api: DriverNetworkApi): DriverNetworkDataSource {
    override suspend fun driverAuth(networkDriverAuthRequest: NetworkDriverAuthRequest): NetworkResult<String> {
        return  api.driverAuth(networkDriverAuthRequest).safeRequestServerResponse()
    }

    override suspend fun driverVerify(networkDriverVerifyRequest: NetworkDriverVerifyRequest): NetworkResult<NetworkDriverVerifyResponse> {
        return api.driverVerify(networkDriverVerifyRequest).safeRequestServerResponse()
    }

    override suspend fun getDriverVehicle(): NetworkResult<String> {
        return api.getDriverVehicle().safeRequestServerResponse()
    }

    override suspend fun getDriverInfo(): NetworkResult<NetworkDriverInfoResponse> {
        return api.getDriverInfo().safeRequestServerResponse()
    }

    override suspend fun getDriverInfoWithVehicle(): NetworkResult<String> {
        return api.getDriverInfoWithVehicle().safeRequestServerResponse()
    }

    override suspend fun driverCard(networkDriverCardRequest: NetworkDriverCardRequest): NetworkResult<String> {
        return api.driverCard(networkDriverCardRequest).safeRequestServerResponse()
    }
}