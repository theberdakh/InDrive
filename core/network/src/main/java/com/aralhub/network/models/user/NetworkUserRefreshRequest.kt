package com.aralhub.network.models.user

import com.google.gson.annotations.SerializedName

data class NetworkUserRefreshResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("token_type")
    val tokenType: String
)
