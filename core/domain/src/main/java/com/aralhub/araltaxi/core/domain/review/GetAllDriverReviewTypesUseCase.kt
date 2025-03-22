package com.aralhub.araltaxi.core.domain.review

import com.aralhub.indrive.core.data.model.review.ReviewType
import com.aralhub.indrive.core.data.repository.review.ReviewRepository
import com.aralhub.indrive.core.data.result.Result
import javax.inject.Inject

class GetAllDriverReviewTypesUseCase @Inject constructor(private val reviewRepository: ReviewRepository) {
    suspend operator fun invoke(): Result<List<ReviewType>> {
        return reviewRepository.getPassengerReviewTypes()
    }
}