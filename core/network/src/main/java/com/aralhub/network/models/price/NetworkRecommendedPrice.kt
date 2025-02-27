package com.aralhub.network.models.price

import com.google.gson.annotations.SerializedName

data class NetworkRecommendedPrice(
    @SerializedName("min_amount")
    val minAmount: Number,
    @SerializedName("max_amount")
    val maxAmount: Number,
    @SerializedName("recommended_amount")
    val recommendedAmount: Number
)
