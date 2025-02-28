package com.aralhub.network.models.balance

import com.google.gson.annotations.SerializedName

data class NetworkBalanceInfo(
    val balance: Number?,
    @SerializedName("day_balance")
    val dayBalance: Number?,
    @SerializedName("day_balance_card")
    val dayBalanceCard: Number?,
    @SerializedName("day_balance_cash")
    val dayBalanceCash: Number?,
    @SerializedName("day7_balance")
    val lastWeekBalance: Number?,
    @SerializedName("day30_balance")
    val lastMonthBalance: Number?
)
