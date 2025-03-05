package com.aralhub.ui.model

import android.os.Parcelable
import com.aralhub.ui.R
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
    val paymentType: PaymentType,
    val pickUpAddress: String,
    val destinationAddress: String? = null
) : Parcelable

enum class PaymentType(
    val resId: Int
) {
    CASH(
        resId = R.drawable.ic_cash
    ),
    CARD(
        resId = R.drawable.ic_credit_card_3d
    )
}