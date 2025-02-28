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
import okhttp3.MultipartBody
import com.aralhub.network.models.WebSocketServerResponse
import com.aralhub.network.models.driver.DriverInfoWithVehicleResponse
import com.aralhub.network.models.driver.NetworkDriverAuthRequest
import com.aralhub.network.models.driver.NetworkDriverBalanceResponse
import com.aralhub.network.models.driver.NetworkDriverCardRequest
import com.aralhub.network.models.driver.NetworkDriverCardResponse
import com.aralhub.network.models.driver.NetworkDriverInfoResponse
import com.aralhub.network.models.driver.NetworkDriverLogoutRequest
import com.aralhub.network.models.driver.NetworkDriverVerifyRequest
import com.aralhub.network.models.driver.NetworkDriverVerifyResponse
import com.aralhub.network.models.location.NetworkSendLocationRequest
import com.aralhub.network.models.location.NetworkSendLocationRequestWithoutType
import com.aralhub.network.models.offer.NetworkActiveOfferResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

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

    @Multipart
    @POST("/driver/photo")
    suspend fun driverPhoto(@Part photo: MultipartBody.Part): Response<ServerResponseEmpty>

    @POST("/websocket/ride/get-rides")
    suspend fun getActiveRides(@Body sendLocationRequest: NetworkSendLocationRequestWithoutType): Response<ServerResponse<List<WebSocketServerResponse<NetworkActiveOfferResponse>>>>

}