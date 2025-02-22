package com.aralhub.araltaxi.core.domain.client

import com.aralhub.indrive.core.data.repository.client.ClientAuthRepository
import java.io.File
import javax.inject.Inject

class ClientUploadProfileImageUseCase @Inject constructor(
    private val repository: ClientAuthRepository
) {
    suspend operator fun invoke(file: File) = repository.uploadPhoto(file)
}