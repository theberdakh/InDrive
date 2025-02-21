package com.aralhub.network.models.driver

import com.google.gson.annotations.SerializedName

data class NetworkDriverVerifyRequest(
    @SerializedName("phone_number")
    val phoneNumber: String,
    val code: String
)

data class NetworkDriverVerifyResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("token_type")
    val tokenType: String,
)
