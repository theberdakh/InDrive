package com.aralhub.network.models.location

data class NetworkSendLocationRequest(
    val type: String = "location_update",
    val latitude: Double,
    val longitude: Double,
    val distance: Int
)
