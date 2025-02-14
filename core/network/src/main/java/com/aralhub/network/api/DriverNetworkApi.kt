package com.aralhub.network.api

import com.aralhub.network.models.ServerResponse
import com.aralhub.network.models.driver.NetworkDriverAuthRequest
import com.aralhub.network.models.driver.NetworkDriverCardRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DriverNetworkApi {

    @POST("/driver/auth")
    fun driverAuth(@Body networkDriverAuthRequest: NetworkDriverAuthRequest): Response<ServerResponse<String>>

    @POST("/driver/verify")
    fun driverVerify(@Body networkDriverAuthRequest: NetworkDriverAuthRequest): Response<ServerResponse<String>>

    @GET("/driver/vehicle")
    fun getDriverVehicle(): Response<ServerResponse<String>>

    @GET("/driver/info")
    fun getDriverInfo(): Response<ServerResponse<String>>

    @GET("/driver/info_with_vehicle")
    fun getDriverInfoWithVehicle(): Response<ServerResponse<String>>

    @POST("driver/card")
    fun driverCard(@Body networkDriverCardRequest: NetworkDriverCardRequest): Response<ServerResponse<String>>
}