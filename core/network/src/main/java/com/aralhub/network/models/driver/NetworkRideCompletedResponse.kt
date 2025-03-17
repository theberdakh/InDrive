package com.aralhub.network.models.driver

import com.aralhub.network.models.payment.NetworkPaymentMethod
import com.google.gson.annotations.SerializedName

data class NetworkRideCompletedResponse(
    val amount: Int,
    @SerializedName("wait_amount")
    val waitAmount: Double,
    @SerializedName("total_amount")
    val totalAmount: Int,
    @SerializedName("commission_amount")
    val commissionAmount: Int,
    @SerializedName("cashback_amount")
    val cashbackAmount: Double?,
    val duration: Double,
    val distance: Double,
    @SerializedName("payment_method")
    val paymentMethod: NetworkPaymentMethod
)
