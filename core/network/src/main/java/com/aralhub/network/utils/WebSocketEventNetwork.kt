package com.aralhub.network.utils

import com.aralhub.network.models.WebSocketServerResponse
import com.aralhub.network.models.driver.NetworkActiveRideByDriverResponse
import com.aralhub.network.models.offer.NetworkActiveOfferResponse

sealed class WebSocketEventNetwork {
    data class RideCancel(val rideId: String) : WebSocketEventNetwork()
    data class OfferReject(val rideUUID: String) : WebSocketEventNetwork()
    data class ActiveOffer(val offer: WebSocketServerResponse<NetworkActiveOfferResponse>) :
        WebSocketEventNetwork()
    data class OfferAccepted(val rideId: Int) :
        WebSocketEventNetwork()
    data class Unknown(val error: String) : WebSocketEventNetwork()
}

sealed class StartedRideWebSocketEventNetwork {
    data object RideCancelledByPassenger: StartedRideWebSocketEventNetwork()
    data class UnknownAction(val error: String): StartedRideWebSocketEventNetwork()
}
