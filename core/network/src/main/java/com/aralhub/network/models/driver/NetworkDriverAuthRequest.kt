package com.aralhub.network.models.driver

import com.google.gson.annotations.SerializedName

data class NetworkDriverAuthRequest(
    @SerializedName("phone_number")
    val phoneNumber: String
)