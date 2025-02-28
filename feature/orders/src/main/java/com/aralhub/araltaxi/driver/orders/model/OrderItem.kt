package com.aralhub.araltaxi.driver.orders.model

import com.aralhub.indrive.core.data.model.offer.ActiveOfferResponse

data class OrderItem(
    val id: String,
    val name: String,
    val pickUp: String?,
    val avatar: String,
    val roadPrice: String = "",
    val pickUpDistance: String = "",
    val roadDistance: String = "",
)

fun ActiveOfferResponse.asUI() = with(this) {
    OrderItem(
        id, name, pickUp, avatar, roadDistance, pickUpDistance, roadDistance
    )
}