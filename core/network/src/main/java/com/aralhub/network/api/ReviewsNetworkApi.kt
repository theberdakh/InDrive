package com.aralhub.network.api

import com.aralhub.network.models.ServerResponse
import com.aralhub.network.models.reviews.NetworkCreatePassengerReviewTypeRequest
import com.aralhub.network.models.reviews.NetworkCreatePassengerReviewTypeResponse
import com.aralhub.network.models.reviews.NetworkPassengerReviewTypeResponse
import com.aralhub.network.models.reviews.NetworkUpdatePassengerReviewTypeRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReviewsNetworkApi {

    /** Create passenger review type */
    @POST("/reviews/create-passenger-review-type")
    suspend fun createPassengerReviewType(@Body networkCreatePassengerReviewTypeRequest: NetworkCreatePassengerReviewTypeRequest): NetworkCreatePassengerReviewTypeResponse

    /** Get all passenger review types */
    @GET("/reviews/passenger-review-types")
    suspend fun getPassengerReviewTypes(): ServerResponse<List<NetworkPassengerReviewTypeResponse>>

    @PUT("reviews/passenger-review-types/{type_id}")
    suspend fun updatePassengerReviewType(@Path("type_id") typeId: Int, @Body networkUpdatePassengerReviewTypeRequest: NetworkUpdatePassengerReviewTypeRequest): String
}