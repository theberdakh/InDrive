package com.aralhub.network.requests.auth

import com.google.gson.annotations.SerializedName

data class NetworkUserAuthRequest(
    @SerializedName("phone_number")
    val phoneNumber: String
)
