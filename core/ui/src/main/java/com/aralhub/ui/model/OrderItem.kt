package com.aralhub.ui.model

data class OrderItem(
    val id: String,
    val name: String,
    val pickUp: String?,
    val avatar: String,
    val roadPrice: String = "",
    val pickUpDistance: String = "",
    val roadDistance: String = "",
)