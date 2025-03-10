package com.aralhub.network.utils

import com.aralhub.network.models.offer.NetworkOffer


sealed class ClientWebSocketEventNetwork {
    data class DriverOffer(val offer: NetworkOffer): ClientWebSocketEventNetwork()
    data class Unknown(val error: String) : ClientWebSocketEventNetwork()

}