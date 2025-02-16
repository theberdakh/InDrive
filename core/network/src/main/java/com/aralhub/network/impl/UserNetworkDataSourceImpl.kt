package com.aralhub.network.impl

import com.aralhub.network.UserNetworkDataSource
import com.aralhub.network.api.UserNetworkApi
import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.NetworkWrappedResult
import com.aralhub.network.models.user.NetworkAuthResponseData
import com.aralhub.network.models.user.NetworkUserAuthRequest
import com.aralhub.network.models.user.NetworkUserMeResponse
import com.aralhub.network.models.user.NetworkUserProfileRequest
import com.aralhub.network.models.user.NetworkUserRefreshResponse
import com.aralhub.network.models.user.NetworkUserVerifyRequest
import com.aralhub.network.models.user.NetworkUserVerifyResponse
import com.aralhub.network.utils.NetworkEx.safeRequest
import com.aralhub.network.utils.NetworkEx.safeRequestServerResponse
import com.aralhub.network.utils.NetworkUtils.safeApiCall
import javax.inject.Inject

class UserNetworkDataSourceImpl @Inject constructor(private val api: UserNetworkApi) : UserNetworkDataSource {
    override suspend fun userAuth(networkUserAuthRequest: NetworkUserAuthRequest): NetworkWrappedResult<NetworkAuthResponseData> {
        return safeApiCall {
            api.userAuth(networkUserAuthRequest)
        }
    }

    override suspend fun userVerify(networkUserVerifyRequest: NetworkUserVerifyRequest): NetworkResult<NetworkUserVerifyResponse> {
        return api.userVerify(networkUserVerifyRequest).safeRequestServerResponse()
    }

    override suspend fun userProfile(networkUserProfileRequest: NetworkUserProfileRequest): NetworkResult<Unit> {
        return api.userProfile(networkUserProfileRequest).safeRequestServerResponse()
    }

    override suspend fun getUserMe(): NetworkResult<NetworkUserMeResponse> {
        return api.getUserMe().safeRequest()
    }

    override suspend fun userRefresh(): NetworkResult<NetworkUserRefreshResponse> {
        return api.userRefresh().safeRequest()
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