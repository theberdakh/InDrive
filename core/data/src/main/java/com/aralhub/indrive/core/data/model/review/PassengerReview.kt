package com.aralhub.indrive.core.data.model.review

import com.aralhub.network.models.reviews.NetworkPassengerReview

/*
*
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
*/
data class PassengerReview(
    val driverId: Int,
    val rideId: Int,
    val rating: Number,
    val comment: String
)

fun PassengerReview.toNetwork() = NetworkPassengerReview(
    driverId = this.driverId,
    rideId = this.rideId,
    rating = this.rating,
    comment = this.comment
)
