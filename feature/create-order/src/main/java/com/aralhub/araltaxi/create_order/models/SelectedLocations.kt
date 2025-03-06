package com.aralhub.araltaxi.create_order.models

import java.io.Serializable


data class SelectedLocations(
    val from: SelectedLocation,
    val to: SelectedLocation
): Serializable

data class SelectedLocation(
    val name: String,
    val longitude: Double,
    val latitude: Double,
    val locationType: LocationType
)

enum class LocationType {
    FROM, TO
}
