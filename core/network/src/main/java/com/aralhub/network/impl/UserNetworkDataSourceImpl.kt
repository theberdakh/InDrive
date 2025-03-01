package com.aralhub.network.impl

import com.aralhub.network.UserNetworkDataSource
import com.aralhub.network.api.UserNetworkApi
import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.ServerResponse
import com.aralhub.network.models.user.NetworkAuthResponseData
import com.aralhub.network.models.user.NetworkLogoutRequest
import com.aralhub.network.models.user.NetworkUserAuthRequest
import com.aralhub.network.models.user.NetworkUserMeResponse
import com.aralhub.network.models.user.NetworkUserProfileRequest
import com.aralhub.network.models.user.NetworkUserRefreshResponse
import com.aralhub.network.models.user.NetworkUserVerifyRequest
import com.aralhub.network.models.user.NetworkUserVerifyResponse
import com.aralhub.network.utils.MultipartEx
import com.aralhub.network.utils.NetworkEx.safeRequest
import com.aralhub.network.utils.NetworkEx.safeRequestServerResponse
import com.aralhub.network.utils.RefreshTokenRequestData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
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
        return api.getUserMe().safeRequestServerResponse()
    }

    override suspend fun userRefresh(data: RefreshTokenRequestData): NetworkResult<NetworkUserRefreshResponse> {
        return api.userRefresh(data).safeRequestServerResponse()
    }

    override suspend fun userLogout(refreshToken: String): NetworkResult<NetworkAuthResponseData> {
        return api.userLogout(NetworkLogoutRequest(refreshToken)).safeRequest()
    }

    override suspend fun userPhoto(file: File): NetworkResult<NetworkAuthResponseData> {
        return api.userPhoto(MultipartEx.getMultipartFromFile(file)).safeRequest()
    }

    override suspend fun deleteUserProfile(): NetworkResult<Unit> {
        return api.deleteUserProfile().safeRequestServerResponse()
    }
}