package com.aralhub.network.api

import com.aralhub.network.models.ServerResponse
import com.aralhub.network.models.reviews.NetworkReviewType
import com.aralhub.network.requests.review.NetworkCreatePassengerReviewTypeRequest
import com.aralhub.network.requests.review.NetworkUpdatePassengerReviewTypeRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReviewsNetworkApi {

    @POST("/reviews/create-passenger-review-type")
    suspend fun createPassengerReviewType(@Body networkCreatePassengerReviewTypeRequest: NetworkCreatePassengerReviewTypeRequest): NetworkReviewType

    @GET("/reviews/passenger-review-types")
    suspend fun getPassengerReviewTypes(): ServerResponse<List<NetworkReviewType>>

    @PUT("reviews/passenger-review-types/{type_id}")
    suspend fun updatePassengerReviewType(@Path("type_id") typeId: Int, @Body networkUpdatePassengerReviewTypeRequest: NetworkUpdatePassengerReviewTypeRequest): String
}