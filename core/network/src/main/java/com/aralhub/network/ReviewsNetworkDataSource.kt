package com.aralhub.network

import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.ServerResponse
import com.aralhub.network.models.reviews.NetworkPassengerReview
import com.aralhub.network.models.reviews.NetworkReview
import com.aralhub.network.models.reviews.NetworkReviewType
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ReviewsNetworkDataSource {
    suspend fun getPassengerReviewTypes(): NetworkResult<List<NetworkReviewType>>

    suspend fun getDriverReviewTypes(): NetworkResult<List<NetworkReviewType>>

    suspend fun createReview(networkReview: NetworkReview): NetworkResult<NetworkReview>
    suspend fun createPassengerReview(networkPassengerReview: NetworkPassengerReview): NetworkResult<NetworkReview>
}