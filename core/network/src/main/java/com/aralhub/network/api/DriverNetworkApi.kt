package com.aralhub.network.api

import com.aralhub.network.models.ServerResponse
import com.aralhub.network.models.ServerResponseEmpty
import com.aralhub.network.models.driver.DriverInfoWithVehicleResponse
import com.aralhub.network.models.driver.NetworkDriverAuthRequest
import com.aralhub.network.models.driver.NetworkDriverBalanceResponse
import com.aralhub.network.models.driver.NetworkDriverCardRequest
import com.aralhub.network.models.driver.NetworkDriverCardResponse
import com.aralhub.network.models.driver.NetworkDriverInfoResponse
import com.aralhub.network.models.driver.NetworkDriverLogoutRequest
import com.aralhub.network.models.driver.NetworkDriverVerifyRequest
import com.aralhub.network.models.driver.NetworkDriverVerifyResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface DriverNetworkApi {

    @POST("/driver/auth")
    suspend fun driverAuth(@Body networkDriverAuthRequest: NetworkDriverAuthRequest): Response<ServerResponse<String>>

    @POST("/driver/verify/")
    suspend fun driverVerify(@Body networkDriverVerifyRequest: NetworkDriverVerifyRequest): Response<ServerResponse<NetworkDriverVerifyResponse>>

    @GET("/driver/vehicle")
    suspend fun getDriverVehicle(): Response<ServerResponse<String>>

    @GET("/driver/info")
    suspend fun getDriverInfo(): Response<ServerResponse<NetworkDriverInfoResponse>>

    @GET("/driver/info_with_vehicle")
    suspend fun getDriverInfoWithVehicle(): Response<ServerResponse<DriverInfoWithVehicleResponse>>

    @GET("/driver/card")
    suspend fun getDriverCard(): Response<ServerResponse<NetworkDriverCardResponse>>

    @POST("/driver/card")
    suspend fun driverCard(@Body networkDriverCardRequest: NetworkDriverCardRequest): Response<ServerResponseEmpty>

    @PUT("/driver/card")
    suspend fun updateDriverCard(@Body networkDriverCardRequest: NetworkDriverCardRequest): Response<ServerResponse<ServerResponseEmpty>>

    @GET("/driver/balance")
    suspend fun getDriverBalance(): Response<ServerResponse<NetworkDriverBalanceResponse>>

    @POST("/driver/logout/")
    suspend fun driverLogout(@Body networkDriverRequest: NetworkDriverLogoutRequest): Response<ServerResponseEmpty>

}