package com.aralhub.network.api

import com.aralhub.network.models.ServerResponse
import com.aralhub.network.models.ServerResponseEmpty
import com.aralhub.network.models.user.NetworkAuthResponseData
import com.aralhub.network.models.user.NetworkUserAuthRequest
import com.aralhub.network.models.user.NetworkUserMeResponse
import com.aralhub.network.models.user.NetworkUserProfileRequest
import com.aralhub.network.models.user.NetworkUserRefreshResponse
import com.aralhub.network.models.user.NetworkUserVerifyRequest
import com.aralhub.network.models.user.NetworkUserVerifyResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface UserNetworkApi {

    @POST("/user/auth")
    suspend fun userAuth(@Body networkUserAuthRequest: NetworkUserAuthRequest): Response<ServerResponseEmpty>

    @POST("/user/verify/")
    fun userVerify(@Body networkUserVerifyRequest: NetworkUserVerifyRequest): Response<ServerResponse<NetworkUserVerifyResponse>>

    @POST("/user/profile/")
    fun userProfile(@Body networkUserProfileRequest: NetworkUserProfileRequest): Response<ServerResponseEmpty>

    @GET("/user/me/")
    fun getUserMe(): Response<NetworkUserMeResponse>

    @POST("/user/token/refresh/")
    fun userRefresh(): Response<NetworkUserRefreshResponse>

    @POST("/user/logout/")
    fun userLogout(): Response<String>

    @POST("/user/profile/photo/")
    fun userPhoto(): Response<ServerResponseEmpty>

    @DELETE("/user/delete/profile")
    fun deleteUserProfile(): Response<ServerResponseEmpty>
}