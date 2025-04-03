package com.aralhub.network.models.reviews

import com.google.gson.annotations.SerializedName

data class NetworkPassengerReview(
    @SerializedName("driver_id")
    val driverId: Int,
    @SerializedName("ride_id")
    val rideId: Int,
    val rating: Number,
    val comment: String,
    @SerializedName("driver_review_types_ids")
    val driverReviewTypesIds: List<Int> = emptyList()
)
