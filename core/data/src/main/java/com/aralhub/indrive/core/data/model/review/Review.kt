package com.aralhub.indrive.core.data.model.review

import com.aralhub.network.models.reviews.NetworkReview

data class Review(
    val comment: String,
    val driverId: Int,
    val driverReviewTypeId: List<Int>,
    val passengerReviewTypeId: List<Int>,
    val rating: Number,
    val reviewType: String,
    val rideId: Int,
    val userId: Int
)

fun Review.toNetworkReview() = NetworkReview(
    comment = comment,
    driverId = driverId,
    driverReviewTypeId = driverReviewTypeId,
    passengerReviewTypeId = passengerReviewTypeId,
    rating = rating,
    reviewType = reviewType,
    rideId = rideId,
    userId = userId
)

fun NetworkReview.toReview() = Review(
    comment = comment,
    driverId = driverId,
    driverReviewTypeId = driverReviewTypeId,
    passengerReviewTypeId = passengerReviewTypeId,
    rating = rating,
    reviewType = reviewType,
    rideId = rideId,
    userId = userId
)