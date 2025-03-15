package com.aralhub.network.models.driver

import com.google.gson.annotations.SerializedName

data class NetworkDriverSearch(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("full_name")
    val userFullName: String,
    @SerializedName("rating")
    val userRating: Number
)