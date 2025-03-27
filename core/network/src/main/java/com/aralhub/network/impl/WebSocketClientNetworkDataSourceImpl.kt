package com.aralhub.network.impl

import com.aralhub.network.WebSocketClientNetworkDataSource
import com.aralhub.network.api.WebSocketClientNetworkApi
import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.ServerResponseEmpty
import com.aralhub.network.models.location.NetworkLocationPoint
import com.aralhub.network.models.price.NetworkRecommendedPrice
import com.aralhub.network.models.ride.NetworkRideActive
import com.aralhub.network.models.ride.NetworkRideSearch
import com.aralhub.network.models.ride.NetworkWaitAmount
import com.aralhub.network.requests.price.NetworkRecommendedRidePriceRequest
import com.aralhub.network.requests.ride.NetworkClientRideRequest
import com.aralhub.network.utils.ex.NetworkEx.safeRequest
import com.aralhub.network.utils.ex.NetworkEx.safeRequestServerResponse
import javax.inject.Inject

class WebSocketClientNetworkDataSourceImpl @Inject constructor(private val api: WebSocketClientNetworkApi) :
    WebSocketClientNetworkDataSource {

    override suspend fun cancelRide(
        rideId: Int,
        cancelCauseId: Int
    ): NetworkResult<ServerResponseEmpty> {
        return api.cancelRide(rideId, cancelCauseId).safeRequest()
    }

    override suspend fun cancelRideByPassenger(rideId: Int): NetworkResult<ServerResponseEmpty> {
        return api.cancelRideByPassenger(rideId).safeRequest()
    }

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

    override suspend fun updateAutoTake(
        rideId: String,
        autoTake: Boolean
    ): NetworkResult<ServerResponseEmpty> {
        return api.updateAutoTake(rideId, autoTake).safeRequest()
    }

    override suspend fun getWaitTime(rideId: Int): NetworkResult<NetworkWaitAmount> {
        return api.getWaitAmount(rideId).safeRequestServerResponse()
    }
}