package com.aralhub.network.impl

import com.aralhub.network.RideOptionNetworkDataSource
import com.aralhub.network.api.RideOptionNetworkApi
import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.rideoption.NetworkRideOption
import com.aralhub.network.utils.NetworkEx.safeRequestServerResponse
import javax.inject.Inject

class RideOptionNetworkDataSourceImpl @Inject constructor(private val api: RideOptionNetworkApi):
    RideOptionNetworkDataSource {
    override suspend fun getRideOptions(): NetworkResult<List<NetworkRideOption>> {
        return api.getRideOptions().safeRequestServerResponse()
    }
}