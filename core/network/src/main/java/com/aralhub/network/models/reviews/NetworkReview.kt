package com.aralhub.network.models.reviews

import com.google.gson.annotations.SerializedName

data class NetworkReview(
    val comment: String,
    @SerializedName("driver_id")
    val driverId: Int,
    @SerializedName("driver_review_types_ids")
    val driverReviewTypeId: List<Int>,
    @SerializedName("passenger_review_types_ids")
    val passengerReviewTypeId: List<Int>,
    val rating: Number,
    @SerializedName("review_type")
    val reviewType: String,
    @SerializedName("ride_id")
    val rideId: Int,
    @SerializedName("user_id")
    val userId: Int
)
