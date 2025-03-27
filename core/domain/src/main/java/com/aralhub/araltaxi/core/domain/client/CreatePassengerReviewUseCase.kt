package com.aralhub.araltaxi.core.domain.client

import com.aralhub.indrive.core.data.model.review.PassengerReview
import com.aralhub.indrive.core.data.repository.review.ReviewRepository
import javax.inject.Inject

class CreatePassengerReviewUseCase @Inject constructor(private val reviewRepository: ReviewRepository) {

    suspend operator fun invoke(passengerReview: PassengerReview) = reviewRepository.createPassengerReview(passengerReview)
}