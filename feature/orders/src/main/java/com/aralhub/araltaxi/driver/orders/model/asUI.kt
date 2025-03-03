package com.aralhub.araltaxi.driver.orders.model

import com.aralhub.indrive.core.data.model.offer.ActiveOfferResponse
import com.aralhub.ui.model.OrderItem
import com.aralhub.ui.model.PaymentMethod

fun ActiveOfferResponse.asUI() = with(this) {
    OrderItem(
        id = id,
        name = name,
        pickUp = pickUp,
        avatar = avatar,
        roadPrice = roadPrice,
        pickUpDistance = pickUpDistance,
        roadDistance = roadDistance,
        paymentType = if (paymentType == 1) PaymentMethod.CASH else PaymentMethod.CARD,
        pickUpAddress = pickUpAddress,
        destinationAddress = destinationAddress
    )
}