package com.aralhub.network.api

import com.aralhub.network.models.ServerResponse
import com.aralhub.network.models.payment.NetworkPaymentMethod
import retrofit2.Response
import retrofit2.http.GET

interface PaymentMethodsNetworkApi {

    @GET("/payment_method/get_active_payment_method")
    suspend fun getActivePaymentMethod(): Response<ServerResponse<NetworkPaymentMethod>>

    @GET("/payment_method/payment_methods")
    suspend fun getPaymentMethods(): Response<ServerResponse<List<NetworkPaymentMethod>>>
}