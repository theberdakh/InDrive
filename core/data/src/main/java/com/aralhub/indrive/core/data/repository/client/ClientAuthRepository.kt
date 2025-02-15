package com.aralhub.indrive.core.data.repository.client

import com.aralhub.indrive.core.data.model.client.AuthRequest
import com.aralhub.indrive.core.data.model.client.AuthResponse
import com.aralhub.indrive.core.data.result.WrappedResult

interface ClientAuthRepository {
    suspend fun clientAuth(authRequest: AuthRequest): WrappedResult<AuthResponse>
}