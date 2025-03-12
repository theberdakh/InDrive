package com.aralhub.indrive.core.data.util

import com.aralhub.indrive.core.data.model.offer.Offer
import com.aralhub.indrive.core.data.model.ride.ActiveRide

sealed class ClientWebSocketEvent {
    data class DriverOffer(val offer: Offer): ClientWebSocketEvent()
    data class Unknown(val error: String) : ClientWebSocketEvent()
    data class OfferAccepted(val ride: ActiveRide) : ClientWebSocketEvent()
}
