package com.aralhub.araltaxi.core.domain.client

import com.aralhub.indrive.core.data.model.client.ClientAddPhoneRequest
import com.aralhub.indrive.core.data.model.client.ClientVerifyRequest
import com.aralhub.indrive.core.data.repository.client.ClientAuthRepository
import javax.inject.Inject

class ClientAuthUseCase @Inject constructor(
    private val repository: ClientAuthRepository,
) {
    suspend operator fun invoke(authRequest: ClientAddPhoneRequest) =
        repository.clientAuth(authRequest)

    suspend fun userVerify(data: ClientVerifyRequest) =
        repository.userVerify(
            data
        )
}