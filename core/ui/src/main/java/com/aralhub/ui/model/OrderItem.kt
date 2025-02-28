package com.aralhub.ui.model

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
        id, name, pickUp, avatar, roadPrice, pickUpDistance, roadDistance
    )
}