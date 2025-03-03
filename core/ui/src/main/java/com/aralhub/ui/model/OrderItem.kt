package com.aralhub.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderItem(
    val id: String,
    val name: String,
    val pickUp: String?,
    val avatar: String,
    val roadPrice: String = "",
    val pickUpDistance: String = "",
    val roadDistance: String = "",
    val paymentType: PaymentMethod,
    val pickUpAddress: String,
    val destinationAddress: String? = null
) : Parcelable

enum class PaymentMethod {
    CASH, CARD
}