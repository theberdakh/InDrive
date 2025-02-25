package com.aralhub.network.api

import com.aralhub.network.models.ServerResponse
import com.aralhub.network.models.ServerResponseEmpty
import com.aralhub.network.models.ride.NetworkActiveRideResponse
import com.aralhub.network.models.websocketclient.ClientRideRequest
import com.aralhub.network.models.websocketclient.ClientRideResponse
import com.aralhub.network.models.websocketclient.NetworkGetRecommendedRidePricePoint
import com.aralhub.network.models.websocketclient.NetworkGetRecommendedRidePriceRequest
import com.aralhub.network.models.websocketclient.NetworkGetRecommendedRidePriceResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface WebSocketClientNetworkApi {
    @POST("/websocket/ride/create")
    suspend fun clientRide(@Body clientRideRequest: ClientRideRequest): Response<ServerResponse<ClientRideResponse>>

    @PUT("/websocket/ride/update_amount/{ride_id}")
    suspend fun putClientRideAmount(
        @Path("ride_id") rideId: String,
        @Query("amount") amount: Number
    ): Response<ServerResponseEmpty>

    @POST("/websocket/offer/{offer_id}/accept")
    suspend fun clientAcceptOffer(@Path("offer_id") offerId: String): Response<ServerResponseEmpty>

    @POST("/websocket/offer/{offer_id}/reject")
    suspend fun clientRejectOffer(@Path("offer_id") offerId: String): Response<ServerResponseEmpty>

    @POST("/websocket/ride/cancel/{ride_id}")
    suspend fun clientCancelOffer(@Path("ride_id") rideId: String): Response<ServerResponseEmpty>

    @GET("/websocket/get_active_offer_by_ride_id/{ride_id}")
    suspend fun getActiveOfferByRideId(@Path("ride_id") rideId: String): Response<ServerResponseEmpty>

    @GET("/websocket/get_active_ride_by_passenger")
    suspend fun getActiveRideByPassenger(@Query("user_id") userId: Int): Response<ServerResponse<NetworkActiveRideResponse>>

    @POST("/websocket/get_ride_price")
    suspend fun getRidePrice(@Body getRideRecommendedRidePriceRequest: NetworkGetRecommendedRidePriceRequest): Response<ServerResponse<NetworkGetRecommendedRidePriceResponse>>
}