package com.aralhub.network.utils

import com.aralhub.network.models.ClientWebSocketServerResponse
import com.aralhub.network.models.offer.NetworkOffer


sealed class ClientWebSocketEvent {
    data class DriverOffer(val offer: ClientWebSocketServerResponse<NetworkOffer>): ClientWebSocketEvent()
    data class Unknown(val error: String) : ClientWebSocketEvent()

}