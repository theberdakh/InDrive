package com.aralhub.network.models.price

import com.google.gson.annotations.SerializedName

data class NetworkStandardPrice(
    @SerializedName("free_wait_minutes")
    val freeWaitMinutes: Float,
    @SerializedName("wait_price_per_minute")
    val waitPricePerMinute: Int,
    @SerializedName("commission_percent")
    val commissionPercent: Int,
    @SerializedName("cashback_percent")
    val cashbackPercent: Int
)
