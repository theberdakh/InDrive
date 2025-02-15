package com.aralhub.network

import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.websocketclient.ClientRideRequest
import com.aralhub.network.models.websocketclient.ClientRideResponse

interface WebSocketClientNetworkDataSource {
    fun clientRide(clientRideRequest: ClientRideRequest): NetworkResult<ClientRideResponse>

    fun putClientRideAmount(
        rideId: String,
        amount: Number
    ): NetworkResult<Unit>

    fun clientAcceptOffer(offerId: String): NetworkResult<Unit>

    fun clientRejectOffer(offerId: String): NetworkResult<Unit>

    fun clientCancelOffer(rideId: String): NetworkResult<Unit>

    fun getActiveOfferByRideId(rideId: String): NetworkResult<Unit>

    fun getActiveRideByPassenger(userId: Int): NetworkResult<Unit>

}