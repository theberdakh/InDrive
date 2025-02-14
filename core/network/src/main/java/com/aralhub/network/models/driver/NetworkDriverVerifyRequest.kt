package com.aralhub.network.models.driver

import com.google.gson.annotations.SerializedName

data class NetworkDriverVerifyRequest(
    @SerializedName("phone_number")
    val phoneNumber: String,
    val code: String
)
