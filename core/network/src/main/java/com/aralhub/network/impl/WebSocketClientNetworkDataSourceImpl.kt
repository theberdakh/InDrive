package com.aralhub.network.impl

import android.util.Log
import com.aralhub.network.WebSocketClientNetworkDataSource
import com.aralhub.network.api.WebSocketClientNetworkApi
import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.websocketclient.ClientRideRequest
import com.aralhub.network.models.websocketclient.ClientRideResponse
import com.aralhub.network.models.websocketclient.NetworkGetRecommendedRidePricePoint
import com.aralhub.network.models.websocketclient.NetworkGetRecommendedRidePriceRequest
import com.aralhub.network.models.websocketclient.NetworkGetRecommendedRidePriceResponse
import com.aralhub.network.utils.NetworkEx.safeRequestServerResponse
import javax.inject.Inject

class WebSocketClientNetworkDataSourceImpl @Inject constructor(private val api: WebSocketClientNetworkApi): WebSocketClientNetworkDataSource {

    override suspend fun getRecommendedPrice(points: List<NetworkGetRecommendedRidePricePoint>): NetworkResult<NetworkGetRecommendedRidePriceResponse> {
        return api.getRidePrice(NetworkGetRecommendedRidePriceRequest(points)).safeRequestServerResponse()
    }

}