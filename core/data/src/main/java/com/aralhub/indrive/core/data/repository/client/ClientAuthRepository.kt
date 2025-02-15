package com.aralhub.indrive.core.data.repository.client

import com.aralhub.indrive.core.data.model.client.ClientAddPhoneRequest
import com.aralhub.indrive.core.data.model.client.ClientVerifyPhoneRequest
import com.aralhub.indrive.core.data.result.Result

interface ClientAuthRepository {
    suspend fun clientAddPhone(clientAddPhoneRequest: ClientAddPhoneRequest): Result<Boolean>
    suspend fun clientAuthVerify(clientVerifyPhoneRequest: ClientVerifyPhoneRequest): Result<Boolean>
}