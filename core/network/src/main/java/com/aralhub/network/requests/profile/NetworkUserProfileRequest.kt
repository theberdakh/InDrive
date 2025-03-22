package com.aralhub.network.requests.profile

import com.google.gson.annotations.SerializedName

data class NetworkUserProfileRequest(
    @SerializedName("full_name")
    val fullName: String
)
