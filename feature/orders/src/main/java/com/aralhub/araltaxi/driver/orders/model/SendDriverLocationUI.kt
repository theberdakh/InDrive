package com.aralhub.araltaxi.driver.orders.model

import com.aralhub.indrive.core.data.model.location.SendLocationRequest

data class SendDriverLocationUI(
    val type: String = "location_update",
    val latitude: Double,
    val longitude: Double,
    val distance: Int
)

fun SendDriverLocationUI.asDomain() = with(this) {
    SendLocationRequest(
        type, latitude, longitude, distance
    )
}
