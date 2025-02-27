package com.aralhub.network.models.ride

import com.google.gson.annotations.SerializedName

data class NetworkActiveOffer(
    @SerializedName("offer_id")
    val offerId: String,
    @SerializedName("ride_uuid")
    val rideUuid: String,
    val driver: NetworkActiveRideDriver,
    val amount: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("expires_at")
    val expiresAt: String
)