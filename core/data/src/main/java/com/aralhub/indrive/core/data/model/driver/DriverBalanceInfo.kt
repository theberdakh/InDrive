package com.aralhub.indrive.core.data.model.driver

data class DriverBalanceInfo(
    val balance: Number,
    val dayBalance: Number,
    val dayBalanceCard: Number,
    val dayBalanceCash: Number,
    val lastWeekBalance: Number,
    val lastMonthBalance: Number
)