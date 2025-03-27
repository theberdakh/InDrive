package com.aralhub.indrive.core.data.model.ride

data class StandardPrice(
    val freeWaitMinutes: Float,
    val waitPricePerMinute: Int,
    val commissionPercent: Int,
    val cashbackPercent: Int
)
