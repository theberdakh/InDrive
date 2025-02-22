package com.aralhub.network.models.address

import com.google.gson.annotations.SerializedName

data class NetworkAddressRequest(
    @SerializedName("user_id")
    val userId: Int,
    val name: String,
    val address: String,
    val latitude: Number,
    val longitude: Number
)

data class NetworkAddressResponse(
    @SerializedName("user_id")
    val userId: Int,
    val name: String,
    val address: String,
    val latitude: Number,
    val longitude: Number,
    val id: Int
)


