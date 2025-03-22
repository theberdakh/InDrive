package com.aralhub.network.models.address

import com.google.gson.annotations.SerializedName

data class NetworkAddress(
    @SerializedName("user_id")
    val userId: Int,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val id: Int
)
