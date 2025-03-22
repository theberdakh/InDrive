package com.aralhub.network

import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.option.NetworkOption

interface RideOptionNetworkDataSource {
    suspend fun getRideOptions(): NetworkResult<List<NetworkOption>>
}