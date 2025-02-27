package com.aralhub.network.requests.refresh

import com.google.gson.annotations.SerializedName

data class NetworkRefreshTokenRequest(
    @SerializedName("refresh_token")
    val refreshToken: String
)