package com.aralhub.network.models.user

import com.google.gson.annotations.SerializedName

data class NetworkMeResponse(
    val id: Int,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("profile_photo_url")
    val profilePhotoUrl: String,
    @SerializedName("is_fully_registered")
    val isFullyRegistered: Boolean
)