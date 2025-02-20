package com.aralhub.network

import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.NetworkWrappedResult
import com.aralhub.network.models.ServerResponse
import com.aralhub.network.models.user.NetworkAuthResponseData
import com.aralhub.network.models.user.NetworkUserAuthRequest
import com.aralhub.network.models.user.NetworkUserMeResponse
import com.aralhub.network.models.user.NetworkUserProfileRequest
import com.aralhub.network.models.user.NetworkUserRefreshResponse
import com.aralhub.network.models.user.NetworkUserVerifyRequest
import com.aralhub.network.models.user.NetworkUserVerifyResponse
import com.aralhub.network.utils.RefreshTokenRequestData

interface UserNetworkDataSource {
    suspend fun userAuth(networkUserAuthRequest: NetworkUserAuthRequest): NetworkWrappedResult<NetworkAuthResponseData>
    suspend fun userVerify(networkUserVerifyRequest: NetworkUserVerifyRequest): NetworkWrappedResult<ServerResponse<NetworkUserVerifyResponse>>
    suspend fun userProfile(networkUserProfileRequest: NetworkUserProfileRequest): NetworkResult<Unit>
    suspend fun getUserMe(): NetworkResult<NetworkUserMeResponse>
    suspend fun userLogout(): NetworkResult<String>
    suspend fun userPhoto(): NetworkResult<Unit>
    suspend fun deleteUserProfile(): NetworkResult<Unit>
    suspend fun userRefresh(data: RefreshTokenRequestData): NetworkWrappedResult<ServerResponse<NetworkUserRefreshResponse>>
}