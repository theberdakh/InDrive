package com.aralhub.network

import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.ServerResponseEmpty
import com.aralhub.network.models.location.NetworkLocationPoint
import com.aralhub.network.models.price.NetworkRecommendedPrice
import com.aralhub.network.models.ride.NetworkRideActive
import com.aralhub.network.models.ride.NetworkRideSearch
import com.aralhub.network.requests.ride.NetworkClientRideRequest
import retrofit2.Response

interface WebSocketClientNetworkDataSource {

    suspend fun getActiveRideByPassenger(userId: Int): NetworkResult<NetworkRideActive>

    suspend fun getSearchRideByPassengerId(userId: Int): NetworkResult<NetworkRideSearch>

    suspend fun getRecommendedPrice(points: List<NetworkLocationPoint>): NetworkResult<NetworkRecommendedPrice>

    suspend fun clientRide(networkClientRideRequest: NetworkClientRideRequest): NetworkResult<NetworkRideSearch>

    suspend fun clientCancelSearchRide(rideId: String): NetworkResult<ServerResponseEmpty>

}