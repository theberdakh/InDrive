package com.aralhub.araltaxi.core.domain.client

import com.aralhub.indrive.core.data.model.client.AuthRequest
import com.aralhub.indrive.core.data.repository.client.ClientAuthRepository
import javax.inject.Inject

class ClientAuthUseCase @Inject constructor(
    private val repository: ClientAuthRepository,
) {
    suspend operator fun invoke(authRequest: AuthRequest) =
        repository.clientAuth(authRequest)
}