package com.aralhub.network.api

import com.aralhub.network.models.ServerResponse
import com.aralhub.network.models.rideoption.NetworkRideOption
import retrofit2.Response
import retrofit2.http.GET

interface RideOptionNetworkApi {

    @GET("/options")
    suspend fun getRideOptions(): Response<ServerResponse<List<NetworkRideOption>>>
}