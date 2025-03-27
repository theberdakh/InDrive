package com.aralhub.network.models.ride

import com.google.gson.annotations.SerializedName

data class NetworkWaitAmount(
    @SerializedName("wait_amount")
    val waitAmount: Double,
    @SerializedName("wait_start_time")
    val waitStartTime: Double,
    @SerializedName("paid_waiting_time")
    val paidWaitingTime: Double
)
