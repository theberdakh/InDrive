package com.aralhub.araltaxi.driver.orders.model

enum class RideStatus(val status: String) {
    DRIVER_ON_THE_WAY("driver_on_the_way"),
    DRIVER_WAITING_CLIENT("driver_waiting_client"),
    RIDE_STARTED("ride_started"),
    WAITING_FOR_CLIENT("waiting_for_client"), //waiting while ride
    RIDE_STARTED_AFTER_WAITING("ride_started_after_waiting"),
    RIDE_COMPLETED("ride_completed")
}