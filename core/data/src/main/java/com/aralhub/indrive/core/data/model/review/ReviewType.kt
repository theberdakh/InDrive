package com.aralhub.indrive.core.data.model.review

import com.aralhub.network.models.reviews.NetworkReviewType


data class ReviewType(
    val name: String,
    val id: Int
)

fun ReviewType.toNetworkReviewType() = NetworkReviewType(
    name = name,
    id = id
)

fun NetworkReviewType.toReviewType() = ReviewType(
    name = name,
    id = id
)
