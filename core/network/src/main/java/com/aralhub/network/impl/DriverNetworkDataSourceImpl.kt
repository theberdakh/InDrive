package com.aralhub.network.impl

import com.aralhub.network.DriverNetworkDataSource
import com.aralhub.network.api.DriverNetworkApi
import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.driver.NetworkDriverAuthRequest
import com.aralhub.network.models.driver.NetworkDriverCardRequest
import com.aralhub.network.utils.NetworkEx.safeRequest

class DriverNetworkDataSourceImpl(private val api: DriverNetworkApi): DriverNetworkDataSource {
    override fun driverAuth(networkDriverAuthRequest: NetworkDriverAuthRequest): NetworkResult<String> {
        return api.driverAuth(networkDriverAuthRequest).safeRequest()
    }

    override fun driverVerify(networkDriverAuthRequest: NetworkDriverAuthRequest): NetworkResult<String> {
        return api.driverVerify(networkDriverAuthRequest).safeRequest()
    }

    override fun getDriverVehicle(): NetworkResult<String> {
        return api.getDriverVehicle().safeRequest()
    }

    override fun getDriverInfo(): NetworkResult<String> {
        return api.getDriverInfo().safeRequest()
    }

    override fun getDriverInfoWithVehicle(): NetworkResult<String> {
        return api.getDriverInfoWithVehicle().safeRequest()
    }

    override fun driverCard(networkDriverCardRequest: NetworkDriverCardRequest): NetworkResult<String> {
        return api.driverCard(networkDriverCardRequest).safeRequest()
    }
}