package com.aralhub.network.api

import com.aralhub.network.models.ServerResponse
import com.aralhub.network.models.driver.NetworkDriverAuthRequest
import com.aralhub.network.models.driver.NetworkDriverCardRequest
import com.aralhub.network.models.driver.NetworkDriverVerifyRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DriverNetworkApi {

    @POST("/driver/auth")
    suspend fun driverAuth(@Body networkDriverAuthRequest: NetworkDriverAuthRequest): Response<ServerResponse<String>>

    @POST("/driver/verify")
    suspend fun driverVerify(@Body networkDriverVerifyRequest: NetworkDriverVerifyRequest): Response<ServerResponse<String>>

    @GET("/driver/vehicle")
    suspend fun getDriverVehicle(): Response<ServerResponse<String>>

    @GET("/driver/info")
    suspend fun getDriverInfo(): Response<ServerResponse<String>>

    @GET("/driver/info_with_vehicle")
    suspend fun getDriverInfoWithVehicle(): Response<ServerResponse<String>>

    @POST("driver/card")
    suspend fun driverCard(@Body networkDriverCardRequest: NetworkDriverCardRequest): Response<ServerResponse<String>>
}