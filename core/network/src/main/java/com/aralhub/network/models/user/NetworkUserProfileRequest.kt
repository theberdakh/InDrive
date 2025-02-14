package com.aralhub.network.models.user

import com.google.gson.annotations.SerializedName

data class NetworkUserProfileRequest(
    @SerializedName("full_name")
    val fullName: String
)
