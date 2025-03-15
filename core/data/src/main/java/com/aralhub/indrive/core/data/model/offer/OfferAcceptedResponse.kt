package com.aralhub.indrive.core.data.model.offer

import com.aralhub.network.models.driver.NetworkActiveRideByDriverResponse

data class OfferAcceptedResponse(
    val id: Int,
    val uuid: String,
    val status: String,
    val amount: Int,
    val waitAmount: Int,
    val distance: Float,
    val locations: List<ClientRideLocationsItems>,
    val isActive: Boolean,
    val name: String,
    val avatar: String?,
    val paymentMethod: Int
)

fun NetworkActiveRideByDriverResponse.toDomain() = with(this) {
    OfferAcceptedResponse(
        id = id,
        uuid = uuid,
        status = status,
        amount = amount,
        waitAmount = waitAmount,
        distance = distance,
        locations = locations.points.map {
            ClientRideLocationsItems(
                ClientRideLocationsItemsCoordinates(
                    it.coordinates.longitude,
                    it.coordinates.latitude
                ),
                it.name
            )
        },
        isActive = isActive,
        name = passenger.userFullName,
        avatar = passenger.avatar,
        paymentMethod = paymentMethod.id
    )
}