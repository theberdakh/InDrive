package com.aralhub.indrive.core.data.util

import com.aralhub.indrive.core.data.model.offer.ActiveOfferResponse

sealed class WebSocketEvent {
    data class RideCancel(val rideId: String) : WebSocketEvent()
    data class OfferReject(val rideUUID: String) : WebSocketEvent()
    data class ActiveOffer(val order: ActiveOfferResponse) : WebSocketEvent()
    data class OfferAccepted(val rideId: Int) : WebSocketEvent()
    data class Unknown(val error: String) : WebSocketEvent()
}