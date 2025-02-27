package com.aralhub.network.api

import com.aralhub.network.models.ServerResponse
import com.aralhub.network.models.ServerResponseEmpty
import com.aralhub.network.models.auth.NetworkAuthToken
import com.aralhub.network.models.balance.NetworkBalance
import com.aralhub.network.models.card.NetworkCard
import com.aralhub.network.models.driver.NetworkDriverActive
import com.aralhub.network.models.driver.NetworkDriverInfo
import com.aralhub.network.requests.auth.NetworkDriverAuthRequest
import com.aralhub.network.requests.logout.NetworkLogoutRequest
import com.aralhub.network.requests.verify.NetworkVerifyRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface DriverNetworkApi {

    @POST("/driver/auth")
    suspend fun driverAuth(@Body networkDriverAuthRequest: NetworkDriverAuthRequest): Response<ServerResponse<String>>

    @POST("/driver/verify/")
    suspend fun driverVerify(@Body networkDriverVerifyRequest: NetworkVerifyRequest): Response<ServerResponse<NetworkAuthToken>>

    @GET("/driver/vehicle")
    suspend fun getDriverVehicle(): Response<ServerResponse<String>>

    @GET("/driver/info")
    suspend fun getDriverInfo(): Response<ServerResponse<NetworkDriverInfo>>

    @GET("/driver/info_with_vehicle")
    suspend fun getDriverInfoWithVehicle(): Response<ServerResponse<NetworkDriverActive>>

    @GET("/driver/card")
    suspend fun getDriverCard(): Response<ServerResponse<NetworkCard>>

    @POST("/driver/card")
    suspend fun driverCard(@Body networkDriverCardRequest: NetworkCard): Response<ServerResponseEmpty>

    @PUT("/driver/card")
    suspend fun updateDriverCard(@Body networkDriverCardRequest: NetworkCard): Response<ServerResponse<ServerResponseEmpty>>

    @GET("/driver/balance")
    suspend fun getDriverBalance(): Response<ServerResponse<NetworkBalance>>

    @POST("/driver/logout/")
    suspend fun driverLogout(@Body networkDriverRequest: NetworkLogoutRequest): Response<ServerResponseEmpty>

}