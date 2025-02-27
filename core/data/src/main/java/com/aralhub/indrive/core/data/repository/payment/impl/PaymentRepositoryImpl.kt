package com.aralhub.indrive.core.data.repository.payment.impl

import com.aralhub.indrive.core.data.model.payment.PaymentMethod
import com.aralhub.indrive.core.data.model.payment.PaymentMethodType
import com.aralhub.indrive.core.data.repository.payment.PaymentRepository
import com.aralhub.indrive.core.data.result.Result
import com.aralhub.network.PaymentMethodNetworkDataSource
import com.aralhub.network.models.NetworkResult
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(private val paymentMethodNetworkDataSource: PaymentMethodNetworkDataSource) :
    PaymentRepository {
    override suspend fun getPaymentMethods(): Result<List<PaymentMethod>> {
        return paymentMethodNetworkDataSource.getPaymentMethods().let {
            when (it) {
                is NetworkResult.Error -> Result.Error(it.message)
                is NetworkResult.Success -> Result.Success(it.data.map { networkPaymentMethod ->
                    PaymentMethod(
                        id = networkPaymentMethod.id,
                        name = networkPaymentMethod.name,
                        isActive = networkPaymentMethod.isActive
                    )
                })
            }
        }
    }

    override suspend fun getActivePaymentMethod(): Result<PaymentMethod> {
        return paymentMethodNetworkDataSource.getActivePaymentMethod().let {
            when (it) {
                is NetworkResult.Error -> Result.Error(it.message)
                is NetworkResult.Success -> Result.Success(PaymentMethod(
                    id = it.data.id,
                    name = it.data.name,
                    isActive = it.data.isActive,
                ))
            }
        }
    }

}