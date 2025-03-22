package com.aralhub.network.requests.verify

import com.google.gson.annotations.SerializedName

data class NetworkVerifyRequest(
    @SerializedName("phone_number")
    val phoneNumber: String,
    val code: String
)