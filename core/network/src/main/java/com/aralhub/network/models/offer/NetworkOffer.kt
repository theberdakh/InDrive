package com.aralhub.network.models.offer

import com.aralhub.network.models.driver.NetworkDriverOffer
import com.google.gson.annotations.SerializedName

data class NetworkOffer(
    @SerializedName("offer_id")
    val offerId: String,
    @SerializedName("ride_uuid")
    val rideUuid: String,
    val driver: NetworkDriverOffer,
    val amount: Double,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("expires_at")
    val expiresAt: String
)