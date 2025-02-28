package com.aralhub.araltaxi.core.domain.driver

import com.aralhub.indrive.core.data.model.location.SendLocationRequest
import com.aralhub.indrive.core.data.repository.driver.DriverAuthRepository
import javax.inject.Inject

class GetExistingOrdersUseCase @Inject constructor(
    private val repository: DriverAuthRepository
) {
    suspend operator fun invoke(sendLocationRequest: SendLocationRequest) =
        repository.getActiveOrders(sendLocationRequest)
}