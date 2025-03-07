package com.aralhub.indrive.core.data.model.offer

import com.aralhub.network.models.driver.NetworkDriverOffer

data class Offer(
    val offerId: String,
    val rideUuid: String,
    val driver: NetworkDriverOffer,
    val amount: Int,
    val createdAt: String,
    val expiresAt: String
)
