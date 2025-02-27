package com.aralhub.network.models.passanger

import com.google.gson.annotations.SerializedName

data class NetworkPassenger(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("user_fullname")
    val userFullName: String,
    @SerializedName("user_rating")
    val userRating: Number
)