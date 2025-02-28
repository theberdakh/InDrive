package com.aralhub.indrive.core.data.model.offer

import com.aralhub.network.models.WebSocketServerResponse
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

fun WebSocketServerResponse<NetworkActiveOfferResponse>.toDomain(): ActiveOfferResponse =
    with(this) {
        return ActiveOfferResponse(
            id = this.data.uuid,
            name = this.data.passenger.userFullName,
            pickUp = this.data.clientPickUpAddress,
            avatar = this.data.passenger.avatar ?: "https://randomuser.me/api/portraits/men/8.jpg",
            roadPrice = this.data.baseAmount.toString(),
            pickUpDistance = this.distanceToClient.distance.toString(),
            roadDistance = this.data.distance.totalDistance.toString()
        )
    }