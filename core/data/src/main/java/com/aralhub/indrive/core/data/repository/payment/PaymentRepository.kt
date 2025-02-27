package com.aralhub.indrive.core.data.repository.payment

import com.aralhub.indrive.core.data.model.payment.PaymentMethod
import com.aralhub.indrive.core.data.result.Result

interface PaymentRepository {
    suspend fun getPaymentMethods(): Result<List<PaymentMethod>>
    suspend fun getActivePaymentMethod(): Result<PaymentMethod>
}