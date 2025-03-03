package com.aralhub.araltaxi.core.domain.review

import com.aralhub.indrive.core.data.model.review.Review
import com.aralhub.indrive.core.data.repository.review.ReviewRepository
import javax.inject.Inject

class CreateReviewUseCase @Inject constructor(private val reviewRepository: ReviewRepository) {
    suspend operator fun invoke(review: Review) = reviewRepository.createReview(review)
}