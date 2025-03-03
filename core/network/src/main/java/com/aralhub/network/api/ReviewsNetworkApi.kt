package com.aralhub.network.api

import com.aralhub.network.models.ServerResponse
import com.aralhub.network.models.reviews.NetworkReview
import com.aralhub.network.models.reviews.NetworkReviewType
import com.aralhub.network.requests.review.NetworkCreatePassengerReviewTypeRequest
import com.aralhub.network.requests.review.NetworkUpdatePassengerReviewTypeRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReviewsNetworkApi {

    @GET("/reviews/passenger-review-types")
    suspend fun getPassengerReviewTypes(): Response<ServerResponse<List<NetworkReviewType>>>

    @GET("/reviews/driver-review-types")
    suspend fun getDriverReviewTypes(): Response<ServerResponse<List<NetworkReviewType>>>

    @POST("/reviews")
    suspend fun createReview(@Body networkReview: NetworkReview): Response<ServerResponse<NetworkReview>>
}