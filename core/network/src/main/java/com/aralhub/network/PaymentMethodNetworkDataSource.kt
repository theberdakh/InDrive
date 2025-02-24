package com.aralhub.network

import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.payment.NetworkPaymentMethod

interface PaymentMethodNetworkDataSource {

    suspend fun getPaymentMethods() : NetworkResult<List<NetworkPaymentMethod>>
    suspend fun getActivePaymentMethod() : NetworkResult<NetworkPaymentMethod>
}