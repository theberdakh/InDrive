package com.aralhub.indrive.core.data.model.payment

import com.aralhub.network.models.payment.NetworkPaymentMethod

fun NetworkPaymentMethod.toDomain() = PaymentMethod(
    id = id,
    name = name,
    isActive = isActive
)

data class PaymentMethod(
    val id: Int,
    val name: String,
    val isActive: Boolean,
    val type: PaymentMethodType = when(id){
        1 -> PaymentMethodType.CASH
        else -> PaymentMethodType.CARD
    }
)

enum class PaymentMethodType(val id: Int) { CARD(2), CASH(1)}



