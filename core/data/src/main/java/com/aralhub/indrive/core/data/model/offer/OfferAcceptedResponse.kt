package com.aralhub.indrive.core.data.model.offer

import com.aralhub.network.models.driver.NetworkActiveRideByDriverResponse
import com.aralhub.network.models.offer.Locations
import com.aralhub.network.models.websocketclient.ClientRideResponsePassenger

data class OfferAcceptedResponse(
    val id: Int,
    val uuid: String,
    val status: String,
    val amount: Int,
    val waitAmount: Int,
    val distance: Float,
    val locations: Locations,
    val isActive: Boolean,
    val passenger: ClientRideResponsePassenger,
    val paymentMethod: Int
)

fun NetworkActiveRideByDriverResponse.toOfferAcceptedDomain() = with(this) {
    OfferAcceptedResponse(
        id = id,
        uuid = uuid,
        status = status,
        amount = amount,
        waitAmount = waitAmount,
        distance = distance,
        locations = locations,
        isActive = isActive,
        passenger = passenger,
        paymentMethod = paymentMethod.id
    )
}