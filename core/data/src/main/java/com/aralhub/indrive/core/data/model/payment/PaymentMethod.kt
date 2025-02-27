package com.aralhub.indrive.core.data.model.payment

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



