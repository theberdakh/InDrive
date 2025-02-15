package com.aralhub.network

import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.ServerResponse
import com.aralhub.network.models.ServerResponseEmpty
import com.aralhub.network.models.user.NetworkAuthResponseData
import com.aralhub.network.models.user.NetworkUserAuthRequest
import com.aralhub.network.models.user.NetworkUserMeResponse
import com.aralhub.network.models.user.NetworkUserProfileRequest
import com.aralhub.network.models.user.NetworkUserRefreshResponse
import com.aralhub.network.models.user.NetworkUserVerifyRequest
import com.aralhub.network.models.user.NetworkUserVerifyResponse

interface UserNetworkDataSource {
    suspend fun userAuth(networkUserAuthRequest: NetworkUserAuthRequest): NetworkResult<Boolean>
    suspend fun userVerify(networkUserVerifyRequest: NetworkUserVerifyRequest): NetworkResult<NetworkUserVerifyResponse>
    suspend fun userProfile(networkUserProfileRequest: NetworkUserProfileRequest): NetworkResult<Boolean>
    suspend fun getUserMe(): NetworkResult<NetworkUserMeResponse>
    suspend fun userRefresh(): NetworkResult<NetworkUserRefreshResponse>
    suspend fun userLogout(): NetworkResult<String>
    suspend fun userPhoto(): NetworkResult<Boolean>
    suspend fun deleteUserProfile(): NetworkResult<Boolean>
}