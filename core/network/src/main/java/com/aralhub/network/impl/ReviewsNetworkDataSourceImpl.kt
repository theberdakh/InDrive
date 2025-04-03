package com.aralhub.network.impl

import com.aralhub.network.ReviewsNetworkDataSource
import com.aralhub.network.api.ReviewsNetworkApi
import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.reviews.NetworkPassengerReview
import com.aralhub.network.models.reviews.NetworkReview
import com.aralhub.network.models.reviews.NetworkReviewType
import com.aralhub.network.utils.ex.NetworkEx.safeRequestServerResponse
import javax.inject.Inject

class ReviewsNetworkDataSourceImpl @Inject constructor(private val reviewsNetworkApi: ReviewsNetworkApi): ReviewsNetworkDataSource {
    override suspend fun getPassengerReviewTypes(): NetworkResult<List<NetworkReviewType>> {
        return reviewsNetworkApi.getPassengerReviewTypes().safeRequestServerResponse()
    }

    override suspend fun getDriverReviewTypes(): NetworkResult<List<NetworkReviewType>> {
        return reviewsNetworkApi.getDriverReviewTypes().safeRequestServerResponse()
    }

    override suspend fun createReview(networkReview: NetworkReview): NetworkResult<NetworkReview> {
        return reviewsNetworkApi.createReview(networkReview).safeRequestServerResponse()
    }

    override suspend fun createPassengerReview(networkPassengerReview: NetworkPassengerReview): NetworkResult<NetworkReview> {
        return reviewsNetworkApi.createPassengerReview(networkPassengerReview).safeRequestServerResponse()
    }
}