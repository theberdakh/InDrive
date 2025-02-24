package com.aralhub.network

import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.websocketclient.ClientRideRequest
import com.aralhub.network.models.websocketclient.ClientRideResponse
import com.aralhub.network.models.websocketclient.NetworkGetRecommendedRidePricePoint
import com.aralhub.network.models.websocketclient.NetworkGetRecommendedRidePriceRequest
import com.aralhub.network.models.websocketclient.NetworkGetRecommendedRidePriceResponse

interface WebSocketClientNetworkDataSource {
/*    suspend fun clientRide(clientRideRequest: ClientRideRequest): NetworkResult<ClientRideResponse>

    suspend fun putClientRideAmount(
        rideId: String,
        amount: Number
    ): NetworkResult<Boolean>

    suspend fun clientAcceptOffer(offerId: String): NetworkResult<Boolean>

    suspend fun clientRejectOffer(offerId: String): NetworkResult<Boolean>

    suspend fun clientCancelOffer(rideId: String): NetworkResult<Boolean>

    suspend fun getActiveOfferByRideId(rideId: String): NetworkResult<Boolean>

    suspend fun getActiveRideByPassenger(userId: Int): NetworkResult<Boolean>*/

    suspend fun getRecommendedPrice(points: List<NetworkGetRecommendedRidePricePoint>): NetworkResult<NetworkGetRecommendedRidePriceResponse>

    suspend fun clientRide(clientRideRequest: ClientRideRequest): NetworkResult<ClientRideResponse>

}