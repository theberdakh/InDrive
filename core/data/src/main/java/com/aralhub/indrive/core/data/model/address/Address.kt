package com.aralhub.indrive.core.data.model.address

import com.aralhub.network.models.address.NetworkAddress
import com.aralhub.network.requests.address.NetworkAddressRequest


data class CreateAddressRequest(
    val userId: Int,
    val name: String,
    val address: String,
    val latitude: Number,
    val longitude: Number
)

fun CreateAddressRequest.toNetwork() = NetworkAddressRequest(
    this.userId,
    this.name,
    this.address,
    this.latitude,
    this.longitude
)

fun NetworkAddress.toDomain(): Address {
    return Address(
        this.id,
        this.userId,
        this.name,
        this.address,
        this.latitude,
        this.longitude
    )
}

data class Address(
    val id: Int,
    val userId: Int,
    val name: String,
    val address: String,
    val latitude: Number,
    val longitude: Number
)