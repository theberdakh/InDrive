package com.aralhub.network

import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.ServerResponse
import com.aralhub.network.models.ServerResponseEmpty
import com.aralhub.network.models.websocketclient.ClientRideRequest
import com.aralhub.network.models.websocketclient.ClientRideResponse

interface WebSocketClientNetworkDataSource {
    suspend fun clientRide(clientRideRequest: ClientRideRequest): NetworkResult<ClientRideResponse>

    suspend fun putClientRideAmount(
        rideId: String,
        amount: Number
    ): NetworkResult<Boolean>

    suspend fun clientAcceptOffer(offerId: String): NetworkResult<Boolean>

    suspend fun clientRejectOffer(offerId: String): NetworkResult<Boolean>

    suspend fun clientCancelOffer(rideId: String): NetworkResult<Boolean>

    suspend fun getActiveOfferByRideId(rideId: String): NetworkResult<Boolean>

    suspend fun getActiveRideByPassenger(userId: Int): NetworkResult<Boolean>

}