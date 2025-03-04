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
    val paymentType: Int,
    val pickUpAddress: String,
    val destinationAddress: String? = null
)

fun WebSocketServerResponse<NetworkActiveOfferResponse>.toDomain(): ActiveOfferResponse =
    with(this) {
        return ActiveOfferResponse(
            id = data.uuid,
            name = data.passenger.userFullName,
            pickUp = data.clientPickUpAddress,
            avatar = data.passenger.avatar ?: "https://randomuser.me/api/portraits/men/8.jpg",
            roadPrice = data.baseAmount.toString(),
            pickUpDistance = distanceToClient.distance.toString(),
            roadDistance = data.distance.totalDistance.toString(),
            paymentType = data.paymentMethod.id,
            pickUpAddress = data.clientPickUpAddress,
            destinationAddress = data.locations.points.getOrNull(1)?.name
        )
    }

fun NetworkActiveOfferResponse.toDomain(): ActiveOfferResponse =
    with(this) {
        return ActiveOfferResponse(
            id = uuid,
            name = passenger.userFullName,
            pickUp = clientPickUpAddress,
            avatar = passenger.avatar ?: "https://randomuser.me/api/portraits/men/8.jpg",
            roadPrice = baseAmount.toString(),
            pickUpDistance = distance.toString(),
            roadDistance = distance.totalDistance.toString(),
            paymentType = paymentMethod.id,
            pickUpAddress = clientPickUpAddress,
            destinationAddress = locations.points.getOrNull(1)?.name
        )
    }