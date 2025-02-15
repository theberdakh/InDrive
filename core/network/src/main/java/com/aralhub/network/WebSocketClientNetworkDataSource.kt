package com.aralhub.network

import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.ServerResponse
import com.aralhub.network.models.ServerResponseEmpty
import com.aralhub.network.models.websocketclient.ClientRideRequest
import com.aralhub.network.models.websocketclient.ClientRideResponse

interface WebSocketClientNetworkDataSource {
    fun clientRide(clientRideRequest: ClientRideRequest): NetworkResult<ClientRideResponse>

    fun putClientRideAmount(
        rideId: String,
        amount: Number
    ): NetworkResult<Boolean>

    fun clientAcceptOffer(offerId: String): NetworkResult<Boolean>

    fun clientRejectOffer(offerId: String): NetworkResult<Boolean>

    fun clientCancelOffer(rideId: String): NetworkResult<Boolean>

    fun getActiveOfferByRideId(rideId: String): NetworkResult<Boolean>

    fun getActiveRideByPassenger(userId: Int): NetworkResult<Boolean>

}