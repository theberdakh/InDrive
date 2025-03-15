package com.aralhub.network.utils

import com.aralhub.network.models.offer.NetworkOffer
import com.aralhub.network.models.ride.NetworkRideActive
import com.aralhub.network.models.ride.NetworkRideStatus
import com.google.gson.annotations.SerializedName


sealed class ClientWebSocketEventNetwork {
    data class DriverOffer(val offer: NetworkOffer): ClientWebSocketEventNetwork()
    data class Unknown(val error: String) : ClientWebSocketEventNetwork()
    data class OfferAccepted(val ride: NetworkRideActive) : ClientWebSocketEventNetwork()
}

data class DriverWaitingClientStatus(
    @SerializedName("free_wait_minutes")
    val freeWaitMinutes: Double,
    @SerializedName("wait_price_per_minute")
    val waitPricePerMinute: Int
)

data class PaidWaitingStartedStatus(
    val message: String,
    @SerializedName("wait_price_per_minute")
    val waitPricePerMinute: Int
)

data class RideStartedStatus(
    val status: String,
    val message: String
)

data class RideStarted(
    val message: String,
    @SerializedName("wait_amount")
    val waitAmount: String
)

data class PaidWaitingStatus(
    val message: String,
    @SerializedName("wait_amount")
    val waitAmount: Double
)


sealed class ClientWebSocketEventRide {
    data class RideUpdate(val networkRideStatus: NetworkRideStatus): ClientWebSocketEventRide()
    data class Unknown(val error: String) : ClientWebSocketEventRide()
}

