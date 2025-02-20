package com.aralhub.network.models.address

import com.google.gson.annotations.SerializedName

data class NetworkAddressByUserIdResponse(
    @SerializedName("user_id")
    val userId: Int,
    val name: String,
    val address: String,
    val latitude: Int,
    val longitude: Int,
    val id: Int
)