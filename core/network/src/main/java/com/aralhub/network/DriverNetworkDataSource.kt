package com.aralhub.network

import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.ServerResponse
import com.aralhub.network.models.driver.NetworkDriverAuthRequest
import com.aralhub.network.models.driver.NetworkDriverCardRequest

interface DriverNetworkDataSource {
    fun driverAuth(networkDriverAuthRequest: NetworkDriverAuthRequest): NetworkResult<String>
    fun driverVerify(networkDriverAuthRequest: NetworkDriverAuthRequest): NetworkResult<String>
    fun getDriverVehicle(): NetworkResult<String>
    fun getDriverInfo(): NetworkResult<String>
    fun getDriverInfoWithVehicle(): NetworkResult<String>
    fun driverCard(networkDriverCardRequest: NetworkDriverCardRequest): NetworkResult<String>
}