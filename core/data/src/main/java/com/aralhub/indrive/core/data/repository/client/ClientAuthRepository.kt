package com.aralhub.indrive.core.data.repository.client

import com.aralhub.indrive.core.data.model.client.AuthRequest
import com.aralhub.indrive.core.data.model.client.AuthResponse
import com.aralhub.indrive.core.data.result.Result

interface ClientAuthRepository {
    suspend fun clientAuth(authRequest: AuthRequest): Result<Boolean>
}