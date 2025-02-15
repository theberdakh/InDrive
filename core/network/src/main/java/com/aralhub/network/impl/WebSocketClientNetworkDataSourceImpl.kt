package com.aralhub.network.impl

import com.aralhub.network.WebSocketClientNetworkDataSource
import com.aralhub.network.api.WebSocketClientNetworkApi
import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.websocketclient.ClientRideRequest
import com.aralhub.network.models.websocketclient.ClientRideResponse
import com.aralhub.network.utils.NetworkEx.safeRequestServerResponse

class WebSocketClientNetworkDataSourceImpl(private val api: WebSocketClientNetworkApi): WebSocketClientNetworkDataSource {
    override fun clientRide(clientRideRequest: ClientRideRequest): NetworkResult<ClientRideResponse> {
        return api.clientRide(clientRideRequest).safeRequestServerResponse()
    }

    override fun putClientRideAmount(rideId: String, amount: Number): NetworkResult<Unit> {
        return api.putClientRideAmount(rideId, amount).safeRequestServerResponse()
    }

    override fun clientAcceptOffer(offerId: String): NetworkResult<Unit> {
        return api.clientAcceptOffer(offerId).safeRequestServerResponse()
    }

    override fun clientRejectOffer(offerId: String): NetworkResult<Unit> {
        return api.clientRejectOffer(offerId).safeRequestServerResponse()
    }

    override fun clientCancelOffer(rideId: String): NetworkResult<Unit> {
        return api.clientCancelOffer(rideId).safeRequestServerResponse()
    }

    override fun getActiveOfferByRideId(rideId: String): NetworkResult<Unit> {
        return api.getActiveOfferByRideId(rideId).safeRequestServerResponse()
    }

    override fun getActiveRideByPassenger(userId: Int): NetworkResult<Unit> {
        return api.getActiveRideByPassenger(userId).safeRequestServerResponse()
    }
}