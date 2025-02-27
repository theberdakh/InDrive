package com.aralhub.network.models.balance

import com.google.gson.annotations.SerializedName

data class NetworkBalance(
    val balance: Number,
    @SerializedName("day_balance")
    val dayBalance: Number
)