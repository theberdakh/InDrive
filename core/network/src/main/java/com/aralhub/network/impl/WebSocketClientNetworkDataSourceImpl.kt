package com.aralhub.network.impl

import com.aralhub.network.WebSocketClientNetworkDataSource
import com.aralhub.network.api.WebSocketClientNetworkApi
import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.ServerResponseEmpty
import com.aralhub.network.models.location.NetworkLocationPoint
import com.aralhub.network.models.price.NetworkRecommendedPrice
import com.aralhub.network.models.ride.NetworkRideActive
import com.aralhub.network.models.ride.NetworkRideSearch
import com.aralhub.network.requests.price.NetworkRecommendedRidePriceRequest
import com.aralhub.network.requests.ride.NetworkClientRideRequest
import com.aralhub.network.utils.ex.NetworkEx.safeRequest
import com.aralhub.network.utils.ex.NetworkEx.safeRequestEmpty
import com.aralhub.network.utils.ex.NetworkEx.safeRequestServerResponse
import retrofit2.Response
import javax.inject.Inject

class WebSocketClientNetworkDataSourceImpl @Inject constructor(private val api: WebSocketClientNetworkApi) :
    WebSocketClientNetworkDataSource {
    override suspend fun getActiveRideByPassenger(userId: Int): NetworkResult<NetworkRideActive> {
        return api.getActiveRideByPassenger(userId).safeRequestServerResponse()
    }

    override suspend fun getSearchRideByPassengerId(userId: Int): NetworkResult<NetworkRideSearch> {
        return api.getSearchRideByPassengerId(userId).safeRequestServerResponse()
    }

    override suspend fun updateSearchRideAmount(
        rideId: String,
        amount: Number
    ): NetworkResult<ServerResponseEmpty> {
        return api.updateSearchRideAmount(rideId, amount).safeRequest()
    }

    override suspend fun getRecommendedPrice(points: List<NetworkLocationPoint>): NetworkResult<NetworkRecommendedPrice> {
        return api.getRidePrice(NetworkRecommendedRidePriceRequest(points))
            .safeRequestServerResponse()
    }

    override suspend fun clientRide(networkClientRideRequest: NetworkClientRideRequest): NetworkResult<NetworkRideSearch> {
        return api.clientRide(networkClientRideRequest).safeRequestServerResponse()
    }

    override suspend fun clientCancelSearchRide(rideId: String): NetworkResult<ServerResponseEmpty> {
        return api.clientCancelSearchRide(rideId).safeRequest()
    }
}