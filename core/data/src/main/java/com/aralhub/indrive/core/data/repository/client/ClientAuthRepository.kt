package com.aralhub.indrive.core.data.repository.client

import com.aralhub.indrive.core.data.model.client.ClientProfile
import com.aralhub.indrive.core.data.result.Result

interface ClientAuthRepository {
    suspend fun clientAuth(phone: String): Result<Boolean>
    suspend fun userVerify(phone: String, code: String): Result<Boolean>
    suspend fun userProfile(fullName: String): Result<Boolean>
    suspend fun userMe(): Result<ClientProfile>
}