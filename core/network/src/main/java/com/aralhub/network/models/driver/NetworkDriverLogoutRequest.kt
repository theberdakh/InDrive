package com.aralhub.network.models.driver

import com.google.gson.annotations.SerializedName

data class NetworkDriverLogoutRequest(
    @SerializedName("refresh_token")
    val refreshToken: String
)
