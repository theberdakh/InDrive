package com.aralhub.network.requests.logout

import com.google.gson.annotations.SerializedName

data class NetworkLogoutRequest(
    @SerializedName("refresh_token")
    val refreshToken: String
)
