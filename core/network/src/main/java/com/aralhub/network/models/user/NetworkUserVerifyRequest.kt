package com.aralhub.network.models.user

import com.google.gson.annotations.SerializedName

data class NetworkUserVerifyRequest(
    @SerializedName("phone_number")
    val phoneNumber: String,
    val code: String
)

data class NetworkUserVerifyResponse(
    @SerializedName("access_token")
    val token: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("token_type")
    val tokenType: String
)


