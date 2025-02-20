package com.aralhub.network.api

import com.aralhub.network.models.ServerResponse
import com.aralhub.network.models.user.NetworkAuthResponseData
import com.aralhub.network.models.user.NetworkUserAuthRequest
import com.aralhub.network.models.user.NetworkUserMeResponse
import com.aralhub.network.models.user.NetworkUserProfileRequest
import com.aralhub.network.models.user.NetworkUserRefreshResponse
import com.aralhub.network.models.user.NetworkUserVerifyRequest
import com.aralhub.network.models.user.NetworkUserVerifyResponse
import com.aralhub.network.utils.RefreshTokenRequestData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserNetworkApi {

    @POST("/user/auth")
    suspend fun userAuth(@Body networkUserAuthRequest: NetworkUserAuthRequest): Response<NetworkAuthResponseData>

    @POST("/user/verify/")
    suspend fun userVerify(@Body networkUserVerifyRequest: NetworkUserVerifyRequest): Response<ServerResponse<NetworkUserVerifyResponse>>

    @POST("/user/profile/")
    suspend fun userProfile(@Body networkUserProfileRequest: NetworkUserProfileRequest): Response<NetworkAuthResponseData>

    @GET("/user/me/")
    fun getUserMe(): Response<NetworkUserMeResponse>

    @POST("/user/token/refresh/")
    suspend fun userRefresh(@Body refreshTokenData: RefreshTokenRequestData): Response<ServerResponse<NetworkUserRefreshResponse>>

    @POST("/user/logout/")
    suspend fun userLogout(): Response<String>

    @POST("/user/profile/photo/")
    fun userPhoto(): Response<ServerResponse<Unit>>

    @DELETE("/user/delete/profile")
    fun deleteUserProfile(): Response<ServerResponse<Unit>>
}