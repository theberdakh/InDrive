package com.aralhub.network.utils

import com.aralhub.network.models.offer.NetworkOffer
import com.aralhub.network.models.ride.NetworkRideActive
import com.google.gson.annotations.SerializedName


sealed class ClientWebSocketEventNetwork {
    data class DriverOffer(val offer: NetworkOffer): ClientWebSocketEventNetwork()
    data class Unknown(val error: String) : ClientWebSocketEventNetwork()
    data class OfferAccepted(val ride: NetworkRideActive) : ClientWebSocketEventNetwork()
}


data class NetworkDriverWaitingClientMessage(
    val message: String,
    @SerializedName("wait_price_per_minute")
    val waitPricePerMinute: Int,
    @SerializedName("start_free_time")
    val startFreeTime: Double,
    @SerializedName("end_free_time")
    val endFreeTime: Double
)

data class NetworkRideStartedMessage(
    val message: String,
    @SerializedName("wait_amount")
    val waitAmount: Double
)

sealed class ClientWebSocketEventRideMessage {
    data class DriverOnTheWay(val message: String): ClientWebSocketEventRideMessage()
    data class DriverWaitingClientMessage(val message: NetworkDriverWaitingClientMessage): ClientWebSocketEventRideMessage()
    data class PaidWaitingStarted(val message: String): ClientWebSocketEventRideMessage()
    data class PaidWaiting(val message: String): ClientWebSocketEventRideMessage()
    data class RideStarted(val message: NetworkRideStartedMessage): ClientWebSocketEventRideMessage()
    data class RideCompleted(val message: String): ClientWebSocketEventRideMessage()
    data class Unknown(val error: String) : ClientWebSocketEventRideMessage()
}

enum class NetworkRideStatus(val status: String) {
    DRIVER_ON_THE_WAY("driver_on_the_way"),
    DRIVER_WAITING_CLIENT("driver_waiting_client"),
    PAID_WAITING_STARTED("paid_waiting_started"),
    PAID_WAITING("paid_waiting"),
    RIDE_STARTED("ride_started"),
    RIDE_COMPLETED("ride_completed"),
}

