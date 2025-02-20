package com.aralhub.network.impl

import com.aralhub.network.UserNetworkDataSource
import com.aralhub.network.api.UserNetworkApi
import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.ServerResponse
import com.aralhub.network.models.user.NetworkAuthResponseData
import com.aralhub.network.models.user.NetworkUserAuthRequest
import com.aralhub.network.models.user.NetworkUserMeResponse
import com.aralhub.network.models.user.NetworkUserProfileRequest
import com.aralhub.network.models.user.NetworkUserRefreshResponse
import com.aralhub.network.models.user.NetworkUserVerifyRequest
import com.aralhub.network.models.user.NetworkUserVerifyResponse
import com.aralhub.network.utils.NetworkEx.safeRequest
import com.aralhub.network.utils.NetworkEx.safeRequestServerResponse
import com.aralhub.network.utils.RefreshTokenRequestData
import javax.inject.Inject

class UserNetworkDataSourceImpl @Inject constructor(private val api: UserNetworkApi) : UserNetworkDataSource {
    override suspend fun userAuth(networkUserAuthRequest: NetworkUserAuthRequest): NetworkResult<NetworkAuthResponseData> {
        return api.userAuth(networkUserAuthRequest).safeRequest()
    }

    override suspend fun userVerify(networkUserVerifyRequest: NetworkUserVerifyRequest): NetworkResult<NetworkUserVerifyResponse> {
        return api.userVerify(networkUserVerifyRequest).safeRequestServerResponse()
    }

    override suspend fun userProfile(networkUserProfileRequest: NetworkUserProfileRequest): NetworkResult<NetworkAuthResponseData> {
        return api.userProfile(networkUserProfileRequest).safeRequest()
    }

    override suspend fun getUserMe(): NetworkResult<NetworkUserMeResponse> {
        return api.getUserMe().safeRequest()
    }

    override suspend fun userRefresh(data: RefreshTokenRequestData): NetworkResult<NetworkUserRefreshResponse> {
        return api.userRefresh(data).safeRequestServerResponse()
    }

    override suspend fun userLogout(): NetworkResult<String> {
        return api.userLogout().safeRequest()
    }

    override suspend fun userPhoto(): NetworkResult<Unit> {
        return api.userPhoto().safeRequestServerResponse()
    }

    override suspend fun deleteUserProfile(): NetworkResult<Unit> {
        return api.deleteUserProfile().safeRequestServerResponse()
    }
}