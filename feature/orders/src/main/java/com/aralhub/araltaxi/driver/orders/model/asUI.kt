package com.aralhub.araltaxi.driver.orders.model

import com.aralhub.indrive.core.data.model.offer.ActiveOfferResponse
import com.aralhub.indrive.core.data.model.offer.OfferAcceptedResponse
import com.aralhub.ui.model.ClientRideLocationsCoordinatesUI
import com.aralhub.ui.model.ClientRideLocationsUI
import com.aralhub.ui.model.OrderItem
import com.aralhub.ui.model.PaymentType

fun ActiveOfferResponse.asUI() = with(this) {
    val paymentType = if (paymentType == 1) PaymentType.CASH else PaymentType.CARD
    val pickUpDistanceValue =
        if (pickUpDistance < 1000) "${pickUpDistance.toInt()} m" else "${pickUpDistance.toInt() / 1000} km"
    val roadDistanceValue =
        if (roadDistance < 1000) "${roadDistance.toInt()} m" else "${roadDistance.toInt() / 1000} km"
    OrderItem(
        id = id,
        uuid = uuid,
        name = name,
        pickUp = pickUp,
        avatar = avatar,
        roadPrice = roadPrice,
        pickUpDistance = pickUpDistanceValue,
        roadDistance = roadDistanceValue,
        paymentType = paymentType,
        pickUpAddress = pickUpAddress,
        destinationAddress = destinationAddress,
        locations = locations.map {
            ClientRideLocationsUI(
                ClientRideLocationsCoordinatesUI(
                    it.coordinates.longitude.toDouble(),
                    it.coordinates.latitude.toDouble()
                ), it.name
            )
        }
    )
}

fun OfferAcceptedResponse.asUI() = with(this) {
    val paymentType = if (paymentMethod == 1) PaymentType.CASH else PaymentType.CARD
    val pickUpDistanceValue =
        if (distance < 1000) "${distance.toInt()} m" else "${distance.toInt() / 1000} km"
    val roadDistanceValue =
        if (distance < 1000) "${distance.toInt()} m" else "${distance.toInt() / 1000} km"
    OrderItem(
        id = id,
        uuid = uuid,
        name = name,
        pickUp = locations.getOrNull(0)?.name,
        avatar = avatar.toString(),
        roadPrice = amount.toString(),
        pickUpDistance = pickUpDistanceValue,
        roadDistance = roadDistanceValue,
        paymentType = paymentType,
        pickUpAddress = locations.getOrNull(0)?.name,
        destinationAddress = locations.getOrNull(1)?.name,
        locations = locations.map {
            ClientRideLocationsUI(
                ClientRideLocationsCoordinatesUI(
                    it.coordinates.longitude.toDouble(),
                    it.coordinates.latitude.toDouble()
                ), it.name
            )
        }
    )
}