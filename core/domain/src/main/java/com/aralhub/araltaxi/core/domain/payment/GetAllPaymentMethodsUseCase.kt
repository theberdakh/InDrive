package com.aralhub.araltaxi.core.domain.payment

import com.aralhub.indrive.core.data.repository.payment.PaymentRepository
import javax.inject.Inject

class GetAllPaymentMethodsUseCase @Inject constructor(private val paymentRepository: PaymentRepository) {
    suspend operator fun invoke() = paymentRepository.getPaymentMethods()
}