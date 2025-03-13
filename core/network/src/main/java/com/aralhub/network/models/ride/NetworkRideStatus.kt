package com.aralhub.network.models.ride

data class NetworkRideStatus(
    val status: String,
    val message: String
) {
    companion object {
        const val DRIVER_WAITING_CLIENT = "driver_waiting_client"
        const val RIDE_STARTED_AFTER_WAITING = "ride_started_after_waiting"
        const val RIDE_STARTED = "ride_started"
        const val RIDE_COMPLETED = "ride_completed"
        const val DRIVER_ON_THE_WAY = "driver_on_the_way"
        const val CANCELED_BY_DRIVER = "cancelled_by_driver"
    }
}

