package com.aralhub.network.impl

import com.aralhub.network.PaymentMethodNetworkDataSource
import com.aralhub.network.api.PaymentMethodsNetworkApi
import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.payment.NetworkPaymentMethod
import com.aralhub.network.utils.NetworkEx.safeRequestServerResponse
import javax.inject.Inject

class PaymentMethodNetworkDataSourceImpl @Inject constructor(private val api: PaymentMethodsNetworkApi): PaymentMethodNetworkDataSource {
    override suspend fun getPaymentMethods(): NetworkResult<List<NetworkPaymentMethod>> {
        return api.getPaymentMethods().safeRequestServerResponse()
    }

    override suspend fun getActivePaymentMethod(): NetworkResult<NetworkPaymentMethod> {
        return api.getActivePaymentMethod().safeRequestServerResponse()
    }

}