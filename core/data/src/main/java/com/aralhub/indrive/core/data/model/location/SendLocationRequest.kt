package com.aralhub.indrive.core.data.model.location

import com.aralhub.network.models.location.NetworkSendLocationRequest

data class SendLocationRequest(
    val type: String = "location_update",
    val latitude: Double,
    val longitude: Double,
    val distance: Int
)

fun SendLocationRequest.toDTO() = with(this) {
    NetworkSendLocationRequest(
        type, latitude, longitude, distance
    )
}
