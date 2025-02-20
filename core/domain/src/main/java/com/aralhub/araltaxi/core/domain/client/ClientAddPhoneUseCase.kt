package com.aralhub.araltaxi.core.domain.client

import com.aralhub.indrive.core.data.model.client.ClientAddPhoneRequest
import com.aralhub.indrive.core.data.repository.client.ClientAuthRepository
import javax.inject.Inject

class ClientAddPhoneUseCase @Inject constructor(
    private val repository: ClientAuthRepository
) {
    suspend operator fun invoke(phone: String)= repository.clientAuth(ClientAddPhoneRequest(phoneNumber = phone))
}