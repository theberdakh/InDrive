package com.aralhub.indrive.core.data.model.driver

import com.aralhub.network.models.driver.NetworkRideCompletedResponse

data class RideCompleted(
    val amount: Int,
    val waitAmount: Int,
    val totalAmount: Int,
    val commissionAmount: Int,
    val cashbackAmount: Double?,
    val duration: Double,
    val distance: Double,
    val paymentMethodId: Int
)

fun NetworkRideCompletedResponse.toDomain(): RideCompleted =
    with(this) {
        return RideCompleted(
            amount = amount,
            waitAmount = waitAmount,
            totalAmount = totalAmount,
            commissionAmount = commissionAmount,
            cashbackAmount = cashbackAmount,
            duration = duration,
            distance = distance,
            paymentMethodId = paymentMethod.id
        )
    }