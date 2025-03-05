package com.aralhub.network.models.offer

import com.google.gson.annotations.SerializedName

data class CreateOfferByDriverResponse(
    @SerializedName("offer_id")
    val offerId: String,
    @SerializedName("ride_uuid")
    val rideUUID: String,
    val amount: Int
)
