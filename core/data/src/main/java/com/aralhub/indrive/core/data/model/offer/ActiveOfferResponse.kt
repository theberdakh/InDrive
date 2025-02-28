package com.aralhub.indrive.core.data.model.offer

import com.aralhub.network.models.offer.NetworkActiveOfferResponse

data class ActiveOfferResponse(
    val id: String,
    val name: String,
    val pickUp: String?,
    val avatar: String,
    val roadPrice: String = "",
    val pickUpDistance: String = "",
    val roadDistance: String = "",
)

fun NetworkActiveOfferResponse.toDomain(): ActiveOfferResponse {
    return ActiveOfferResponse(
        id = this.uuid,
        name = this.passenger.userFullName,
        pickUp = this.locations.points.getOrNull(0)?.name,
        avatar = "https://randomuser.me/api/portraits/men/8.jpg",
        roadPrice = this.baseAmount.toString(),
        pickUpDistance = this.distance.totalDistance.toString(),
        roadDistance = this.distance.totalDistance.toString()
    )
}