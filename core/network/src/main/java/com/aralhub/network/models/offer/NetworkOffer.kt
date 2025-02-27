package com.aralhub.network.models.offer

import com.aralhub.network.models.driver.NetworkDriverActive
import com.google.gson.annotations.SerializedName

data class NetworkOffer(
    @SerializedName("offer_id")
    val offerId: String,
    @SerializedName("ride_uuid")
    val rideUuid: String,
    val driver: NetworkDriverActive,
    val amount: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("expires_at")
    val expiresAt: String
)