package com.aralhub.network.models.user

import com.google.gson.annotations.SerializedName

data class NetworkUserAuthRequest(
    @SerializedName("phone_number")
    val phoneNumber: String
)
