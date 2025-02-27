package com.aralhub.network.models.ride

import com.aralhub.network.models.driver.NetworkDriverActive
import com.aralhub.network.models.option.NetworkOption
import com.aralhub.network.models.payment.NetworkPaymentMethod
import com.google.gson.annotations.SerializedName

data class NetworkRideActive(
    val id: Int,
    val uuid: String,
    val status: String,
    val amount: Int,
    @SerializedName("wait_amount")
    val waitAmount: Int,
    val distance: Int,
    val locations: List<Int>,
    @SerializedName("is_active")
    val isActive: Boolean,
    @SerializedName("created_at")
    val createdAt: String,
    val driver: NetworkDriverActive,
    @SerializedName("payment_method")
    val paymentMethod: NetworkPaymentMethod,
    val options: List<NetworkOption>
)