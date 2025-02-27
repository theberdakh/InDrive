package com.aralhub.network

import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.rideoption.NetworkRideOption

interface RideOptionNetworkDataSource {
    suspend fun getRideOptions(): NetworkResult<List<NetworkRideOption>>
}