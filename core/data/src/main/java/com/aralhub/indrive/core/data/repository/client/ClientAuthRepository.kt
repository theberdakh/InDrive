package com.aralhub.indrive.core.data.repository.client

import com.aralhub.indrive.core.data.model.client.AuthRequest
import com.aralhub.indrive.core.data.model.client.AuthResponse
import com.aralhub.indrive.core.data.model.client.ClientVerifyRequest
import com.aralhub.indrive.core.data.model.client.ClientVerifyResponse
import com.aralhub.indrive.core.data.result.WrappedResult
import com.aralhub.network.models.user.NetworkUserVerifyRequest
import com.aralhub.network.models.user.NetworkUserVerifyResponse

interface ClientAuthRepository {
    suspend fun clientAuth(authRequest: AuthRequest): WrappedResult<AuthResponse>
    suspend fun userVerify(networkUserVerifyRequest: ClientVerifyRequest): WrappedResult<ClientVerifyResponse>
}