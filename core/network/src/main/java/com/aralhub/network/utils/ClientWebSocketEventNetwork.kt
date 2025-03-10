package com.aralhub.network.utils

import com.aralhub.network.models.offer.NetworkOffer
import com.aralhub.network.models.ride.NetworkRideActive


sealed class ClientWebSocketEventNetwork {
    data class DriverOffer(val offer: NetworkOffer): ClientWebSocketEventNetwork()
    data class Unknown(val error: String) : ClientWebSocketEventNetwork()
    data class OfferAccepted(val ride: NetworkRideActive) : ClientWebSocketEventNetwork()
}

