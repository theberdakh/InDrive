package com.aralhub.araltaxi.core.domain.client

import com.aralhub.indrive.core.data.model.client.ClientVerifyPhoneRequest
import com.aralhub.indrive.core.data.repository.client.ClientAuthRepository
import com.aralhub.indrive.core.data.result.Result
import javax.inject.Inject

class ClientVerifyPhoneUseCase @Inject constructor(
    private val repository: ClientAuthRepository
) {
    suspend operator fun invoke(phone: String, code: String): Result<Boolean> {
        return repository.clientAuthVerify(ClientVerifyPhoneRequest(phone = phone, code = code))
    }
}