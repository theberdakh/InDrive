package com.aralhub.network.models.driver

import com.google.gson.annotations.SerializedName

data class NetworkDriverBalanceResponse(
    val balance: Number,
    @SerializedName("day_balance")
    val dayBalance: Number
)