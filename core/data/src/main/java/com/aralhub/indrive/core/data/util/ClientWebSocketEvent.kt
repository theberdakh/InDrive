package com.aralhub.indrive.core.data.util

import com.aralhub.indrive.core.data.model.offer.Offer

sealed class ClientWebSocketEvent {
    data class DriverOffer(val offer: Offer): ClientWebSocketEvent()
    data class Unknown(val error: String) : ClientWebSocketEvent()
}
