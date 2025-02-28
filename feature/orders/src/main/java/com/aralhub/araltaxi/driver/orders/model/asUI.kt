package com.aralhub.araltaxi.driver.orders.model

import com.aralhub.indrive.core.data.model.offer.ActiveOfferResponse
import com.aralhub.ui.model.OrderItem

fun ActiveOfferResponse.asUI() = with(this) {
    OrderItem(
        id, name, pickUp, avatar, roadPrice, pickUpDistance, roadDistance
    )
}