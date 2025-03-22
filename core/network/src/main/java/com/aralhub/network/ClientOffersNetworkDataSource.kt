package com.aralhub.network

import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.ServerResponseEmpty

interface ClientOffersNetworkDataSource {
    suspend fun acceptOffer(offerId: String): NetworkResult<ServerResponseEmpty>
    suspend fun rejectOffer(offerId: String): NetworkResult<ServerResponseEmpty>
}