package com.aralhub.araltaxi.core.domain.client

import com.aralhub.indrive.core.data.model.client.ClientVerifyRequest
import com.aralhub.indrive.core.data.model.client.ClientVerifyResponse
import com.aralhub.indrive.core.data.repository.client.ClientAuthRepository
import com.aralhub.indrive.core.data.result.Result
import javax.inject.Inject

class ClientVerifyPhoneUseCase @Inject constructor(
    private val repository: ClientAuthRepository
) {
    suspend operator fun invoke(phone: String, code: String) = repository.userVerify(ClientVerifyRequest(code = code, phoneNumber = phone))
}