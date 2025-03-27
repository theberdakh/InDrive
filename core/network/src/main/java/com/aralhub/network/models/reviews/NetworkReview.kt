package com.aralhub.network.models.reviews

import com.google.gson.annotations.SerializedName

data class NetworkReview(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("driver_id")
    val driverId: Int,
    @SerializedName("ride_id")
    val rideId: Int,
    @SerializedName("review_type")
    val reviewType: String,
    @SerializedName("passenger_review_types")
    val passengerReviewTypeId: List<Int>,
    @SerializedName("driver_review_types")
    val driverReviewTypeId: List<Int>,
    val rating: Number,
    val comment: String
)
