package com.aralhub.araltaxi.ride

enum class FragmentRideStatus(val status: String) {
    DRIVER_ON_THE_WAY("driver_on_the_way"),
    DRIVER_WAITING_CLIENT("driver_waiting_client"),
    PAID_WAITING_STARTED("paid_waiting_started"),
    PAID_WAITING("paid_waiting"),
    RIDE_STARTED("ride_started"),
    RIDE_COMPLETED("ride_completed"),
    CANCELED_BY_DRIVER("cancelled_by_driver")
}