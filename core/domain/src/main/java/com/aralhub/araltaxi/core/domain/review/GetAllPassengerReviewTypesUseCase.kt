package com.aralhub.araltaxi.core.domain.review

import com.aralhub.indrive.core.data.repository.review.ReviewRepository
import javax.inject.Inject

class GetAllPassengerReviewTypesUseCase @Inject constructor(private val reviewRepository: ReviewRepository) {
    suspend operator fun invoke() = reviewRepository.getPassengerReviewTypes()
}