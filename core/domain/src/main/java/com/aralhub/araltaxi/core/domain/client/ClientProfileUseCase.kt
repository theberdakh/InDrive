package com.aralhub.araltaxi.core.domain.client

import com.aralhub.indrive.core.data.repository.client.ClientAuthRepository
import javax.inject.Inject

class ClientProfileUseCase @Inject constructor(
    private val repository: ClientAuthRepository
) {
    suspend operator fun invoke(fullName: String) = repository.userProfile(fullName)
}