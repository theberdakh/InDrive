package com.aralhub.network.models.payment

import com.google.gson.annotations.SerializedName

data class NetworkPaymentMethod(
    val id: Int,
    val name: String,
    @SerializedName("is_active")
    val isActive: Boolean
)
