package com.aralhub.araltaxi.core.domain.driver

import com.aralhub.indrive.core.data.repository.driver.DriverAuthRepository
import java.io.File
import javax.inject.Inject

class DriverUploadProfileImageUseCase @Inject constructor(
    private val repository: DriverAuthRepository
) {
    suspend operator fun invoke(file: File) = repository.driverPhoto(file)
}