package com.aralhub.network.models.user

import com.google.gson.annotations.SerializedName

data class NetworkLogoutRequest(
    @SerializedName("refresh_token")
    val refreshToken: String
)
