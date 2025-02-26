package com.aralhub.indrive.core.data.model.address


data class CreateAddressRequest(
    val userId: Int,
    val name: String,
    val address: String,
    val latitude: Number,
    val longitude: Number
)

data class Address(
    val id: Int,
    val userId: Int,
    val name: String,
    val address: String,
    val latitude: Number,
    val longitude: Number
)