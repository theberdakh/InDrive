package com.aralhub.indrive.core.data.repository.client

import com.aralhub.indrive.core.data.model.client.AuthResponse
import com.aralhub.indrive.core.data.model.client.ClientAddPhoneRequest
import com.aralhub.indrive.core.data.model.client.ClientVerifyRequest
import com.aralhub.indrive.core.data.model.client.ClientVerifyResponse
import com.aralhub.indrive.core.data.result.Result

interface ClientAuthRepository {
    suspend fun clientAuth(authRequest: ClientAddPhoneRequest): Result<AuthResponse>
    suspend fun userVerify(networkUserVerifyRequest: ClientVerifyRequest): Result<ClientVerifyResponse>
    suspend fun userProfile(fullName: String): Result<Boolean>
}