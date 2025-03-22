package com.aralhub.indrive.core.data.repository.review

import com.aralhub.indrive.core.data.model.review.Review
import com.aralhub.indrive.core.data.model.review.ReviewType
import com.aralhub.indrive.core.data.result.Result

interface ReviewRepository {
    suspend fun getPassengerReviewTypes(): Result<List<ReviewType>>
    suspend fun getDriverReviewTypes(): Result<List<ReviewType>>
    suspend fun createReview(review: Review): Result<Review>
}