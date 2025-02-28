package com.aralhub.network.utils

import com.aralhub.network.models.offer.NetworkActiveOfferResponse

sealed class WebSocketEventNetwork {
    data class RideCancel(val rideId: String) : WebSocketEventNetwork()
    data class ActiveOffer(val offer: NetworkActiveOfferResponse) : WebSocketEventNetwork()
    data class Unknown(val error: String) : WebSocketEventNetwork()
}
