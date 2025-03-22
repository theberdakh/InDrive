package com.aralhub.indrive.core.data.repository.review.impl

import com.aralhub.indrive.core.data.model.review.Review
import com.aralhub.indrive.core.data.model.review.ReviewType
import com.aralhub.indrive.core.data.model.review.toNetworkReview
import com.aralhub.indrive.core.data.model.review.toReview
import com.aralhub.indrive.core.data.model.review.toReviewType
import com.aralhub.indrive.core.data.repository.review.ReviewRepository
import com.aralhub.indrive.core.data.result.Result
import com.aralhub.network.ReviewsNetworkDataSource
import com.aralhub.network.models.NetworkResult
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(private val reviewsNetworkDataSource: ReviewsNetworkDataSource): ReviewRepository {
    override suspend fun getPassengerReviewTypes(): Result<List<ReviewType>> {
        return when(val result = reviewsNetworkDataSource.getPassengerReviewTypes()) {
            is NetworkResult.Success -> Result.Success(result.data.map { it.toReviewType() })
            is NetworkResult.Error -> Result.Error(result.message)
        }
    }

    override suspend fun getDriverReviewTypes(): Result<List<ReviewType>> {
        return when(val result = reviewsNetworkDataSource.getDriverReviewTypes()) {
            is NetworkResult.Success -> Result.Success(result.data.map { it.toReviewType() })
            is NetworkResult.Error -> Result.Error(result.message)
        }
    }

    override suspend fun createReview(review: Review): Result<Review> {
        return when(val result = reviewsNetworkDataSource.createReview(review.toNetworkReview())) {
            is NetworkResult.Success -> Result.Success(result.data.toReview())
            is NetworkResult.Error -> Result.Error(result.message)
        }
    }
}