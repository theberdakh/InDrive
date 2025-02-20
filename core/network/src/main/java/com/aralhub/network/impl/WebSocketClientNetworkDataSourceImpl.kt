package com.aralhub.network.impl

import com.aralhub.network.WebSocketClientNetworkDataSource
import com.aralhub.network.api.WebSocketClientNetworkApi
import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.websocketclient.ClientRideRequest
import com.aralhub.network.models.websocketclient.ClientRideResponse
import com.aralhub.network.utils.NetworkEx.safeRequestServerResponse

//class WebSocketClientNetworkDataSourceImpl(private val api: WebSocketClientNetworkApi) :
//    WebSocketClientNetworkDataSource {
//    override suspend fun clientRide(clientRideRequest: ClientRideRequest): NetworkResult<ClientRideResponse> {
//        return api.clientRide(clientRideRequest).safeRequestServerResponse()
//    }
//    override suspend fun putClientRideAmount(
//        rideId: String,
//        amount: Number
//    ): NetworkResult<Boolean> {
//        return api.putClientRideAmount(rideId, amount).safeRequestServerResponseEmpty()
//    }
//
//    override suspend fun clientAcceptOffer(offerId: String): NetworkResult<Boolean> {
//        return api.clientAcceptOffer(offerId).safeRequestServerResponseEmpty()
//    }
//
//    override suspend fun clientRejectOffer(offerId: String): NetworkResult<Boolean> {
//        return api.clientRejectOffer(offerId).safeRequestServerResponseEmpty()
//    }
//
//    override suspend fun clientCancelOffer(rideId: String): NetworkResult<Boolean> {
//        return api.clientCancelOffer(rideId).safeRequestServerResponseEmpty()
//    }
//
//    override suspend fun getActiveOfferByRideId(rideId: String): NetworkResult<Boolean> {
//        return api.getActiveOfferByRideId(rideId).safeRequestServerResponseEmpty()
//    }
//
//    override suspend fun getActiveRideByPassenger(userId: Int): NetworkResult<Boolean> {
//        return api.getActiveRideByPassenger(userId).safeRequestServerResponseEmpty()
//    }
//}