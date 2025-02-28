package com.aralhub.network.models.offer

import com.google.gson.annotations.SerializedName

data class NetworkOfferCancelResponse(
    @SerializedName("ride_id")
    val rideId: String
)
