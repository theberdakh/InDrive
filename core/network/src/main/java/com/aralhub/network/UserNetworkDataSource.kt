package com.aralhub.network

import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.user.NetworkAuthRequest
import com.aralhub.network.models.user.NetworkMeResponse
import com.aralhub.network.models.user.NetworkProfileRequest
import com.aralhub.network.models.user.NetworkRefreshResponse
import com.aralhub.network.models.user.NetworkVerifyRequest
import com.aralhub.network.models.user.NetworkVerifyResponse

interface UserNetworkDataSource {
    suspend fun auth(networkAuthRequest: NetworkAuthRequest): NetworkResult<Unit>
    suspend fun verify(networkVerifyRequest: NetworkVerifyRequest): NetworkResult<NetworkVerifyResponse>
    suspend fun profile(networkProfileRequest: NetworkProfileRequest): NetworkResult<Unit>
    suspend fun getMe(): NetworkResult<NetworkMeResponse>
    suspend fun refresh(): NetworkResult<NetworkRefreshResponse>
    suspend fun logout(): NetworkResult<Unit>
    suspend fun photo(): NetworkResult<Unit>
    suspend fun deleteProfile(): NetworkResult<Unit>
}